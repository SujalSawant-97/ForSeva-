package com.example.APIGateway.Util;

import com.example.APIGateway.RouterValidator;
import io.jsonwebtoken.Claims;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;


import java.util.List;




import org.springframework.web.server.ServerWebExchange;


import reactor.core.publisher.Mono;

@Component
public class AuthenticationGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthenticationGatewayFilterFactory.Config> {

    @Autowired
    private RouterValidator routerValidator;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthenticationGatewayFilterFactory() {
        super(Config.class);
        System.out.println(">>> AuthenticationGatewayFilterFactory LOADED <<<");
    }
    @Override
    public String name() {
        return "Authentication";
    }


    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            ServerHttpRequest request = exchange.getRequest();
            String path = request.getURI().getPath();
            System.out.println(">>> GATEWAY FILTER HIT: " + path);

            if (routerValidator.isSecured.test(request)) {

                List<String> authHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION);

                if (authHeader == null || authHeader.isEmpty()) {
                    return onError(exchange, "Missing Authorization Header", HttpStatus.UNAUTHORIZED);
                }

                String token = authHeader.get(0);
                if (token.startsWith("Bearer ")) {
                    token = token.substring(7);
                }

                try {
                    jwtUtil.validateToken(token);
                    Claims claims = jwtUtil.getAllClaimsFromToken(token);

                    request = exchange.getRequest().mutate()
                            .header("X-User-Id", String.valueOf(claims.get("userId")))
                            .header("X-User-Role", String.valueOf(claims.get("role")))
                            .build();
                    System.out.println("Forwarding headers: " + request.getHeaders());
                    System.out.println(token);

                } catch (Exception e) {
                    System.err.println("!!! GATEWAY TOKEN VALIDATION FAILED !!!");
                    System.err.println("Error Message: " + e.getMessage());
                    return onError(exchange, "Invalid JWT Token", HttpStatus.UNAUTHORIZED);
                }
            }

            return chain.filter(exchange.mutate().request(request).build());
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus status) {
        exchange.getResponse().setStatusCode(status);
        return exchange.getResponse().setComplete();
    }

    public static class Config {
    }
}

