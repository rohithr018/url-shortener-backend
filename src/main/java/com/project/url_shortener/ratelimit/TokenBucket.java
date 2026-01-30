package com.project.url_shortener.ratelimit;

public class TokenBucket {
    private final int capacity;
    private final double refillRatePerSecond;
    private double tokens;
    private long lastRefillTimestamp;

    public TokenBucket(int capacity, double refillRatePerSecond) {
        this.capacity = capacity;
        this.refillRatePerSecond = refillRatePerSecond;
        this.tokens = capacity;
        this.lastRefillTimestamp = System.nanoTime();
    }

    private void refill(){
        long now =System.nanoTime();
        double secondsPassed=(now-lastRefillTimestamp)/1_000_000_000.0;

        double tokensToAdd=secondsPassed*refillRatePerSecond;

        if(tokensToAdd>0){
            tokens=Math.min(capacity,tokens+tokensToAdd);
            lastRefillTimestamp=now;
        }
    }

    public synchronized boolean allowRequest(){
        refill();
        if(tokens>=1){
            tokens-=1;
            return true;
        }
        return false;
    }

}
