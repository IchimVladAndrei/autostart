package com.autodrive.common.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ApplicationLoggingAspect {

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
