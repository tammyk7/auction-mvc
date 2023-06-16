import java.util.Random;

public class Main
{
    public static void main(String[] args)
    {
        final Random random = new Random();
        final Publisher publisher = new Publisher();
        final Subscriber subscriber = new Subscriber();

        publisher.subscribe(subscriber);

        for (int i = 0; i <= random.nextInt(10); i++)
        {
            publisher.publishStreamItem();
            if (random.nextDouble(1.0d) <= 0.02d)
            {
                publisher.errorStream();
            }
        }
        publisher.completeStream();
    }
}
