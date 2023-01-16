package com.example.forgetfulness.application.service;

import com.example.forgetfulness.api.DTO.request.CreateGroupRequest;
import com.example.forgetfulness.application.entity.Group;
import com.example.forgetfulness.application.entity.User;
import com.example.forgetfulness.application.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;
    private final UserService userService;

    public void createGroup(final List<String> usernames, final CreateGroupRequest request){
        final List<User> users = userService.getUsersByUsernames(usernames);
        final Group group = Group.builder()
                .name(request.getName())
                .description(request.getDescription())
                .users(users)
                .build();
        groupRepository.save(group);
    }
}
