package com.example.forgetfulness.api.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    private Long id;
    private String password;
    private String name;
    private String username;
    private String surname;
    private String email;
}
