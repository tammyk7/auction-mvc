package operations;

public interface GenericOperation<T> {
    T process(T a, T b);
}
