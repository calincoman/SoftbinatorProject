package com.example.softbinatorproject.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {

    private String email;

    private String password;

    // For login: grantType = "password"
    private String grantType;

}

