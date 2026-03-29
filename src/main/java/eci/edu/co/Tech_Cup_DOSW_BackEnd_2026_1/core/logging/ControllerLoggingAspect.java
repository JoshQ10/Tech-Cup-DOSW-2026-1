package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class ControllerLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(ControllerLoggingAspect.class);

    @Around("execution(* eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller..*(..))")
    public Object logControllerCalls(ProceedingJoinPoint joinPoint) throws Throwable {
        String method = joinPoint.getSignature().toShortString();
        String args = Arrays.toString(joinPoint.getArgs());
        long start = System.currentTimeMillis();

        logger.info("controller_request method={} args={}", method, args);
        logger.debug("controller_request_debug method={} argCount={}", method, joinPoint.getArgs().length);

        try {
            Object result = joinPoint.proceed();
            long durationMs = System.currentTimeMillis() - start;
            logger.info("controller_response method={} durationMs={} status=OK", method, durationMs);
            return result;
        } catch (Exception ex) {
            long durationMs = System.currentTimeMillis() - start;
            logger.error("controller_error method={} durationMs={} message={}", method, durationMs, ex.getMessage(),
                    ex);
            throw ex;
        }
    }
}
