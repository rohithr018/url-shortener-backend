package com.project.url_shortener.ratelimit;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimiter {
    private static final int CAPACITY=100;
    private static final double REFILL_RATE=6.0;

    private final Map<String,TokenBucket> buckets=new ConcurrentHashMap<>();

    public boolean allow(String ip){
        TokenBucket bucket = buckets.computeIfAbsent(
                ip,k->new TokenBucket(CAPACITY,REFILL_RATE)
        );
        return bucket.allowRequest();
    }
}
