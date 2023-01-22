package com.example.forgetfulness.application.service;

import com.example.forgetfulness.application.entity.Group;
import com.example.forgetfulness.application.entity.User;
import com.example.forgetfulness.application.entity.UserGroup;
import com.example.forgetfulness.application.exception.ForgetfulnessException;
import com.example.forgetfulness.application.exception.ForgetfulnessExceptionType;
import com.example.forgetfulness.application.repository.GroupRepository;
import com.example.forgetfulness.application.repository.UserGroupRepository;
import com.example.forgetfulness.application.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserGroupService {
    private final UserGroupRepository userGroupRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    public void save(UserGroup request) {
        if (request.isIdNull()) {
            throw new ForgetfulnessException(ForgetfulnessExceptionType.ID_PROBLEM);
        }

        Optional<UserGroup> userGroup = userGroupRepository.findById(request.getCompositeId());
        if (userGroup.isPresent()) {
            throw new ForgetfulnessException(ForgetfulnessExceptionType.USER_IN_GROUP);
        }

        Optional<User> user = userRepository.findById(request.getUser().getId());
        if (user.isEmpty()) {
            throw new ForgetfulnessException(ForgetfulnessExceptionType.NO_USER);
        }

        Optional<Group> group = groupRepository.findById(request.getGroup().getId());
        if (group.isEmpty()) {
            throw new ForgetfulnessException(ForgetfulnessExceptionType.NO_GROUP);
        }

        userGroupRepository.save(request);
    }

    public void delete(UserGroup request) {
        Optional<UserGroup> userGroup = userGroupRepository.findById(request.getCompositeId());
        if (userGroup.isEmpty()) {
            throw new ForgetfulnessException(ForgetfulnessExceptionType.USER_NOT_IN_GROUP);
        }

        userGroupRepository.delete(userGroup.get());
    }
}
