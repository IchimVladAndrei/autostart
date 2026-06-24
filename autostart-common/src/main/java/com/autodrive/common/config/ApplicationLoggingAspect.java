package com.autodrive.common.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
@Component
public class ApplicationLoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(ApplicationLoggingAspect.class);

    @Around("within(com.autodrive..service..*)")
    public Object logServiceCall(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        long start = System.nanoTime();
        log.debug("Service call started: {}", methodName);

        try {
            Object result = joinPoint.proceed();
            log.debug("Service call completed: {} in {} ms", methodName, elapsedMillis(start));
            return result;
        } catch (Throwable ex) {
            log.warn("Service call failed: {} in {} ms - {}", methodName, elapsedMillis(start), ex.getMessage());
            throw ex;
        }
    }

    private long elapsedMillis(long start) {
        return (System.nanoTime() - start) / 1_000_000;
    }
}
