package com.emreeker.ratelimit.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.google.common.util.concurrent.RateLimiter;

@Service
public class RateLimitService {

    private RateLimiter rateLimiter;

    public RateLimitService(@Value("${rate.limit.default}") double defaultRate) {
        this.rateLimiter = RateLimiter.create(defaultRate);
    }

    public boolean tryAcquire() {
        return rateLimiter.tryAcquire();
    }

    public void setRate(Double rate) {
        this.rateLimiter.setRate(rate);
    }
}
