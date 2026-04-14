package ratelimiter;

import lombok.Getter;

import java.util.ArrayDeque;
import java.util.Deque;

@Getter
public class UserWindow {
    private final Deque<Long> timestamps;

    public UserWindow() {
        this.timestamps = new ArrayDeque<>();
    }

}
