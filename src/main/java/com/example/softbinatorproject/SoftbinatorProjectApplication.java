package com.example.softbinatorproject;

import com.example.softbinatorproject.utils.KeycloakUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableFeignClients
public class SoftbinatorProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(SoftbinatorProjectApplication.class, args);
    }

}
