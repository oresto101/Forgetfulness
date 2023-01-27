package com.example.forgetfulness.application.service;

import com.example.forgetfulness.application.entity.Recurrence;
import com.example.forgetfulness.application.entity.Reminder;
import com.example.forgetfulness.application.exception.ForgetfulnessException;
import com.example.forgetfulness.application.repository.RecurrenceRepository;
import com.example.forgetfulness.application.repository.ReminderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith(MockitoExtension.class)
class ReminderServiceTest {
    @Mock
    private ReminderRepository reminderRepository;
    @Mock
    private RecurrenceRepository recurrenceRepository;
    @InjectMocks
    private ReminderService reminderService;

    @Test
    void shouldGetAllReminders() {
        // given
        List<Reminder> expected = sampleReminderList();
        given(reminderRepository.findAll()).willReturn(expected);
        // when
        List<Reminder> actual = reminderService.getAllReminders();
        // then
        assertEquals(expected, actual);
    }

    @Test
    void shouldGetReminderById() {
        // given
        Optional<Reminder> expected = sampleOptionalReminder();
        given(reminderRepository.findById(any())).willReturn(expected);
        // when
        Optional<Reminder> actual = reminderService.getReminderById(1L);
        // then
        assertEquals(expected, actual);
    }

    @Test
    void shouldCreate() {
        // given
        Reminder expected = sampleReminder();
        given(reminderRepository.save(any())).willReturn(expected);
        given(recurrenceRepository.getTopByPeriodAndTime(any(), any()))
                .willReturn(sampleOptionalRecurrence());
        // when
        Reminder request = sampleReminder();
        request.setId(null);
        Reminder actual = reminderService.create(request);
        // then
        assertEquals(expected, actual);
    }

    @Test
    void shouldThrowErrorWhenSaveWithExistingId() {
        // when-then
        Reminder request = sampleReminder();
        assertThrows(ForgetfulnessException.class, () -> reminderService.create(request));
    }

    @Test
    void shouldUpdate() {
        // given
        Reminder expected = sampleReminder();
        given(reminderRepository.findById(any())).willReturn(sampleOptionalReminder());
        given(recurrenceRepository.getTopByPeriodAndTime(any(), any()))
                .willReturn(sampleOptionalRecurrence());
        given(reminderRepository.save(any())).willReturn(expected);
        given(reminderRepository.findAllByRecurrence(sampleRecurrence()))
                .willReturn(sampleReminderList());
        // when
        Reminder request = sampleReminder();

        Reminder actual = reminderService.update(request);
        // then
        assertEquals(expected, actual);
    }

    @Test
    void shouldThrowErrorWhenUpdateWithNonExistingId() {
        // when-then
        Reminder request = sampleReminder();
        request.setId(null);
        assertThrows(ForgetfulnessException.class, () -> reminderService.update(request));
    }

    @Test
    void shouldThrowErrorWhenUpdateNonExistingReminder() {
        // given
        given(reminderRepository.findById(any())).willReturn(Optional.empty());
        // when-then
        Reminder request = sampleReminder();
        assertThrows(ForgetfulnessException.class, () -> reminderService.update(request));
    }

    @Test
    void shouldDelete() {
        // given
        willDoNothing().given(reminderRepository).delete(any());
        given(reminderRepository.findById(any())).willReturn(sampleOptionalReminder());
        // when-then
        try {
            reminderService.delete(1L);
        } catch (Exception e) {
            fail("Should not have thrown any exception");
        }
    }

    @Test
    void shouldThrowExceptionWhenDeleteNonExistingReminder() {
        // given
        given(reminderRepository.findById(any())).willReturn(Optional.empty());
        // when-then
        assertThrows(ForgetfulnessException.class, () -> reminderService.delete(1L));
    }


    protected Reminder sampleReminder() {
        return new Reminder(
                1L,
                "pass",
                "John",
                LocalDate.of(2023, 1, 1),
                null,
                null,
                sampleRecurrence()
        );
    }

    protected Optional<Reminder> sampleOptionalReminder() {
        return Optional.of(sampleReminder());
    }

    protected List<Reminder> sampleReminderList() {
        return List.of(sampleReminder());
    }

    protected Recurrence sampleRecurrence() {
        return new Recurrence(
                1L,
                60L,
                LocalTime.of(12, 30)
        );
    }

    protected Optional<Recurrence> sampleOptionalRecurrence() {
        return Optional.of(sampleRecurrence());
    }
}