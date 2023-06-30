package com.weareadaptive.util;

import com.weareadaptive.cluster.services.OMSService;
import com.weareadaptive.cluster.services.oms.Order;
import com.weareadaptive.cluster.services.oms.OrderbookImpl;
import com.weareadaptive.cluster.services.oms.util.Side;
import com.weareadaptive.sbe.EndOfSnapshotDecoder;
import com.weareadaptive.sbe.EndOfSnapshotEncoder;
import com.weareadaptive.sbe.MessageHeaderDecoder;
import com.weareadaptive.sbe.MessageHeaderEncoder;
import com.weareadaptive.sbe.OrderIdSnapshotDecoder;
import com.weareadaptive.sbe.OrderIdSnapshotEncoder;
import com.weareadaptive.sbe.OrderSnapshotDecoder;
import com.weareadaptive.sbe.OrderSnapshotEncoder;

import org.agrona.DirectBuffer;
import org.agrona.ExpandableDirectByteBuffer;
import org.agrona.concurrent.BusySpinIdleStrategy;
import org.agrona.concurrent.IdleStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.aeron.ExclusivePublication;
import io.aeron.Image;
import io.aeron.Publication;
import io.aeron.logbuffer.FragmentHandler;
import io.aeron.logbuffer.Header;

public class SnapshotManager implements FragmentHandler
{
    private static final Logger LOGGER = LoggerFactory.getLogger(OMSService.class);
    private static final int RETRY_COUNT = 3;
    private final OrderbookImpl orderbook;
    private final IdleStrategy idleStrategy = new BusySpinIdleStrategy();
    private final ExpandableDirectByteBuffer buffer = new ExpandableDirectByteBuffer(1024);
    private final MessageHeaderEncoder headerEncoder = new MessageHeaderEncoder();
    private final MessageHeaderDecoder headerDecoder = new MessageHeaderDecoder();
    private final OrderIdSnapshotEncoder orderIdEncoder = new OrderIdSnapshotEncoder();
    private final OrderIdSnapshotDecoder orderIdDecoder = new OrderIdSnapshotDecoder();
    private final OrderSnapshotEncoder orderEncoder = new OrderSnapshotEncoder();
    private final OrderSnapshotDecoder orderDecoder = new OrderSnapshotDecoder();
    private final EndOfSnapshotEncoder endOfSnapshotEncoder = new EndOfSnapshotEncoder();
    private final EndOfSnapshotDecoder endOfSnapshotDecoder = new EndOfSnapshotDecoder();
    private boolean snapshotFullyLoaded = false;

    public SnapshotManager(
        final OrderbookImpl orderbook
    )
    {
        this.orderbook = orderbook;
    }

    /**
     * Called by the clustered service once a snapshot needs to be taken
     *
     * @param snapshotPublication the publication to write snapshot data to
     */
    public void takeSnapshot(final ExclusivePublication snapshotPublication)
    {
        LOGGER.info("Starting snapshot...");
        offerOrderID(snapshotPublication);
        offerOrders(snapshotPublication);
        offerEndOfSnapshotMarker(snapshotPublication);
        LOGGER.info("Snapshot complete");
    }

    /**
     * Offers the orderId to the snapshot publication using the OrderIdSnapshotEncoder
     *
     * @param snapshotPublication the publication to offer the snapshot data to
     */
    private void offerOrderID(final ExclusivePublication snapshotPublication)
    {
        headerEncoder.wrap(buffer, 0);
        orderIdEncoder.wrapAndApplyHeader(buffer, 0, headerEncoder);
        orderIdEncoder.orderId(orderbook.getOrderId());
        retryingOffer(snapshotPublication, buffer, headerEncoder.encodedLength() + orderIdEncoder.encodedLength());
    }

    /**
     * Offers the orders to the snapshot publication using the OrderSnapshotEncoder
     *
     * @param snapshotPublication the publication to offer the snapshot data to
     */
    private void offerOrders(final ExclusivePublication snapshotPublication)
    {
        headerEncoder.wrap(buffer, 0);
        orderbook.getOrders().values().forEach(order ->
        {
            orderEncoder.wrapAndApplyHeader(buffer, 0, headerEncoder);
            orderEncoder.orderId(order.getOrderId());
            orderEncoder.price(order.getPrice());
            orderEncoder.size(order.getSize());
            orderEncoder.side(order.getSide().getValue());
            retryingOffer(snapshotPublication, buffer, headerEncoder.encodedLength() + orderEncoder.encodedLength());
        });
    }

    private void offerEndOfSnapshotMarker(final ExclusivePublication snapshotPublication)
    {
        headerEncoder.wrap(buffer, 0);
        endOfSnapshotEncoder.wrapAndApplyHeader(buffer, 0, headerEncoder);
        retryingOffer(snapshotPublication, buffer, headerEncoder.encodedLength() + endOfSnapshotEncoder.encodedLength());
    }

    /**
     * Called by the clustered service once a snapshot has been provided by the cluster
     *
     * @param snapshotImage the image to read snapshot data from
     */
    public void loadSnapshot(final Image snapshotImage)
    {
        LOGGER.info("Loading snapshot...");
        snapshotFullyLoaded = false;
        idleStrategy.reset();
        while (!snapshotImage.isEndOfStream())
        {
            idleStrategy.idle(snapshotImage.poll(this, 20));
        }

        if (!snapshotFullyLoaded)
        {
            LOGGER.warn("Snapshot load not completed; no end of snapshot marker found");
        }
        LOGGER.info("Snapshot load complete.");
    }

    @Override
    public void onFragment(final DirectBuffer buffer, final int offset, final int length, final Header header)
    {
        if (length < MessageHeaderDecoder.ENCODED_LENGTH)
        {
            return;
        }

        headerDecoder.wrap(buffer, offset);

        switch (headerDecoder.templateId())
        {
            case OrderIdSnapshotDecoder.TEMPLATE_ID ->
            {
                orderIdDecoder.wrapAndApplyHeader(buffer, offset, headerDecoder);
                orderbook.setOrderId(orderIdDecoder.orderId());
            }
            case OrderSnapshotDecoder.TEMPLATE_ID ->
            {
                orderDecoder.wrapAndApplyHeader(buffer, offset, headerDecoder);
                final Order order = new Order(orderDecoder.orderId(), orderDecoder.price(), orderDecoder.size(),
                    Side.fromByte(orderDecoder.side()));
                orderbook.getOrders().put(order.getOrderId(), order);
                if (Side.BID == order.getSide())
                {
                    orderbook.getBids().add(order);
                }
                else
                {
                    orderbook.getAsks().add(order);
                }
            }
            case EndOfSnapshotDecoder.TEMPLATE_ID -> snapshotFullyLoaded = true;
            default -> LOGGER.warn("Unknown snapshot message template id: {}", headerDecoder.templateId());
        }
    }

    /**
     * Retries the offer to the publication if it fails on back pressure or admin action.
     * Buffer is assumed to always start at offset 0
     *
     * @param publication the publication to offer data to
     * @param buffer      the buffer holding the source data
     * @param length      the length to write
     */
    private void retryingOffer(final ExclusivePublication publication, final DirectBuffer buffer, final int length)
    {
        final int offset = 0;
        int retries = 0;
        do
        {
            final long result = publication.offer(buffer, offset, length);
            if (result > 0L)
            {
                return;
            }
            else if (result == Publication.ADMIN_ACTION || result == Publication.BACK_PRESSURED)
            {
                LOGGER.warn("backpressure or admin action on snapshot");
            }
            else if (result == Publication.NOT_CONNECTED || result == Publication.MAX_POSITION_EXCEEDED)
            {
                LOGGER.error("unexpected publication state on snapshot: {}", result);
                return;
            }
            idleStrategy.idle();
            retries += 1;
        }
        while (retries < RETRY_COUNT);

        LOGGER.error("failed to offer snapshot within {} retries", RETRY_COUNT);
    }
}
