package ratelimiter;

import java.time.Clock;
import java.time.Duration;
import java.util.Deque;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class RateLimiter {
    private final int maxRequests;
    private final long windowMillis;
    private final Clock clock;
    private final ConcurrentHashMap<String, UserWindow> userRequests;

    public RateLimiter(int maxRequests, Duration window) {
        this(maxRequests, window, Clock.systemUTC());
    }

    public RateLimiter(int maxRequests, Duration window, Clock clock) {
        if(maxRequests <= 0) {
            throw new IllegalArgumentException("Max requests must be > 0");
        }
        Objects.requireNonNull(window, "window mustn't be null");
        Objects.requireNonNull(clock, "clock mustn't be null");
        if(window.isNegative() || window.isZero()) {
            throw new IllegalArgumentException("window mustn't positive");

        }
        this.maxRequests = maxRequests;
        this.windowMillis = window.toMillis();
        this.clock = clock;
        this.userRequests = new ConcurrentHashMap<>();
    }

    public boolean allowRequest(String userId) {
        Objects.requireNonNull(userId, "userId mustn't be null");
        long now = clock.millis();
        UserWindow userWindow = userRequests.computeIfAbsent(userId, id -> new UserWindow());

        synchronized (userWindow) {
            Deque<Long> timestamps = userWindow.getTimestamps();
            removeExpired(timestamps, now);
            if(timestamps.size() >= maxRequests){
                return false;
            }
            timestamps.offerLast(now);
            return true;
        }
    }

    private void removeExpired(Deque<Long> timestamps, long now) {
        long validAfter = now - windowMillis;
        while(!timestamps.isEmpty() && timestamps.getFirst() < validAfter) {
            timestamps.pollFirst();
        }
    }
}
