package com.example.forgetfulness.application.repository;

import com.example.forgetfulness.application.entity.Recurrence;
import com.example.forgetfulness.application.entity.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {
    List<Reminder> findAllByRecurrence(Recurrence recurrence);
}
