package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class RequestTracingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestTracingFilter.class);

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        long start = System.currentTimeMillis();
        String requestId = UUID.randomUUID().toString();
        String method = request.getMethod();
        String path = request.getRequestURI();

        MDC.put("requestId", requestId);
        MDC.put("httpMethod", method);
        MDC.put("requestPath", path);

        logger.info("request_received");
        logger.trace("request_headers userAgent={} contentType={}", request.getHeader("User-Agent"),
                request.getContentType());

        try {
            filterChain.doFilter(request, response);
        } finally {
            long durationMs = System.currentTimeMillis() - start;
            logger.info("request_completed status={} durationMs={}", response.getStatus(), durationMs);
            MDC.clear();
        }
    }
}
