package com.hexdump95.auth.routes;

import com.hexdump95.auth.user.UserService;
import com.hexdump95.auth.user.dto.JwtResponse;
import com.hexdump95.auth.user.dto.LoginRequest;
import org.springdoc.core.fn.builders.operation.Builder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static org.springdoc.core.fn.builders.content.Builder.contentBuilder;
import static org.springdoc.core.fn.builders.schema.Builder.schemaBuilder;
import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;
import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.headers;

@Configuration
public class LoginUserRouter {

    public static final String LOGIN_USER_URL = "/api/login";

    private final UserService userService;

    public LoginUserRouter(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public RouterFunction<ServerResponse> loginUserRouterFunction() {
        return route()
                .POST(
                        LOGIN_USER_URL,
                        headers(this::apiVersion1),
                        this::handlerLogin,
                        this::docLogin
                ).build();
    }

    private boolean apiVersion1(ServerRequest.Headers headers) {
        return headers.header("accept").stream()
                .anyMatch(h -> h.equals("application/json") || h.matches(".+(v=1)$") || Objects.equals(h, "*/*")
                );
    }

    private Mono<ServerResponse> handlerLogin(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(LoginRequest.class)
                .map(userService::login)
                .flatMap(r -> ServerResponse.ok().contentType(APPLICATION_JSON)
                        .body(r, JwtResponse.class));
    }

    private void docLogin(Builder ops) {
        ops
                .tag("login")
                .operationId("login")
                .requestBody(requestBodyBuilder().implementation(LoginRequest.class))
                .response(responseBuilder()
                        .content(contentBuilder()
                                .mediaType("application/json;v=1")
                                .schema(schemaBuilder()
                                        .type("object").implementation(JwtResponse.class))
                        ).responseCode("200")
                );
    }

}
