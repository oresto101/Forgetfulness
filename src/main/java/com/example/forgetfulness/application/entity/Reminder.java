package com.example.forgetfulness.application.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Table(name = "reminder_table")
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Reminder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @ManyToOne
    private User user;

    @ManyToOne
    private Group group;

    @ManyToOne
    private Recurrence recurrence;

    public boolean isIdNull() {
        return id == null;
    }

    public boolean isIdNotNull() {
        return !isIdNull();
    }
}
