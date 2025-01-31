package com.fastcampus.thread.model.user;


import jakarta.validation.constraints.NotEmpty;

public record UserAuthenticationResponse(


        String accessToken
        ) {
    public UserAuthenticationResponse {

    }
}
