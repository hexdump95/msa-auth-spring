package com.hexdump95.auth.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SignUpRequest {
    private String username;
    private String password;
    private String email;
}
