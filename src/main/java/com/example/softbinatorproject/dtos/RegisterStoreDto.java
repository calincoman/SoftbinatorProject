package com.example.softbinatorproject.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterStoreDto {
    private Long id;

    private String name;

    private String email;

    private String password;

    private String location;

    private String officeNumber;

    private String identificationCode;
}
