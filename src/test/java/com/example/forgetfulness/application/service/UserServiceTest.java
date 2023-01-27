package com.example.forgetfulness.application.service;

import com.example.forgetfulness.application.entity.Reminder;
import com.example.forgetfulness.application.entity.User;
import com.example.forgetfulness.application.exception.ForgetfulnessException;
import com.example.forgetfulness.application.repository.UserRepository;
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
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;
    @Mock
    private ReminderService reminderService;

    @Test
    void shouldGetAllUsers() {
        // given
        List<User> expected = sampleUserList();
        given(userRepository.findAll()).willReturn(expected);
        // when
        List<User> actual = userService.getAllUsers();
        // then
        assertEquals(expected, actual);
    }

    @Test
    void shouldGetUserById() {
        // given
        Optional<User> expected = sampleOptionalUser();
        given(userRepository.findById(any())).willReturn(expected);
        // when
        Optional<User> actual = userService.getUserById(1L);
        // then
        assertEquals(expected, actual);
    }

    @Test
    void shouldGetUserByEmail() {
        // given
        Optional<User> expected = sampleOptionalUser();
        given(userRepository.findByEmail(any())).willReturn(expected);
        // when
        Optional<User> actual = userService.getUserByEmail(expected.get().getEmail());
        // then
        assertEquals(expected, actual);
    }

    @Test
    void shouldSave() {
        // given
        User expected = sampleUser();
        given(userRepository.save(any())).willReturn(expected);
        // when
        User request = sampleUser();
        request.setId(null);
        User actual = userService.save(request);
        // then
        assertEquals(expected, actual);
    }

    @Test
    void shouldThrowErrorWhenSaveWithExistingId() {
        // when-then
        User request = sampleUser();
        assertThrows(ForgetfulnessException.class, () -> userService.save(request));
    }

    @Test
    void shouldThrowErrorWhenSaveWithExistingEmail() {
        // given
        given(userRepository.findByEmail(any())).willReturn(sampleOptionalUser());
        // when-then
        User request = sampleUser();
        request.setId(null);
        assertThrows(ForgetfulnessException.class, () -> userService.save(request));
    }

    @Test
    void shouldDelete() {
        // given
        willDoNothing().given(userRepository).delete(any());
        given(userRepository.findById(any())).willReturn(sampleOptionalUser());
        // when-then
        try {
            userService.delete(1L);
        } catch (Exception e) {
            fail("Should not have thrown any exception");
        }
    }

    @Test
    void shouldAddReminder() {
        // given
        given(userRepository.findById(any())).willReturn(sampleOptionalUser());
        // when
        userService.addReminder(1L, sampleReminder(null));
        // then
        verify(reminderService).create(argThat(
                reminder -> reminder.getUser().equals(sampleUser())
        ));
    }

    @Test
    void shouldThrowErrorWhenAddReminderWithExistingReminderId() {
        // when-then
        assertThrows(ForgetfulnessException.class,
                () -> userService.addReminder(1L, sampleReminder(1L)));
    }

    @Test
    void shouldDeleteReminder() {
        // given
        given(reminderService.getReminderById(any())).willReturn(sampleOptionalReminder(1L));
        willDoNothing().given(reminderService).delete(any());
        // when-then
        try {
            userService.deleteReminder(1L, 1L);
        } catch (Exception e) {
            fail("Should not have thrown any exception");
        }
    }

    @Test
    void shouldThrowErrorWhenDeleteReminderWithoutReminderId() {
        // when-then
        assertThrows(ForgetfulnessException.class,
                () -> userService.deleteReminder(1L, null));
    }

    @Test
    void shouldThrowErrorWhenDeleteReminderWithNotExistingReminder() {
        // given
        given(reminderService.getReminderById(any())).willReturn(Optional.empty());
        // when-then
        assertThrows(ForgetfulnessException.class,
                () -> userService.deleteReminder(1L, 1L));
    }

    @Test
    void shouldGetUserReminders() {
        // given
        List<Reminder> expected = sampleReminderList();
        Optional<User> response = sampleOptionalUser();
        response.get().setReminders(sampleReminderList());
        given(userRepository.findById(any())).willReturn(response);
        // when
        List<Reminder> actual = userService.getUserReminders(1L);
        // then
        assertEquals(expected, actual);
    }

    @Test
    void shouldThrowErrorWhenGetUserRemindersWithoutUserId() {
        // when-then
        assertThrows(ForgetfulnessException.class,
                () -> userService.getUserReminders(null));
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

    protected Reminder sampleReminder(Long reminderId) {
        return new Reminder(
                reminderId,
                "Test reminder",
                "Detailed description",
                LocalDate.of(2023, 1, 1),
                null,
                null,
                null
        );
    }

    protected Optional<Reminder> sampleOptionalReminder(Long reminderId) {
        return Optional.of(sampleReminder(reminderId));
    }

    protected List<Reminder> sampleReminderList() {
        return List.of(sampleReminder(1L));
    }
}