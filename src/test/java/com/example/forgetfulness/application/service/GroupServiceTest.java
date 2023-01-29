package com.example.forgetfulness.application.service;

import com.example.forgetfulness.application.entity.Group;
import com.example.forgetfulness.application.entity.Reminder;
import com.example.forgetfulness.application.exception.ForgetfulnessException;
import com.example.forgetfulness.application.repository.GroupRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {
    @Mock
    private GroupRepository groupRepository;
    @InjectMocks
    private GroupService groupService;
    @Mock
    private ReminderService reminderService;

    @Test
    void shouldGetAllGroups() {
        // given
        List<Group> expected = sampleGroupList();
        given(groupRepository.findAll()).willReturn(expected);
        // when
        List<Group> actual = groupService.getAllGroups();
        // then
        assertEquals(expected, actual);
    }

    @Test
    void shouldGetGroupById() {
        // given
        Optional<Group> expected = sampleOptionalGroup();
        given(groupRepository.findById(any())).willReturn(expected);
        // when
        Optional<Group> actual = groupService.getGroupById(1L);
        // then
        assertEquals(expected, actual);
    }

    @Test
    void shouldSave() {
        // given
        Group expected = sampleGroup();
        given(groupRepository.save(any())).willReturn(expected);
        // when
        Group request = sampleGroup();
        request.setId(null);
        Group actual = groupService.save(request);
        // then
        assertEquals(expected, actual);
    }

    @Test
    void shouldThrowErrorWhenSaveWithExistingId() {
        // when-then
        Group request = sampleGroup();
        assertThrows(ForgetfulnessException.class, () -> groupService.save(request));
    }

    @Test
    void shouldDelete() {
        // given
        willDoNothing().given(groupRepository).delete(any());
        given(groupRepository.findById(any())).willReturn(sampleOptionalGroup());
        // when-then
        try {
            groupService.delete(1L);
        } catch (Exception e) {
            fail("Should not have thrown any exception");
        }
    }

    @Test
    void shouldAddReminder() {
        // given
        given(groupRepository.findById(any())).willReturn(sampleOptionalGroup());
        // when
        groupService.addReminder(1L, sampleReminder(null));
        // then
        verify(reminderService).create(argThat(
                reminder -> reminder.getGroup().equals(sampleGroup())
        ));
    }

    @Test
    void shouldThrowErrorWhenAddReminderWithExistingReminderId() {
        // when-then
        assertThrows(ForgetfulnessException.class,
                () -> groupService.addReminder(1L, sampleReminder(1L)));
    }

    @Test
    void shouldDeleteReminder() {
        // given
        given(reminderService.getReminderById(any())).willReturn(sampleOptionalReminder(1L));
        willDoNothing().given(reminderService).delete(any());
        // when-then
        try {
            groupService.deleteReminder(1L, 1L);
        } catch (Exception e) {
            fail("Should not have thrown any exception");
        }
    }

    @Test
    void shouldThrowErrorWhenDeleteReminderWithoutReminderId() {
        // when-then
        assertThrows(ForgetfulnessException.class,
                () -> groupService.deleteReminder(1L, null));
    }

    @Test
    void shouldThrowErrorWhenDeleteReminderWithNotExistingReminder() {
        // given
        given(reminderService.getReminderById(any())).willReturn(Optional.empty());
        // when-then
        assertThrows(ForgetfulnessException.class,
                () -> groupService.deleteReminder(1L, 1L));
    }

    @Test
    void shouldGetGroupReminders() {
        // given
        List<Reminder> expected = sampleReminderList();
        Optional<Group> response = sampleOptionalGroup();
        response.get().setReminders(sampleReminderList());
        given(groupRepository.findById(any())).willReturn(response);
        // when
        List<Reminder> actual = groupService.getGroupReminders(1L);
        // then
        assertEquals(expected, actual);
    }

    @Test
    void shouldThrowErrorWhenGetGroupRemindersWithoutGroupId() {
        // when-then
        assertThrows(ForgetfulnessException.class,
                () -> groupService.getGroupReminders(null));
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

    protected Reminder sampleReminder(Long groupId) {
        return new Reminder(
                groupId,
                "Test reminder",
                "Detailed description",
                LocalDate.of(2023, 1, 1),
                null,
                null,
                null
        );
    }

    protected Optional<Reminder> sampleOptionalReminder(Long groupId) {
        return Optional.of(sampleReminder(groupId));
    }

    protected List<Reminder> sampleReminderList() {
        return List.of(sampleReminder(1L));
    }
}