package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ServiceLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(ServiceLoggingAspect.class);

    @Around("execution(* eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.impl..*(..))")
    public Object logServiceCalls(ProceedingJoinPoint joinPoint) throws Throwable {
        String method = joinPoint.getSignature().toShortString();
        long start = System.currentTimeMillis();

        logger.debug("service_start method={}", method);

        try {
            Object result = joinPoint.proceed();
            long durationMs = System.currentTimeMillis() - start;
            logger.info("service_success method={} durationMs={}", method, durationMs);
            logger.trace("service_trace method={} resultType={}", method,
                    result != null ? result.getClass().getSimpleName() : "null");
            return result;
        } catch (Exception ex) {
            long durationMs = System.currentTimeMillis() - start;
            logger.error("service_error method={} durationMs={} message={}", method, durationMs, ex.getMessage(), ex);
            throw ex;
        }
    }
}
