package com.hexdump95.auth.user;

import com.hexdump95.auth.user.dto.LoginRequest;
import com.hexdump95.auth.user.dto.SignUpRequest;
import com.hexdump95.auth.user.dto.JwtResponse;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<JwtResponse> signUp(SignUpRequest signUpRequest);
    Mono<JwtResponse> login(LoginRequest loginRequest);
}
