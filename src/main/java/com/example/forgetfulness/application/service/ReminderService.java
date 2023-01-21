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

    public Reminder save(Reminder reminder) {
        if (reminder.isIdNull()) {
            Optional<Recurrence> recurrence = recurrenceRepository.getTopByPeriod(reminder.getRecurrence().getPeriod());
            if (recurrence.isEmpty()) {
                recurrence = Optional.of(recurrenceRepository.save(reminder.getRecurrence()));
            }
            reminder.setRecurrence(recurrence.get());
            return reminderRepository.save(reminder);
        }

        throw new ForgetfulnessException(ForgetfulnessExceptionType.ID_PROBLEM);
    }

    public Reminder update(Reminder reminder) {
        if (reminder.isIdNotNull()) {
            Optional<Recurrence> recurrence = recurrenceRepository.getTopByPeriod(reminder.getRecurrence().getPeriod());
            if (recurrence.isEmpty()) {
                recurrence = Optional.of(recurrenceRepository.save(reminder.getRecurrence()));
            }
            reminder.setRecurrence(recurrence.get());
            return reminderRepository.save(reminder);
        }

        throw new ForgetfulnessException(ForgetfulnessExceptionType.ID_PROBLEM);
    }

    public void delete(Long id) {
        Optional<Reminder> reminder = getReminderById(id);

        if (reminder.isEmpty()) {
            throw new ForgetfulnessException(ForgetfulnessExceptionType.ID_PROBLEM);
        }

        reminderRepository.delete(reminder.get());

        List<Reminder> remindersWithRecurrence = reminderRepository.findAllByRecurrence(reminder.get().getRecurrence());
        if (remindersWithRecurrence.isEmpty()) {
            recurrenceRepository.delete(reminder.get().getRecurrence());
        }
    }
}
