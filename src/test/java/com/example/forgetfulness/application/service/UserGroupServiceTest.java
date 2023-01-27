package com.example.forgetfulness.application.service;

import com.example.forgetfulness.application.entity.Group;
import com.example.forgetfulness.application.entity.User;
import com.example.forgetfulness.application.entity.UserGroup;
import com.example.forgetfulness.application.exception.ForgetfulnessException;
import com.example.forgetfulness.application.repository.GroupRepository;
import com.example.forgetfulness.application.repository.UserGroupRepository;
import com.example.forgetfulness.application.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserGroupServiceTest {
    @Mock
    private UserGroupRepository userGroupRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private GroupRepository groupRepository;
    @InjectMocks
    private UserGroupService userGroupService;

    @Test
    void shouldSave() {
        // given
        given(userGroupRepository.findById(any())).willReturn(Optional.empty());
        given(userRepository.findById(any())).willReturn(sampleOptionalUser());
        given(groupRepository.findById(any())).willReturn(sampleOptionalGroup());
        given(userGroupRepository.save(any())).willReturn(sampleUserGroup());
        // when-then
        try {
            userGroupService.save(1L, 1L);
        } catch (Exception e) {
            fail("Should not have thrown any exception");
        }
    }

    @Test
    void shouldThrowErrorWhenSaveWithoutUserId() {
        // when-then
        assertThrows(ForgetfulnessException.class, () -> userGroupService.save(null, 1L));
    }

    @Test
    void shouldThrowErrorWhenSaveWithoutGroupId() {
        // when-then
        assertThrows(ForgetfulnessException.class, () -> userGroupService.save(1L, null));
    }

    @Test
    void shouldThrowErrorWhenSaveWithExistingUserGroup() {
        // given
        given(userGroupRepository.findById(any())).willReturn(sampleOptionalUserGroup());
        // when-then
        assertThrows(ForgetfulnessException.class, () -> userGroupService.save(1L, 1L));
    }

    @Test
    void shouldThrowErrorWhenSaveWithNonExistingUser() {
        // given
        given(userGroupRepository.findById(any())).willReturn(Optional.empty());
        given(userRepository.findById(any())).willReturn(Optional.empty());
        // when-then
        assertThrows(ForgetfulnessException.class, () -> userGroupService.save(1L, 1L));
    }

    @Test
    void shouldThrowErrorWhenSaveWithNonExistingGroup() {
        // given
        given(userGroupRepository.findById(any())).willReturn(Optional.empty());
        given(userRepository.findById(any())).willReturn(sampleOptionalUser());
        given(groupRepository.findById(any())).willReturn(Optional.empty());
        // when-then
        assertThrows(ForgetfulnessException.class, () -> userGroupService.save(1L, 1L));
    }

    @Test
    void shouldDelete() {
        // given
        given(userGroupRepository.findById(any())).willReturn(sampleOptionalUserGroup());
        // when-then
        try {
            userGroupService.delete(1L, 1L);
        } catch (Exception e) {
            fail("Should not have thrown any exception");
        }
    }

    @Test
    void shouldThrowErrorWhenDeleteWithNonExistingUserGroup() {
        // given
        given(userGroupRepository.findById(any())).willReturn(Optional.empty());
        // when-then
        assertThrows(ForgetfulnessException.class, () -> userGroupService.delete(1L, 1L));
    }

    @Test
    void shouldGetUserGroups() {
        // given
        List<Group> expected = sampleGroupList();
        given(userGroupRepository.findAllByUserId(any())).willReturn(sampleUserGroupList());
        given(groupRepository.findById(any())).willReturn(sampleOptionalGroup());
        // when
        List<Group> actual = userGroupService.getUserGroups(1L);
        // then
        assertEquals(expected, actual);
    }

    @Test
    void shouldThrowErrorWhenGetUserGroupsWithNonExistingUserId() {
        // when-then
        assertThrows(ForgetfulnessException.class, () -> userGroupService.getUserGroups(null));
    }

    @Test
    void shouldGetGroupUsers() {
        // given
        List<User> expected = sampleUserList();
        given(userGroupRepository.findAllByGroupId(any())).willReturn(sampleUserGroupList());
        given(userRepository.findById(any())).willReturn(sampleOptionalUser());
        // when
        List<User> actual = userGroupService.getGroupUsers(1L);
        // then
        assertEquals(expected, actual);
    }

    @Test
    void shouldThrowErrorWhenGetGroupUsersWithNonExistingUserId() {
        // when-then
        assertThrows(ForgetfulnessException.class, () -> userGroupService.getGroupUsers(null));
    }


    protected UserGroup sampleUserGroup() {
        return new UserGroup(1L, 1L);
    }

    protected Optional<UserGroup> sampleOptionalUserGroup() {
        return Optional.of(sampleUserGroup());
    }

    protected List<UserGroup> sampleUserGroupList() {
        return List.of(sampleUserGroup());
    }

    protected User sampleUser() {
        return new User(
                1L,
                "pass",
                "John",
                "Smith",
                "john.smith@mail.com",
                Set.of(),
                List.of()
        );
    }

    protected Optional<User> sampleOptionalUser() {
        return Optional.of(sampleUser());
    }

    protected List<User> sampleUserList() {
        return List.of(sampleUser());
    }

    protected Group sampleGroup() {
        return new Group(
                1L,
                "group",
                "Detailed group description",
                Set.of(),
                List.of()
        );
    }

    protected Optional<Group> sampleOptionalGroup() {
        return Optional.of(sampleGroup());
    }

    protected List<Group> sampleGroupList() {
        return List.of(sampleGroup());
    }
}