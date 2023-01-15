package com.example.forgetfulness.api.group;

import com.example.forgetfulness.application.group.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/group")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;
    @PostMapping("/create")
    public ResponseEntity<String> createGroup(@RequestParam("members") final List<String> users,
                                              final CreateGroupRequest request){
        groupService.createGroup(users, request);
        return ResponseEntity.accepted().body("Group created");

    }
}
