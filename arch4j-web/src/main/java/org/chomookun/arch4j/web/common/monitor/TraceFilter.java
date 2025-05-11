package org.chomookun.arch4j.web.common.monitor;

import brave.Span;
import brave.Tracer;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class TraceFilter extends OncePerRequestFilter {

    private final Tracer tracer;

    public TraceFilter(Tracer tracer) {
        this.tracer = tracer;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Span span = tracer.currentSpan();
        if (span != null) {
            span.tag("http.path", request.getRequestURI());
            span.tag("http.method", request.getMethod());
        }
        filterChain.doFilter(request, response);
    }
}
