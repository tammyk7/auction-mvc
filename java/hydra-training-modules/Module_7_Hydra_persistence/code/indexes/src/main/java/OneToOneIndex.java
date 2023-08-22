import com.weareadaptive.hydra.codecs.generated.FlyweightCodec;
import com.weareadaptive.hydra.memory.plugins.DirtyRowHandler;
import com.weareadaptive.hydra.memory.plugins.RepositoryPlugin;

public interface OneToOneIndex<RowT extends FlyweightCodec, KeyT> extends RepositoryPlugin<RowT>, DirtyRowHandler {
    RowT get(KeyT key);
}
