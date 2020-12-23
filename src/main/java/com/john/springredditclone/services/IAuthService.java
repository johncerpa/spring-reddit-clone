package com.john.springredditclone.services;

import com.john.springredditclone.dto.RegisterRequest;

public interface IAuthService {

    public void signUp(RegisterRequest registerRequest);

}
