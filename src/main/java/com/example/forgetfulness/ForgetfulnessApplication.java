package com.example.forgetfulness;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
@EntityScan("com.example.forgetfulness.application.entity")
@EnableJpaRepositories
public class ForgetfulnessApplication {
    public static void main(String[] args) {
        SpringApplication.run(ForgetfulnessApplication.class, args);
    }
}
