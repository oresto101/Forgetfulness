package com.example.forgetfulness.application.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Table(name = "group_table")
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @OneToMany(mappedBy = "group")
    private Set<UserGroup> members;

    @OneToMany(mappedBy = "group")
    private List<Reminder> reminders;

    public boolean isIdNull() {
        return id == null;
    }

    public boolean isIdNotNull() {
        return !isIdNull();
    }
}
