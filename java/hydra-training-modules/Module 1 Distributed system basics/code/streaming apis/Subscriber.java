import java.util.concurrent.Flow;

public class Subscriber implements Flow.Subscriber<String>
{
    @Override
    public void onSubscribe(final Flow.Subscription subscription)
    {
        // TODO implement
    }

    @Override
    public void onNext(final String item)
    {
        System.out.println(item);
    }

    @Override
    public void onError(final Throwable throwable)
    {
        System.out.println(throwable.getMessage());
    }

    @Override
    public void onComplete()
    {
        System.out.println("Completed stream");
    }
}
