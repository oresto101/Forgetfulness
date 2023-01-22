package com.example.forgetfulness.application.service;

import com.example.forgetfulness.application.entity.Group;
import com.example.forgetfulness.application.entity.User;
import com.example.forgetfulness.application.entity.UserGroup;
import com.example.forgetfulness.application.entity.compositeKey.UserGroupCompositeKey;
import com.example.forgetfulness.application.exception.ForgetfulnessException;
import com.example.forgetfulness.application.exception.ForgetfulnessExceptionType;
import com.example.forgetfulness.application.repository.GroupRepository;
import com.example.forgetfulness.application.repository.UserGroupRepository;
import com.example.forgetfulness.application.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserGroupService {
    private final UserGroupRepository userGroupRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    public void save(Long userId, Long groupId) {
        if (userId == null || groupId == null) {
            throw new ForgetfulnessException(ForgetfulnessExceptionType.ID_PROBLEM);
        }

        UserGroup userGroupRequest = new UserGroup(userId, groupId);

        Optional<UserGroup> userGroup = userGroupRepository.findById(userGroupRequest.getCompositeId());
        if (userGroup.isPresent()) {
            throw new ForgetfulnessException(ForgetfulnessExceptionType.USER_IN_GROUP);
        }

        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new ForgetfulnessException(ForgetfulnessExceptionType.NO_USER);
        }

        Optional<Group> group = groupRepository.findById(groupId);
        if (group.isEmpty()) {
            throw new ForgetfulnessException(ForgetfulnessExceptionType.NO_GROUP);
        }

        userGroupRepository.save(userGroupRequest);
    }

    public void delete(Long userId, Long groupId) {
        Optional<UserGroup> userGroup = userGroupRepository.findById(new UserGroupCompositeKey(userId, groupId));
        if (userGroup.isEmpty()) {
            throw new ForgetfulnessException(ForgetfulnessExceptionType.USER_NOT_IN_GROUP);
        }

        userGroupRepository.delete(userGroup.get());
    }

    public List<Group> getUserGroups(Long userId) {
        if (userId == null) {
            throw new ForgetfulnessException(ForgetfulnessExceptionType.ID_PROBLEM);
        }

        return userGroupRepository
                .findAllByUserId(userId)
                .stream()
                .map(userGroup -> groupRepository
                        .findById(userGroup
                                .getGroup()
                                .getId())
                        .orElseThrow())
                .collect(Collectors.toList());
    }
}
