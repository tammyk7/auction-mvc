package operations;

public class IntegerMultiplyOperation implements Operation {
    public Integer process(Integer a, Integer b) {
        if (a == null || b == null) {
            throw new NullPointerException();
        }
        return a * b;
    }

    @Override
    public Object process(Object a, Object b) {
        if (!(a instanceof Integer)) {
            throw new IllegalArgumentException("a");
        }

        if (!(b instanceof Integer)) {
            throw new IllegalArgumentException("b");
        }

        return process((Integer) a, (Integer) b);
    }
}
