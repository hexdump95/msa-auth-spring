package com.hexdump95.auth.user;

import com.hexdump95.auth.security.JwtService;
import com.hexdump95.auth.user.dto.LoginRequest;
import com.hexdump95.auth.user.dto.SignUpRequest;
import com.hexdump95.auth.user.dto.JwtResponse;
import com.hexdump95.auth.security.RolesConstants;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ReactiveAuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UserServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository, ReactiveAuthenticationManager authenticationManager, JwtService jwtService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public Mono<JwtResponse> signUp(SignUpRequest signUpRequest) {
        String encryptedPassword = passwordEncoder.encode(signUpRequest.getPassword());
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setUserId(UUID.randomUUID());
        user.setPassword(encryptedPassword);
        user.setRoles(List.of(RolesConstants.USER));

        return userRepository.save(user)
                .then(Mono.fromCallable(() -> {
                    JwtResponse signUpResponse = new JwtResponse();
                    signUpResponse.setAccessToken(jwtService.createToken(user.getUserId()));
                    return signUpResponse;
                }));
    }

    @Override
    public Mono<JwtResponse> login(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
        return authenticationManager.authenticate(authToken)
                .flatMap(auth -> Mono.fromCallable(() -> {
                    JwtResponse signUpResponse = new JwtResponse();
                    signUpResponse.setAccessToken(jwtService.createToken(auth));
                    return signUpResponse;
                }));
    }

}
