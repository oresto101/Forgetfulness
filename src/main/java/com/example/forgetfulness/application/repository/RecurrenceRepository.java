package com.example.forgetfulness.application.repository;

import com.example.forgetfulness.application.entity.Recurrence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecurrenceRepository extends JpaRepository<Recurrence, Long> {
    Optional<Recurrence> getTopByPeriod(Long period);
}
