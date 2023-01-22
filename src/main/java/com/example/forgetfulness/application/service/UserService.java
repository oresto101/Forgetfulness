package com.example.forgetfulness.application.service;

import com.example.forgetfulness.application.entity.Reminder;
import com.example.forgetfulness.application.entity.User;
import com.example.forgetfulness.application.exception.ForgetfulnessException;
import com.example.forgetfulness.application.exception.ForgetfulnessExceptionType;
import com.example.forgetfulness.application.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

        User user = getValidatedUser(userId);
        Reminder reminder = reminderService.create(reminderRequest);

        user.getReminders().add(reminder);
        userRepository.save(user);

        return reminder;
    }

    public void deleteReminder(Long userId, Long reminderId) {
        if (userId == null || reminderId == null) {
            throw new ForgetfulnessException(ForgetfulnessExceptionType.ID_PROBLEM);
        }

        User user = getValidatedUser(userId);
        Optional<Reminder> reminder = reminderService.getReminderById(reminderId);
        if (reminder.isEmpty()) {
            throw new ForgetfulnessException(ForgetfulnessExceptionType.NO_REMINDER);
        }

        user.getReminders().remove(reminder.get());
        userRepository.save(user);

        reminderService.delete(reminder.get().getId());
    }


    private User getValidatedUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new ForgetfulnessException(ForgetfulnessExceptionType.NO_USER);
        }
        return user.get();
    }
}
