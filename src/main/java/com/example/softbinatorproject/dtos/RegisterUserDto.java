package com.example.softbinatorproject.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class RegisterUserDto {

    private String username;

    private String password;

    private String email;

}

