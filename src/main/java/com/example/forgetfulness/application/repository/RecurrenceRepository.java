package com.example.forgetfulness.application.repository;

import com.example.forgetfulness.application.entity.Recurrence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecurrenceRepository extends JpaRepository<Recurrence, Long> {
}
