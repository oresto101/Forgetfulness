package com.example.forgetfulness.application.group;

import com.example.forgetfulness.api.group.CreateGroupRequest;
import com.example.forgetfulness.application.user.UserEntity;
import com.example.forgetfulness.application.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;
    private final UserService userService;

    public void createGroup(final List<String> usernames, final CreateGroupRequest request){
        final List<UserEntity> users = userService.getUsersByUsernames(usernames);
        final GroupEntity groupEntity = GroupEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .users(users)
                .build();
        groupRepository.save(groupEntity);
    }
}
