package com.example.forgetfulness.application.repository;

import com.example.forgetfulness.application.entity.UserGroup;
import com.example.forgetfulness.application.entity.compositeKey.UserGroupCompositeKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, UserGroupCompositeKey> {
}
