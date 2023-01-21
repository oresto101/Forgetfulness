package com.example.forgetfulness.application.entity.compositeKey;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class UserGroupCompositeKey implements Serializable {
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "group_id")
    private Long groupId;
}
