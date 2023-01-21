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

    public User save(User User) {
        if (User.isIdNull()) {
            return userRepository.save(User);
        }

        throw new ForgetfulnessException(ForgetfulnessExceptionType.ID_PROBLEM);
    }

    public User update(User User) {
        if (User.isIdNotNull()) {
            return userRepository.save(User);
        }

        throw new ForgetfulnessException(ForgetfulnessExceptionType.ID_PROBLEM);
    }

    public void delete(Long id) {
        if (id == null) {
            throw new ForgetfulnessException(ForgetfulnessExceptionType.ID_PROBLEM);
        }

        userRepository.deleteById(id);
    }
}
