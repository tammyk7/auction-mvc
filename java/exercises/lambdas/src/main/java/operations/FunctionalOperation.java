package operations;

@FunctionalInterface
public interface FunctionalOperation<T> {
    T process(T a, T b);
}
