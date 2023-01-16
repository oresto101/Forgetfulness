package com.example.forgetfulness.application.mapper;

import com.example.forgetfulness.api.DTO.request.CreateUserRequest;
import com.example.forgetfulness.application.entity.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CreateUserRequestToUserEntityConverter implements Converter<CreateUserRequest, User> {

    @Override
    public User convert(final CreateUserRequest createUserRequest) {
        return User.builder()
                .username(createUserRequest.getUsername())
                .password(createUserRequest.getPassword())
                .name(createUserRequest.getName())
                .surname(createUserRequest.getSurname())
                .email(createUserRequest.getEmail())
                .build();
    }
}
