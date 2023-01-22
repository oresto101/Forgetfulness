package com.example.forgetfulness.application.service;

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
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new ForgetfulnessException(ForgetfulnessExceptionType.NO_USER);
        }

        userRepository.deleteById(id);
    }
}
