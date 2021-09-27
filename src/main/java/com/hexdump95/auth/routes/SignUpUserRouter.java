package com.hexdump95.auth.routes;

import com.hexdump95.auth.user.UserService;
import com.hexdump95.auth.user.dto.SignUpRequest;
import com.hexdump95.auth.user.dto.JwtResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import org.springdoc.core.fn.builders.operation.Builder;

import java.util.Objects;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.content.Builder.contentBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;
import static org.springdoc.core.fn.builders.schema.Builder.schemaBuilder;
import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import static org.springframework.web.reactive.function.server.RequestPredicates.headers;

@Configuration
public class SignUpUserRouter {

    public static final String SIGNUP_USER_URL = "/api/signUp";
    
    private final UserService userService;

    public SignUpUserRouter(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public RouterFunction<ServerResponse> signUpUserRouterFunction() {
        return route()
                .POST(
                        SIGNUP_USER_URL,
                        headers(this::apiVersion1),
                        this::handlerSignUp,
                        this::docSignUp
                ).build();
    }

    private boolean apiVersion1(ServerRequest.Headers headers) {
        return headers.header("accept").stream()
                .anyMatch(h -> h.equals("application/json") || h.matches(".+(v=1)$") || Objects.equals(h, "*/*")
                );
    }

    private Mono<ServerResponse> handlerSignUp(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(SignUpRequest.class)
                .map(userService::signUp)
                .flatMap(r -> ServerResponse.ok().contentType(APPLICATION_JSON)
                        .body(r, JwtResponse.class));
    }

    private void docSignUp(Builder ops) {
        ops
                .tag("signUp")
                .operationId("signUp")
                .requestBody(requestBodyBuilder().implementation(SignUpRequest.class))
                .response(responseBuilder()
                        .content(contentBuilder()
                                .mediaType("application/json;v=1")
                                .schema(schemaBuilder()
                                        .type("object").implementation(JwtResponse.class))
                        ).responseCode("200")
                );
    }

}
