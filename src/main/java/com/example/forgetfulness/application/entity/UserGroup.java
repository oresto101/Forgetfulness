package com.example.forgetfulness.application.entity;

import com.example.forgetfulness.application.entity.compositeKey.UserGroupCompositeKey;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class UserGroup {
    @EmbeddedId
    private UserGroupCompositeKey compositeId;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "group_id", nullable = false, insertable = false, updatable = false)
    private Group group;

    public boolean isIdNull() {
        return user == null || user.isIdNull() ||
                group == null || group.isIdNull() ||
                compositeId.isIdNull();
    }

    public UserGroup(Long userId, Long groupId) {
        this.compositeId = new UserGroupCompositeKey(userId, groupId);
        this.user = new User();
        this.user.setId(userId);
        this.group = new Group();
        this.group.setId(groupId);
    }
}
