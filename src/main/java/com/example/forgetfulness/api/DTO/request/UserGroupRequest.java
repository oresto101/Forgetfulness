package com.example.forgetfulness.api.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserGroupRequest {
    private Long userId;
    private Long groupId;
}
