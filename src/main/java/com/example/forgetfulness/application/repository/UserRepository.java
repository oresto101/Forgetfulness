package com.example.forgetfulness.application.repository;

import com.example.forgetfulness.application.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Boolean findByEmailExists(final String email);

    Boolean findByUsernameExists(final String username);

    User getByUsername(final String username);
}
