package com.john.springredditclone.services;

import com.john.springredditclone.dto.RegisterRequest;
import org.springframework.stereotype.Service;

public interface IAuthService {
    public void signUp(RegisterRequest registerRequest);
}
