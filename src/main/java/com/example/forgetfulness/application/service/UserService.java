package com.example.forgetfulness.application.service;

import com.example.forgetfulness.application.entity.Reminder;
import com.example.forgetfulness.application.entity.User;
import com.example.forgetfulness.application.exception.ForgetfulnessException;
import com.example.forgetfulness.application.exception.ForgetfulnessExceptionType;
import com.example.forgetfulness.application.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ReminderService reminderService;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User save(User userRequest) {
        if (userRequest.isIdNotNull()) {
            throw new ForgetfulnessException(ForgetfulnessExceptionType.USER_ALREADY_EXISTS);
        }

        Optional<User> userWithSameEmail = userRepository.findByEmail(userRequest.getEmail());
        if (userWithSameEmail.isPresent()) {
            throw new ForgetfulnessException(ForgetfulnessExceptionType.USER_EMAIL_ALREADY_EXISTS);
        }

        return userRepository.save(userRequest);
    }

    public void delete(Long id) {
        userRepository.delete(getValidatedUser(id));
    }

    public Reminder addReminder(Long userId, Reminder reminderRequest) {
        if (userId == null || reminderRequest.isIdNotNull()) {
            throw new ForgetfulnessException(ForgetfulnessExceptionType.ID_PROBLEM);
        }

        reminderRequest.setUser(getValidatedUser(userId));

        return reminderService.create(reminderRequest);
    }

    public void deleteReminder(Long userId, Long reminderId) {
        if (userId == null || reminderId == null) {
            throw new ForgetfulnessException(ForgetfulnessExceptionType.ID_PROBLEM);
        }

        Optional<Reminder> reminder = reminderService.getReminderById(reminderId);
        if (reminder.isEmpty()) {
            throw new ForgetfulnessException(ForgetfulnessExceptionType.NO_REMINDER);
        }

        reminderService.delete(reminder.get().getId());
    }

    public List<Reminder> getUserReminders(Long userId) {
        if (userId == null) {
            throw new ForgetfulnessException(ForgetfulnessExceptionType.ID_PROBLEM);
        }
        User user = getValidatedUser(userId);

        return new ArrayList<>(user.getReminders());
    }


    private User getValidatedUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new ForgetfulnessException(ForgetfulnessExceptionType.NO_USER);
        }
        return user.get();
    }
}
