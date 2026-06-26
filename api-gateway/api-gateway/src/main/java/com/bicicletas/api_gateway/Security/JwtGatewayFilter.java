package com.bicicletas.api_gateway.Security;

import lombok.RequiredArgsConstructor;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;

import org.springframework.cloud.gateway.filter.GlobalFilter;

import org.springframework.core.Ordered;

import org.springframework.http.HttpHeaders;

import org.springframework.http.HttpStatus;

import org.springframework.stereotype.Component;

import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtGatewayFilter
        implements GlobalFilter, Ordered {

    private final JwtService jwtService;

    private final RouteValidator routeValidator;

    @Override
    public Mono<Void> filter(

            ServerWebExchange exchange,

            GatewayFilterChain chain) {

        String path =

                exchange.getRequest()

                        .getURI()

                        .getPath();

        if(routeValidator.isPublic(

                path,

                exchange.getRequest()

                        .getMethod())){

            return chain.filter(exchange);

        }

        String authHeader =

                exchange.getRequest()

                        .getHeaders()

                        .getFirst(

                                HttpHeaders.AUTHORIZATION);

        if(authHeader == null ||

                !authHeader.startsWith(

                        "Bearer ")){

            exchange.getResponse()

                    .setStatusCode(

                            HttpStatus.UNAUTHORIZED);

            return exchange.getResponse()

                    .setComplete();

        }

        String token =

                authHeader.substring(7);

        if(!jwtService.validateToken(token)){

            exchange.getResponse()

                    .setStatusCode(

                            HttpStatus.UNAUTHORIZED);

            return exchange.getResponse()

                    .setComplete();

        }

        String email =

                jwtService.extractUsername(token);

        ServerWebExchange mutatedExchange =

                exchange.mutate()

                        .request(

                                exchange.getRequest()

                                        .mutate()

                                        .header(

                                                "X-Authenticated-User",

                                                email)

                                        .build())

                        .build();

        return chain.filter(

                mutatedExchange);

    }

    @Override
    public int getOrder() {

        return -1;

    }

}