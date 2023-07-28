package com.weareadaptive.assignment_b.engine.service;

import com.weareadaptive.assignment_b.trading.entities.MutablePlaceOrderResponse;
import com.weareadaptive.assignment_b.trading.entities.PlaceOrderRequest;
import com.weareadaptive.assignment_b.trading.services.OrderService;
import com.weareadaptive.assignment_b.trading.services.OrderServiceClientProxy;
import com.weareadaptive.hydra.logging.Logger;
import com.weareadaptive.hydra.logging.LoggerFactory;
import com.weareadaptive.hydra.platform.commontypes.entities.UniqueId;

public class OrderServiceImpl implements OrderService
{
    private static final Logger LOGGER = LoggerFactory.getNotThreadSafeLogger(OrderServiceImpl.class);

    private final OrderServiceClientProxy clientProxy;

    public OrderServiceImpl(final OrderServiceClientProxy userServiceClientProxy)
    {
        this.clientProxy = userServiceClientProxy;
    }

    @Override
    public void placeOrder(final UniqueId correlationId, final PlaceOrderRequest placeOrderRequest)
    {
        try (final MutablePlaceOrderResponse mutablePlaceOrderResponse = clientProxy.acquirePlaceOrderResponse())
        {
            mutablePlaceOrderResponse.success();
            clientProxy.onPlaceOrderResponse(correlationId, mutablePlaceOrderResponse);
            LOGGER.info("PlaceOrderRequest processed in the cluster").log();
        }
    }
}
