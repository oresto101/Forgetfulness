package com.example.forgetfulness.application.service;

import com.example.forgetfulness.application.entity.Recurrence;
import com.example.forgetfulness.application.exception.ForgetfulnessException;
import com.example.forgetfulness.application.exception.ForgetfulnessExceptionType;
import com.example.forgetfulness.application.repository.RecurrenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecurrenceService {
    private final RecurrenceRepository recurrenceRepository;

    public List<Recurrence> getAllRecurrences() {
        return recurrenceRepository.findAll();
    }

    public Optional<Recurrence> getRecurrenceById(Long id) {
        return recurrenceRepository.findById(id);
    }

    public void save(Recurrence Recurrence) {
        if (Recurrence.isIdNull()) {
            recurrenceRepository.save(Recurrence);
        }

        throw new ForgetfulnessException(ForgetfulnessExceptionType.ID_PROBLEM);
    }

    public void update(Recurrence Recurrence) {
        if (Recurrence.isIdNotNull()) {
            recurrenceRepository.save(Recurrence);
        }

        throw new ForgetfulnessException(ForgetfulnessExceptionType.ID_PROBLEM);
    }

    public void delete(Long id) {
        if (id == null) {
            throw new ForgetfulnessException(ForgetfulnessExceptionType.ID_PROBLEM);
        }

        recurrenceRepository.deleteById(id);
    }
}
