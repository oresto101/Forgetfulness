package com.example.forgetfulness.application.mapper;

import com.example.forgetfulness.api.DTO.request.UserRequest;
import com.example.forgetfulness.application.entity.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
    User userRequestToUser(UserRequest userRequest);
}
