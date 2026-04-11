package ratelimiter;

import java.util.ArrayDeque;
import java.util.Deque;

public class UserWindow {
    private final Deque<Long> timestamps;

    public UserWindow() {
        this.timestamps = new ArrayDeque<>();
    }

    public Deque<Long> getTimestamps() {
        return timestamps;
    }
}
