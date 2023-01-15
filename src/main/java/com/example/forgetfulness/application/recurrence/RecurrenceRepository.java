package com.example.forgetfulness.application.recurrence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecurrenceRepository extends JpaRepository<Recurrence, Long> {

}
