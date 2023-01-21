package com.example.forgetfulness.application.service;

import com.example.forgetfulness.application.entity.Reminder;
import com.example.forgetfulness.application.exception.ForgetfulnessException;
import com.example.forgetfulness.application.exception.ForgetfulnessExceptionType;
import com.example.forgetfulness.application.repository.ReminderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReminderService {
    private final ReminderRepository reminderRepository;

    public List<Reminder> getAllReminders() {
        return reminderRepository.findAll();
    }

    public Optional<Reminder> getReminderById(Long id) {
        return reminderRepository.findById(id);
    }

    public void save(Reminder Reminder) {
        if (Reminder.isIdNull()) {
            reminderRepository.save(Reminder);
        }

        throw new ForgetfulnessException(ForgetfulnessExceptionType.ID_PROBLEM);
    }

    public void update(Reminder Reminder) {
        if (Reminder.isIdNotNull()) {
            reminderRepository.save(Reminder);
        }

        throw new ForgetfulnessException(ForgetfulnessExceptionType.ID_PROBLEM);
    }

    public void delete(Long id) {
        if (id == null) {
            throw new ForgetfulnessException(ForgetfulnessExceptionType.ID_PROBLEM);
        }

        reminderRepository.deleteById(id);
    }
}
