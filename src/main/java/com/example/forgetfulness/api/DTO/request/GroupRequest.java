package com.example.forgetfulness.api.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupRequest {
    private Long id;
    private String name;
    private String description;
}
