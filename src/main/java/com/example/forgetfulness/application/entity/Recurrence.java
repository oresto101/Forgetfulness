package com.example.forgetfulness.application.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "recurrence_table")
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Recurrence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "period")
    private Long period;

    @OneToOne
    @JoinColumn(name = "id")
    private Reminder reminder;

    public boolean isIdNull() {
        return id == null;
    }

    public boolean isIdNotNull() {
        return !isIdNull();
    }
}