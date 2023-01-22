package com.example.forgetfulness.application.repository;

import com.example.forgetfulness.application.entity.UserGroup;
import com.example.forgetfulness.application.entity.compositeKey.UserGroupCompositeKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, UserGroupCompositeKey> {
    List<UserGroup> findAllByUserId(Long userId);

    List<UserGroup> findAllByGroupId(Long groupId);
}
