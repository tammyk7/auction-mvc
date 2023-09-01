import com.weareadaptive.hydra.codecs.generated.FlyweightCodec;
import com.weareadaptive.hydra.memory.plugins.RepositoryPluginHost;
import com.weareadaptive.hydra.memory.tables.TableCursor;
import org.agrona.collections.Int2ObjectHashMap;
import org.agrona.collections.Object2IntHashMap;

import java.util.function.Function;

public class OneToOneIndexImpl<RowT extends FlyweightCodec, KeyT> implements OneToOneIndex<RowT, KeyT> {
    private static final int MISSING_VALUE = Integer.MIN_VALUE;
    private final Object2IntHashMap<KeyT> keyToRowIndex = new Object2IntHashMap<>(MISSING_VALUE);
    private final Int2ObjectHashMap<KeyT> rowIndexToKey = new Int2ObjectHashMap<>();
    private long lastSeenMutationSeqNo;
    private RepositoryPluginHost<RowT> host;
    private TableCursor<RowT> cursor;
    private final Function<RowT, KeyT> keyExtractor;

    public OneToOneIndexImpl(final Function<RowT, KeyT> keyExtractor) {
        this.keyExtractor = keyExtractor;
    }

    @Override
    public void onAttached(RepositoryPluginHost<RowT> host, long currentMutationSeqNo) {
        this.lastSeenMutationSeqNo = currentMutationSeqNo;
        this.host = host;
        this.cursor = host.createCursor();

        buildIndex();
    }

    private void buildIndex() {
        host.forEachRowIndex(rowIndex ->
        {
            cursor.wrapRow(rowIndex);
            add(rowIndex, cursor.rowFlyweight());
        });
        cursor.unwrap();
    }

    @Override
    public int doEndOfUnitOfWorkProcessing() {
        return host.pollForDirtyRows(lastSeenMutationSeqNo, this);
    }

    @Override
    public RowT get(KeyT key) {
        host.pollForDirtyRows(lastSeenMutationSeqNo, this);

        final int rowIndex = keyToRowIndex.getValue(key);

        return rowIndex == MISSING_VALUE ? null : host.borrowRowForUnitOfWork(rowIndex);
    }

    @Override
    public void onRowDirty(int rowIndex) {
        final KeyT indexedValue = rowIndexToKey.get(rowIndex);
        final boolean isInIndex = indexedValue != null;
        final boolean isInTable = cursor.rowExists(rowIndex);

        if (isInIndex && isInTable) {
            cursor.wrapRow(rowIndex);
            final KeyT cellFlyweight = keyExtractor.apply(cursor.rowFlyweight());
            if (cellFlyweight.equals(indexedValue)) {
                cursor.unwrap();
                return;
            }
            cursor.unwrap();
        }

        if (isInIndex) {
            remove(rowIndex);
        }

        if (isInTable) {
            cursor.wrapRow(rowIndex);
            add(rowIndex, cursor.rowFlyweight());
            cursor.unwrap();
        }
    }

    @Override
    public void onAllRowsDirty() {
        clearIndex();
        buildIndex();
    }

    @Override
    public void onOutOfOrderPollCompleted(long mutationSeqNo) {
        lastSeenMutationSeqNo = mutationSeqNo;
    }

    private void add(final int rowIndex, final RowT rowFlyweight) {
        final KeyT key = keyExtractor.apply(rowFlyweight);

        if (key == null) {
            return;
        }

        final KeyT previousKey = rowIndexToKey.put(rowIndex, key);

        if (previousKey != null) {
            throw new IllegalStateException("Corrupted index.");
        }

        final int existingRowIndex = keyToRowIndex.put(key, rowIndex);

        if (existingRowIndex != MISSING_VALUE) {
            throw new IllegalStateException("Corrupted index.");
        }
    }

    private void remove(final int rowIndex) {
        final KeyT clonedKey = rowIndexToKey.remove(rowIndex);

        if (clonedKey != null) {
            keyToRowIndex.removeKey(clonedKey);
        }
    }

    private void clearIndex() {
        rowIndexToKey.clear();
        keyToRowIndex.clear();
    }
}
