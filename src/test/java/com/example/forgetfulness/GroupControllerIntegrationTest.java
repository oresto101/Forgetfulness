package com.example.forgetfulness;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.forgetfulness.api.DTO.request.GroupRequest;
import com.example.forgetfulness.api.DTO.response.GroupResponse;
import com.example.forgetfulness.api.controller.GroupController;
import com.example.forgetfulness.application.entity.Group;
import com.example.forgetfulness.application.entity.Reminder;
import com.example.forgetfulness.application.entity.UserGroup;
import com.example.forgetfulness.application.service.GroupService;
import com.example.forgetfulness.application.mapper.GroupMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(GroupController.class)
public class GroupControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GroupService groupService;

    @MockBean
    private GroupMapper groupMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testGetAllGroups() throws Exception {
        Set<Reminder> reminders1 = new HashSet<>();
        Set<UserGroup> groups1 = new HashSet<>();
        Group group1 = new Group(1L, "group1", "description1", groups1, reminders1);
        Set<Reminder> reminders2 = new HashSet<>();
        Set<UserGroup> groups2 = new HashSet<>();
        Group group2 = new Group(2L, "group2", "description2", groups2, reminders2);
        List<Group> groups = Arrays.asList(group1, group2);
        GroupResponse groupResponse1 = new GroupResponse(1L, "group1", "groupResponseDescription1");
        GroupResponse groupResponse2 = new GroupResponse(2L, "group2", "groupResponseDescription2");
        List<GroupResponse> groupResponses = Arrays.asList(groupResponse1, groupResponse2);

        when(groupService.getAllGroups()).thenReturn(groups);
        when(groupMapper.groupToGroupResponse(group1)).thenReturn(groupResponse1);
        when(groupMapper.groupToGroupResponse(group2)).thenReturn(groupResponse2);

        mockMvc.perform(get("/group"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("group1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("group2"));
    }
    @Test
    public void testGetGroupById() throws Exception {
        Set<Reminder> reminders1 = new HashSet<>();
        Set<UserGroup> groups1 = new HashSet<>();
        Group group1 = new Group(1L, "group1", "description1", groups1, reminders1);
        GroupResponse groupResponse = new GroupResponse(1L, "group1", "groupResponseDescription1");

        when(groupService.getGroupById(1L)).thenReturn(Optional.of(group1));
        when(groupMapper.groupToGroupResponse(group1)).thenReturn(groupResponse);

        mockMvc.perform(get("/group/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("group1"));

        mockMvc.perform(get("/group/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateGroup() throws Exception {
        GroupRequest groupRequest = new GroupRequest(1L,"group1", "groupRequestDescription1");
        Set<Reminder> reminders1 = new HashSet<>();
        Set<UserGroup> groups1 = new HashSet<>();
        Group group1 = new Group(1L, "group1", "description1", groups1, reminders1);;
        GroupResponse groupResponse = new GroupResponse(1L, "group1", "groupResponseDescription1");

        when(groupMapper.groupRequestToGroup(groupRequest)).thenReturn(group1);
        when(groupService.save(group1)).thenReturn(group1);
        when(groupMapper.groupToGroupResponse(group1)).thenReturn(groupResponse);

        mockMvc.perform(post("/group")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(groupRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("group1"));
    }

    @Test
    public void testUpdateGroup() throws Exception {
        GroupRequest groupRequest = new GroupRequest(1L,"group1", "groupRequestDescription1");
        Set<Reminder> reminders1 = new HashSet<>();
        Set<UserGroup> groups1 = new HashSet<>();
        Group group1 = new Group(1L, "group1", "description1", groups1, reminders1);
        GroupResponse groupResponse = new GroupResponse(1L, "group1", "groupResponseDescription1");

        when(groupMapper.groupRequestToGroup(groupRequest)).thenReturn(group1);
        when(groupService.update(group1)).thenReturn(group1);
        when(groupMapper.groupToGroupResponse(group1)).thenReturn(groupResponse);

        mockMvc.perform(put("/group")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(groupRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("group1"));
    }

    @Test
    public void testDeleteGroup() throws Exception {
        mockMvc.perform(delete("/group/1"))
                .andExpect(status().isOk());
        verify(groupService, times(1)).delete(1L);
    }

}