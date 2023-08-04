package operations;

public class FloatAddOperation implements Operation {
    public Float process(Float a, Float b) {
        if (a == null || b == null) {
            throw new NullPointerException();
        }
        return a + b;
    }

    @Override
    public Object process(Object a, Object b) {
        if (!(a instanceof Float)) {
            throw new IllegalArgumentException("a");
        }

        if (!(b instanceof Float)) {
            throw new IllegalArgumentException("b");
        }

        return process((Float) a, (Float) b);
    }
}
