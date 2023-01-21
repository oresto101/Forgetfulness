package com.example.forgetfulness.application.mapper;

import com.example.forgetfulness.api.DTO.request.GroupRequest;
import com.example.forgetfulness.application.entity.Group;
import org.mapstruct.Mapper;

@Mapper
public interface GroupMapper {
    Group groupRequestToGroup(GroupRequest groupRequest);
}
