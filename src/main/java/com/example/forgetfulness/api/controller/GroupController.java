package com.example.forgetfulness.api.controller;

import com.example.forgetfulness.api.DTO.request.GroupRequest;
import com.example.forgetfulness.api.DTO.response.GroupResponse;
import com.example.forgetfulness.application.entity.Group;
import com.example.forgetfulness.application.mapper.GroupMapper;
import com.example.forgetfulness.application.service.GroupService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/group")
@AllArgsConstructor
public class GroupController {
    private final GroupService groupService;
    private final GroupMapper groupMapper;

    @GetMapping
    public ResponseEntity<List<GroupResponse>> getAllGroups() {
        List<GroupResponse> list = groupService
                .getAllGroups()
                .stream()
                .map(groupMapper::groupToGroupResponse)
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupResponse> getGroupById(@PathVariable("id") Long id) {
        Optional<Group> groupById = groupService.getGroupById(id);

        return groupById
                .map(group ->
                        ResponseEntity
                                .status(HttpStatus.OK)
                                .body(groupMapper.groupToGroupResponse(group)))
                .orElseGet(() -> ResponseEntity
                        .notFound()
                        .build());
    }

    @PostMapping
    public ResponseEntity<String> createGroup(@RequestBody GroupRequest groupRequest) {
        groupService.save(groupMapper.groupRequestToGroup(groupRequest));

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PutMapping
    public ResponseEntity<String> updateGroup(@RequestBody GroupRequest groupRequest) {
        groupService.update(groupMapper.groupRequestToGroup(groupRequest));

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGroup(@PathVariable("id") Long id) {
        groupService.delete(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}
