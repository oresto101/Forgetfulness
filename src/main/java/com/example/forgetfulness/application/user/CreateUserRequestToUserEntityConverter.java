package com.example.forgetfulness.application.user;

import com.example.forgetfulness.api.user.CreateUserRequest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CreateUserRequestToUserEntityConverter implements Converter<CreateUserRequest, UserEntity> {

    @Override
    public UserEntity convert(final CreateUserRequest createUserRequest) {
        return UserEntity.builder()
                .username(createUserRequest.getUsername())
                .password(createUserRequest.getPassword())
                .name(createUserRequest.getName())
                .surname(createUserRequest.getSurname())
                .email(createUserRequest.getEmail())
                .build();
    }
}
