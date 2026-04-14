package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RepositoryLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(RepositoryLoggingAspect.class);

    @Around("execution(* eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository..*(..))")
    public Object logRepositoryCalls(ProceedingJoinPoint joinPoint) throws Throwable {
        String method = joinPoint.getSignature().toShortString();
        long start = System.currentTimeMillis();

        logger.debug("repository_query_start method={}", method);

        try {
            Object result = joinPoint.proceed();
            long durationMs = System.currentTimeMillis() - start;
            logger.debug("repository_query_success method={} durationMs={}", method, durationMs);
            return result;
        } catch (Exception ex) {
            long durationMs = System.currentTimeMillis() - start;
            logger.error("repository_query_error method={} durationMs={} message={}", method, durationMs,
                    ex.getMessage(), ex);
            throw ex;
        }
    }
}
