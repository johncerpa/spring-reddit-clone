package com.john.springredditclone.services;

import com.john.springredditclone.dto.AuthenticationResponse;
import com.john.springredditclone.dto.LoginRequest;
import com.john.springredditclone.dto.RegisterRequest;

public interface IAuthService {
    public void signUp(RegisterRequest registerRequest);

    void verifyAccount(String token);

    AuthenticationResponse login(LoginRequest loginRequest);
}
