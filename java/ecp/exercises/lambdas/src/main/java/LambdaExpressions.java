import operations.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LambdaExpressions {

    public static void lambdaInfo() {
        // syntax
        // (parameters) -> expression

        // example
        // (x, y) -> x + y

        // behaves differently based on parameter types

        // let's consider creating an abstracted object that adds things of different types
    }

    public static void moreLambdaInfo() {
        // A lambda expression can have zero, one or more parameters
        //
        // () -> { }
        // (x, y) -> x + y
        // (x, y, z) -> x + y + z

        // The body of the lambda expressions can contain zero, one or more statements.
        // Body with single statement -> curly brackets not mandatory + return type matches body expression.
        // More than one statement in body -> must be enclosed in curly brackets.
        //
        // (parameters) -> { statements; }

        // Parameter types -> explicit or inferred
        // Multiple parameters -> mandatory parentheses, separated by commas
        // No parameters -> empty parentheses
        // Single inferred parameter -> parentheses optional
        //
        // () -> expression
        // a -> a * a
        // (Float a) -> a * a
        // (Float a) -> return a * a

        // Cannot
        // - Throw
        // - Be generic

        // Method reference
        // substitute for lambda, parameters have to match
        // can be static or instance class
        // class::method
        //
        // e.g. this::myMethod
        // Integer::intValue
        // myObject::memberMethod
    }

    public static void objectAddOperations() {
        Operation addOperation = new IntegerAddOperation();
        System.out.println(addOperation.process(3, 3));

        Operation multiplyOperation = new IntegerMultiplyOperation();
        System.out.println(multiplyOperation.process(3, 3));

        Operation appendOperation = new StringAppendOperation();
        System.out.println(appendOperation.process("3", "3"));

        Operation floatOperation = new FloatAddOperation();
        System.out.println(floatOperation.process(3.1, 3.2));
    }

    public static void genericAddOperations() {
        GenericOperation<Integer> addOperation = (a, b) -> a + b;
        System.out.println(addOperation.process(3, 3));

        GenericOperation<Integer> multiplyOperation = new GenericOperation<Integer>() {
            @Override
            public Integer process(Integer a, Integer b) {
                return a * b;
            }
        };
        System.out.println(multiplyOperation.process(3, 3));

        GenericOperation<String> appendOperation = new GenericOperation<String>() {
            @Override
            public String process(String a, String b) {
                return a + b;
            }
        };
        System.out.println(appendOperation.process("3", "3"));

        GenericOperation<Float> floatOperation = new GenericOperation<Float>() {
            @Override
            public Float process(Float a, Float b) {
                return a + b;
            }
        };
        System.out.println(floatOperation.process(3.1f, 3.2f));
    }

    public static void lambdaOperations() {
        FunctionalOperation<Integer> addOperation = Integer::sum;
        System.out.println(addOperation.process(3, 3));

        FunctionalOperation<Integer> multiplyOperation = (a, b) -> a * b;
        System.out.println(multiplyOperation.process(3, 3));

        FunctionalOperation<String> appendOperation = (a, b) -> a + b;
        System.out.println(appendOperation.process("3", "3"));

        FunctionalOperation<Float> floatOperation = (Float a, Float b) -> a + b;
        System.out.println(floatOperation.process(3.1f, 3.2f));
    }

    public static void lambaExample1() {

        List<String> pointList = new ArrayList<>();

        pointList.add("1");
        pointList.add("2");

        pointList.forEach( p ->  { System.out.println(p); } );

        pointList.forEach(System.out::println);
    }

    public static void lambaExample2() {
        // don't do this!
        var myThread = new Thread(() -> System.out.println("My Runnable"));
        myThread.start();

        // do this!
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> System.out.println("My Runnable"));
    }

    public static void lambdaExample3() {
        JButton button =  new JButton("Submit");
        button.addActionListener((e) -> {
            System.out.println("Click event triggered !!");
        });
    }

    public static void main(String[] args) {
        objectAddOperations();
        genericAddOperations();
        lambdaOperations();
    }
}
