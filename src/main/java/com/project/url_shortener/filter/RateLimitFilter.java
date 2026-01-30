package com.project.url_shortener.filter;

import com.project.url_shortener.exception.TooManyRequestsException;
import com.project.url_shortener.ratelimit.RateLimiter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class RateLimitFilter extends OncePerRequestFilter {
    private final RateLimiter rateLimiter;

    private String extractClientIp(HttpServletRequest request){
        String forwarded= request.getHeader("X-Forwarded-For");
        if(forwarded!=null && !forwarded.isBlank()){
            return forwarded.split(",")[0];
        }
        return request.getRemoteAddr();
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        if (shouldRateLimit(request)) {
            String ip = extractClientIp(request);

            if (!rateLimiter.allow(ip)) {
//                throw new TooManyRequestsException("Too many requests");
                sendTooManyRequests(response);
                return;
            }
        }
        filterChain.doFilter(request,response);

    }

    private boolean shouldRateLimit(HttpServletRequest request) {
        return "POST".equalsIgnoreCase(request.getMethod())
                && request.getRequestURI().equals("/shorten");
    }

    private void sendTooManyRequests(HttpServletResponse response) throws IOException {
        response.setStatus(429);
        response.setContentType("application/json");

        response.getWriter().write("""
            {
              "status": 429,
              "error": "Too many requests"
            }
        """);
    }


}
