package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Aspect
@Component
public class ControllerLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(ControllerLoggingAspect.class);

    private static final java.util.Set<String> SENSITIVE_TYPES = java.util.Set.of(
            "LoginRequest", "RegisterRequest", "ResetPasswordRequest", "ForgotPasswordRequest");

    @Around("execution(* eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller..*(..))")
    public Object logControllerCalls(ProceedingJoinPoint joinPoint) throws Throwable {
        String method = joinPoint.getSignature().toShortString();
        Object[] rawArgs = joinPoint.getArgs();
        String args = Arrays.stream(rawArgs)
                .map(arg -> {
                    if (arg == null) return "null";
                    String typeName = arg.getClass().getSimpleName();
                    return SENSITIVE_TYPES.contains(typeName) ? typeName + "[REDACTED]" : typeName;
                })
                .collect(Collectors.joining(", "));
        long start = System.currentTimeMillis();

        logger.info("controller_request method={} args=[{}]", method, args);
        logger.debug("controller_request_debug method={} argCount={}", method, rawArgs.length);

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
