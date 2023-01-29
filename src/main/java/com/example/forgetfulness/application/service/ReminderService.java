package com.example.forgetfulness.application.service;

import com.example.forgetfulness.application.entity.Recurrence;
import com.example.forgetfulness.application.entity.Reminder;
import com.example.forgetfulness.application.exception.ForgetfulnessException;
import com.example.forgetfulness.application.exception.ForgetfulnessExceptionType;
import com.example.forgetfulness.application.repository.RecurrenceRepository;
import com.example.forgetfulness.application.repository.ReminderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReminderService {
    private final ReminderRepository reminderRepository;
    private final RecurrenceRepository recurrenceRepository;

    public List<Reminder> getAllReminders() {
        return reminderRepository.findAll();
    }

    public Optional<Reminder> getReminderById(Long id) {
        return reminderRepository.findById(id);
    }

    public Reminder create(Reminder reminder) {
        if (reminder.isIdNotNull()) {
            throw new ForgetfulnessException(ForgetfulnessExceptionType.REMINDER_ALREADY_EXISTS);
        }

        reminder.setRecurrence(getOrCreateNewRecurrence(reminder.getRecurrence()));

        return reminderRepository.save(reminder);
    }

    public Reminder update(Reminder reminderRequest) {
        if (reminderRequest.isIdNull()) {
            throw new ForgetfulnessException(ForgetfulnessExceptionType.ID_PROBLEM);
        }

        Optional<Reminder> reminder = getReminderById(reminderRequest.getId());
        if (reminder.isEmpty()) {
            throw new ForgetfulnessException(ForgetfulnessExceptionType.NO_REMINDER);
        }
        Recurrence oldRecurrence = reminder.get().getRecurrence();

        reminderRequest.setRecurrence(getOrCreateNewRecurrence(reminderRequest.getRecurrence()));

        Reminder updatedReminder = reminderRepository.save(reminderRequest);

        deleteNotUsableRecurrence(oldRecurrence);

        return updatedReminder;
    }

    public void delete(Long id) {
        Optional<Reminder> reminder = getReminderById(id);
        if (reminder.isEmpty()) {
            throw new ForgetfulnessException(ForgetfulnessExceptionType.NO_REMINDER);
        }

        reminderRepository.delete(reminder.get());

        deleteNotUsableRecurrence(reminder.get().getRecurrence());
    }

    private Recurrence getOrCreateNewRecurrence(Recurrence recurrence) {
        Optional<Recurrence> recurrenceExisting = recurrenceRepository.getTopByPeriodAndTime(recurrence.getPeriod(), recurrence.getTime());
        if (recurrenceExisting.isEmpty()) {
            return recurrenceRepository.save(new Recurrence(null, recurrence.getPeriod(), recurrence.getTime()));
        }
        return recurrenceExisting.get();
    }

    private void deleteNotUsableRecurrence(Recurrence recurrence) {
        List<Reminder> remindersWithRecurrence = reminderRepository.findAllByRecurrence(recurrence);
        if (remindersWithRecurrence.isEmpty()) {
            recurrenceRepository.delete(recurrence);
        }
    }
}
