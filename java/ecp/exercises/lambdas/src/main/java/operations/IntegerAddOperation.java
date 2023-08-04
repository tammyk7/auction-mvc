package operations;

public class IntegerAddOperation implements Operation {

    public int process(int a, int b) {
        return a + b;
    }

    public Integer process(Integer a, Integer b) {
        if (a == null || b == null) {
            throw new NullPointerException();
        }
        return process(a.intValue(), b.intValue());
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
