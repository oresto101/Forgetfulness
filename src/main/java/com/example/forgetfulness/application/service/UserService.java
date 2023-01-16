package com.example.forgetfulness.application.service;

import com.example.forgetfulness.api.DTO.request.CreateUserRequest;
import com.example.forgetfulness.application.entity.User;
import com.example.forgetfulness.application.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ConversionService conversionService;

    public void addUser(final CreateUserRequest createUserRequest) {
        if (userRepository.findByEmailExists(createUserRequest.getEmail()))
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "Student found");
        final User user = conversionService.convert(createUserRequest, User.class);
        userRepository.save(user);
    }

    public List<User> getUsersByUsernames(final List<String> usernames){
        return usernames.stream()
                .filter(userRepository::findByUsernameExists)
                .map(userRepository::getByUsername)
                .collect(Collectors.toList());
    }
}
