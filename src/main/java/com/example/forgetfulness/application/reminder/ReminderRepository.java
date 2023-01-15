package com.example.forgetfulness.application.reminder;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReminderRepository extends JpaRepository<ReminderEntity, Long> {
}
