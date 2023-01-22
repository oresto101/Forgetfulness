package com.example.forgetfulness.application.mapper;

import com.example.forgetfulness.api.DTO.request.UserGroupRequest;
import com.example.forgetfulness.application.entity.UserGroup;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserGroupMapper {
    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "userId", target = "compositeId.userId")
    @Mapping(source = "groupId", target = "group.id")
    @Mapping(source = "groupId", target = "compositeId.groupId")
    UserGroup userGroupRequestToUserGroup(UserGroupRequest userGroupRequest);
}
