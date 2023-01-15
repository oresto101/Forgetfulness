package com.example.forgetfulness.application.user;

import com.example.forgetfulness.api.user.CreateUserRequest;
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
        final UserEntity userEntity = conversionService.convert(createUserRequest, UserEntity.class);
        userRepository.save(userEntity);
    }

    public List<UserEntity> getUsersByUsernames(final List<String> usernames){
        return usernames.stream()
                .filter(userRepository::findByUsernameExists)
                .map(userRepository::getByUsername)
                .collect(Collectors.toList());
    }
}
