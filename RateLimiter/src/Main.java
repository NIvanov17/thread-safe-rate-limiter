import ratelimiter.RateLimiter;
import java.time.Duration;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws InterruptedException {
        RateLimiter limiter = new RateLimiter(3, Duration.ofSeconds(2));

        System.out.println("=== Same user limit test ===");
        for (int i = 1; i <= 4; i++) {
            System.out.println("user1 request " + i + ": " + limiter.allowRequest("user1"));
        }

        System.out.println();
        System.out.println("=== Different users test ===");
        System.out.println("user2 request 1: " + limiter.allowRequest("user2"));
        System.out.println("user2 request 2: " + limiter.allowRequest("user2"));
        System.out.println("user3 request 1: " + limiter.allowRequest("user3"));

        System.out.println();
        System.out.println("=== Waiting for window to expire ===");
        Thread.sleep(2100);

        System.out.println("user1 request after window: " + limiter.allowRequest("user1"));
        System.out.println("user2 request after window: " + limiter.allowRequest("user2"));

        System.out.println();
        System.out.println("=== Burst after reset ===");
        System.out.println("user1 request 1: " + limiter.allowRequest("user1"));
        System.out.println("user1 request 2: " + limiter.allowRequest("user1"));
        System.out.println("user1 request 3: " + limiter.allowRequest("user1"));
        System.out.println("user1 request 4: " + limiter.allowRequest("user1"));
    }
}