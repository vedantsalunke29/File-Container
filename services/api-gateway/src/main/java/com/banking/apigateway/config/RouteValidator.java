package com.banking.apigateway.config;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RouteValidator {
    private static final List<String> openEndpoints = List.of(
            "/api/auth/login",
            "/api/auth/register"
    );

    public boolean isSecured(ServerHttpRequest request) {
        return openEndpoints.stream().noneMatch(uri -> request.getURI().getPath().contains(uri));
    }
}

