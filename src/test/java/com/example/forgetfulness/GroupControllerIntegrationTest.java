package com.example.forgetfulness;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.forgetfulness.api.DTO.request.GroupRequest;
import com.example.forgetfulness.api.DTO.response.GroupResponse;
import com.example.forgetfulness.api.controller.GroupController;
import com.example.forgetfulness.application.entity.Group;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
        Group group1 = new Group(1L, "group1");
        Group group2 = new Group(2L, "group2");
        List<Group> groups = Arrays.asList(group1, group2);
        GroupResponse groupResponse1 = new GroupResponse(1L, "group1");
        GroupResponse groupResponse2 = new GroupResponse(2L, "group2");
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
        Group group = new Group(1L, "group1");
        GroupResponse groupResponse = new GroupResponse(1L, "group1");

        when(groupService.getGroupById(1L)).thenReturn(Optional.of(group));
        when(groupMapper.groupToGroupResponse(group)).thenReturn(groupResponse);

        mockMvc.perform(get("/group/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("group1"));

        mockMvc.perform(get("/group/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateGroup() throws Exception {
        GroupRequest groupRequest = new GroupRequest("group1");
        Group group = new Group(1L, "group1");
        GroupResponse groupResponse = new GroupResponse(1L, "group1");

        when(groupMapper.groupRequestToGroup(groupRequest)).thenReturn(group);
        when(groupService.save(group)).thenReturn(group);
        when(groupMapper.groupToGroupResponse(group)).thenReturn(groupResponse);

        mockMvc.perform(post("/group")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(groupRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("group1"));
    }

    @Test
    public void testUpdateGroup() throws Exception {
        GroupRequest groupRequest = new GroupRequest("group1");
        Group group = new Group(1L, "group1");
        GroupResponse groupResponse = new GroupResponse(1L, "group1");

        when(groupMapper.groupRequestToGroup(groupRequest)).thenReturn(group);
        when(groupService.update(group)).thenReturn(group);
        when(groupMapper.groupToGroupResponse(group)).thenReturn(groupResponse);

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