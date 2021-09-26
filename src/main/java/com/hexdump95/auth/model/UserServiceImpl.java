package com.hexdump95.auth.model;

import com.hexdump95.auth.model.dto.SignUpResponse;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public SignUpResponse signUp(){
        SignUpResponse signUpResponse = new SignUpResponse();
        signUpResponse.setMessage("Registrado Satisfactoriamente");
        return signUpResponse;
    }
}
