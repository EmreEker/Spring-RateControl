package com.emreeker.ratelimit.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.emreeker.ratelimit.annotation.RateLimit;
import com.emreeker.ratelimit.service.RateLimitService;

@Aspect
@Component
public class RateLimitAspect {

    private static final Logger logger = LoggerFactory.getLogger(RateLimitAspect.class);

    private final RateLimitService rateLimitService;

    @Value("${rate.limit.endpoint}")
    private double endpointRate;

    public RateLimitAspect(RateLimitService rateLimitService) {
        this.rateLimitService = rateLimitService;
    }

    @Around("@annotation(rateLimit)")
    public Object handleRateLimit(ProceedingJoinPoint joinPoint, RateLimit rateLimit) throws Throwable {
        double rate = rateLimit.value() == 0.0 ? endpointRate : rateLimit.value();
        logger.debug("Setting rate to {}", rate);
        rateLimitService.setRate(rate);
        
        if (!rateLimitService.tryAcquire()) {
            logger.warn("Rate limit exceeded for method: {}", joinPoint.getSignature());
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Rate Limit Exceeded");
        }
        
        logger.debug("Rate limit passed for method: {}", joinPoint.getSignature());
        return joinPoint.proceed();
    }
}
