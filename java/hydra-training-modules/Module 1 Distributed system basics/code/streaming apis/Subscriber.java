import java.util.concurrent.Flow;

public class Subscriber implements Flow.Subscriber<String>
{
    private final String name;

    public Subscriber(final String name)
    {
        this.name = name;
    }

    @Override
    public void onSubscribe(final Flow.Subscription subscription)
    {
        // TODO implement
    }

    @Override
    public void onNext(final String item)
    {
        System.out.println("subscriber:" + name + " received: " + item);
    }

    @Override
    public void onError(final Throwable throwable)
    {
        System.out.println("subscriber:" + name + " received error:" + throwable.getMessage());
    }

    @Override
    public void onComplete()
    {
        System.out.println("subscriber:" + name + " received completion signal");
    }
}
