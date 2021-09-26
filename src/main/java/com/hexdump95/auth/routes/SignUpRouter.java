package com.hexdump95.auth.routes;

import com.hexdump95.auth.model.UserService;
import com.hexdump95.auth.model.dto.SignUpRequest;
import com.hexdump95.auth.model.dto.SignUpResponse;
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
public class SignUpRouter {

    private static final String SIGNUP_URL = "/api/v1/signUp";
    private final UserService userService;

    public SignUpRouter(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public RouterFunction<ServerResponse> signUpRouter2() {
        return route()
                .POST(
                        SIGNUP_URL,
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
        return ServerResponse.ok().contentType(APPLICATION_JSON)
                .bodyValue(userService.signUp());
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
                                        .type("object").implementation(SignUpResponse.class))
                        ).responseCode("200")
                );
    }

}
