package com.example.forgetfulness.application.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    public Boolean findByEmailExists(final String email);
    public Boolean findByUsernameExists(final String username);
    public UserEntity getByUsername(final String username);
}
