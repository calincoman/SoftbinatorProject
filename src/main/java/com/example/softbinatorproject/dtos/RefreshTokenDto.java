package com.example.softbinatorproject.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor @AllArgsConstructor
public class RefreshTokenDto {

    @NotNull
    private String refreshToken;

    // For refresh (of the access token): grantType = "refresh_token"
    @NotNull
    private String grantType;
}

