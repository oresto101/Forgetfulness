package com.example.forgetfulness.application.recurrence;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Builder
public class Recurrence{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}