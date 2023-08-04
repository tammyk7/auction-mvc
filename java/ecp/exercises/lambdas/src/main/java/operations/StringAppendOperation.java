package operations;

public class StringAppendOperation implements Operation {
    public String process(String a, String b) {
        if (a == null || b == null) {
            throw new NullPointerException();
        }
        return a + b;
    }

    @Override
    public Object process(Object a, Object b) {
        if (!(a instanceof String)) {
            throw new IllegalArgumentException("a");
        }

        if (!(b instanceof String)) {
            throw new IllegalArgumentException("b");
        }

        return process((String) a, (String) b);
    }
}
