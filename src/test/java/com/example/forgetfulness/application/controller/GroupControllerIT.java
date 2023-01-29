package com.example.forgetfulness.application.controller;
import static com.example.forgetfulness.application.controller.TestValues.*;
import static com.example.forgetfulness.application.controller.TestValues.LOCAL_TIME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.forgetfulness.api.DTO.request.GroupRequest;
import com.example.forgetfulness.api.DTO.request.ReminderRequest;
import com.example.forgetfulness.api.DTO.request.UserRequest;
import com.example.forgetfulness.api.DTO.response.GroupResponse;
import com.example.forgetfulness.application.entity.*;
import com.example.forgetfulness.application.repository.GroupRepository;
import com.example.forgetfulness.application.repository.ReminderRepository;
import com.example.forgetfulness.application.repository.UserGroupRepository;
import com.example.forgetfulness.application.repository.UserRepository;
import com.example.forgetfulness.application.service.GroupService;
import com.example.forgetfulness.application.mapper.GroupMapper;
import com.example.forgetfulness.application.service.UserGroupService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class GroupControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GroupService groupService;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserGroupRepository userGroupRepository;

    @Autowired
    private UserGroupService userGroupService;

    @Autowired
    private ReminderRepository reminderRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldReturnListOfGroupsGivenDBContainsAtLeastOneGroup() throws Exception {
        //given
        final Group group = getGroup();
        groupRepository.save(group);

        //when
        final MvcResult result = mockMvc.perform(get("/group"))
                .andExpect(status().isOk())
                .andReturn();

        //then
        assertEquals(result.getResponse().getContentAsString(), "[{\"id\":1,\"name\":\"group_name\",\"description\":\"group_description\"}]");
    }

    @Test
    public void shouldReturnEmptyListGivenDBContainsNoGroups() throws Exception {
        //given

        //when
        final MvcResult result = mockMvc.perform(get("/group"))
                .andExpect(status().isOk())
                .andReturn();

        //then
        assertEquals(result.getResponse().getContentAsString(), "[]");
    }

    @Test
    public void shouldReturnGroupByIdGivenUserExists() throws Exception {
        //given
        final Group group = getGroup();
        groupRepository.save(group);

        //when
        final MvcResult result = mockMvc.perform(get("/group/1"))
                .andExpect(status().isOk())
                .andReturn();

        //then
        assertEquals(result.getResponse().getContentAsString(), "{\"id\":1,\"name\":\"group_name\",\"description\":\"group_description\"}");
    }

    @Test
    public void shouldReturnNotFoundGivenGroupDoesntExist() throws Exception {
        //given

        //when then
        final MvcResult result = mockMvc.perform(get("/group/1"))
                .andExpect(status().isNotFound())
                .andReturn();

    }

    @Test
    public void shouldCreateGroup() throws Exception {
        //given
        final GroupRequest groupRequest = new GroupRequest(null, GROUP_NAME, GROUP_DESCRIPTION);

        //when
        final MvcResult result = mockMvc.perform(post("/group")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(groupRequest)))
                .andExpect(status().isOk())
                .andReturn();

        //then
        final List<Group> groups = groupRepository.findAll();
        assertThat(groups).isNotEmpty();
        final Group addedGroup = groups.get(0);
        assertEquals(addedGroup.getName(), GROUP_NAME);
        assertEquals(addedGroup.getDescription(), GROUP_DESCRIPTION);
        final String groupResponseString = result.getResponse().getContentAsString();
        assertEquals(groupResponseString, "{\"id\":1,\"name\":\"group_name\",\"description\":\"group_description\"}");
    }


    @Test
    public void shouldDeleteGroup() throws Exception {
        //given
        final Group group = getGroup();
        groupRepository.save(group);

        //when
        final MvcResult result = mockMvc.perform(delete("/group/1"))
                .andExpect(status().isOk())
                .andReturn();

        //then
        final List<Group> groups = groupRepository.findAll();
        assertThat(groups).isEmpty();
    }

    @Test
    public void shouldGetGroupUsers() throws Exception {
        //given
        final User user = createUser();
        userRepository.save(user);
        final Group group = new Group(null, GROUP_NAME, GROUP_DESCRIPTION, Set.of(), List.of());
        groupRepository.save(group);
        userGroupService.save(1L, 1L);

        //when
        final MvcResult result = mockMvc.perform(get("/group/1/users"))
                .andExpect(status().isOk())
                .andReturn();

        //then
        final String response = result.getResponse().getContentAsString();
        assertEquals(response, "[{\"id\":1,\"name\":\"name\",\"surname\":\"surname\",\"email\":\"email\"}]");
    }

    @Test
    public void shouldGetEmptyGroupUsersListGivenGroupHasNoUsers() throws Exception {
        //given
        final Group group = new Group(null, GROUP_NAME, GROUP_DESCRIPTION, Set.of(), List.of());
        groupRepository.save(group);

        //when
        final MvcResult result = mockMvc.perform(get("/group/1/users"))
                .andExpect(status().isOk())
                .andReturn();

        //then
        final String response = result.getResponse().getContentAsString();
        assertEquals(response, "[]");
    }

    @Test
    public void shouldAddReminderToGroup() throws Exception {
        //given
        final Group group = new Group(null, GROUP_NAME, GROUP_DESCRIPTION, Set.of(), List.of());
        groupRepository.save(group);
        final ReminderRequest request = new ReminderRequest(null, REMINDER_NAME, REMINDER_DESCRIPTION, LOCAL_DATE, PERIOD, LOCAL_TIME);

        //when
        final MvcResult result = mockMvc.perform(post("/group/1/add/reminder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        //then
        final String response = result.getResponse().getContentAsString();
        assertEquals(response, "{\"id\":1,\"name\":\"reminder_name\",\"description\":\"reminder_description\",\"date\":\"2023-01-29\",\"period\":228,\"time\":\"00:28:09.437523\",\"userId\":null,\"groupId\":1}");
        final List<Reminder> reminders =  reminderRepository.findAll();
        assertThat(reminders).isNotEmpty();
        final Reminder reminder = reminders.get(0);
        assertEquals(reminder.getName(), REMINDER_NAME);
        assertEquals(reminder.getDescription(), REMINDER_DESCRIPTION);
        assertEquals(reminder.getGroup().getName(), GROUP_NAME);
        assertEquals(reminder.getGroup().getDescription(), GROUP_DESCRIPTION);
    }

    @Test
    public void shouldReturnNotFoundWhenAddingReminderGivenGroupDoesntExist() throws Exception {
        //given
        final ReminderRequest request = new ReminderRequest(null, REMINDER_NAME, REMINDER_DESCRIPTION, LOCAL_DATE, PERIOD, LOCAL_TIME);

        //when
        final MvcResult result = mockMvc.perform(post("/group/1/add/reminder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andReturn();

        //then
        final String response = result.getResolvedException().getMessage();
        assertEquals(response, "NO_GROUP");
    }

    @Test
    public void shouldDeleteReminder() throws Exception {
        //given
        final Group group = new Group(null, GROUP_NAME, GROUP_DESCRIPTION, Set.of(), List.of());
        groupRepository.save(group);
        final Reminder reminder = new Reminder(null, REMINDER_NAME, REMINDER_DESCRIPTION, LOCAL_DATE, null, null, new Recurrence(null, PERIOD, LOCAL_TIME));
        groupService.addReminder(1L, reminder);
        //when
        final MvcResult result = mockMvc.perform(delete("/group/1/delete/reminder/1"))
                .andExpect(status().isOk())
                .andReturn();

        //then
        final List<Reminder> reminders = reminderRepository.findAll();
        assertThat(reminders).isEmpty();
    }
    @Test
    public void shouldReturnNotFoundWhenDeletingReminderGivenReminderDoesntExist() throws Exception {
        //given

        //when
        final MvcResult result = mockMvc.perform(delete("/group/1/delete/reminder/1"))
                .andExpect(status().isNotFound())
                .andReturn();

        //then
        final String response = result.getResolvedException().getMessage();
        assertEquals(response, "NO_REMINDER");
    }

    @Test
    public void shouldGetGroupReminders() throws Exception {
        //given
        final Group group = new Group(null, GROUP_NAME, GROUP_DESCRIPTION, Set.of(), List.of());
        groupRepository.save(group);
        final Reminder reminder = new Reminder(null, REMINDER_NAME, REMINDER_DESCRIPTION, LOCAL_DATE, null, null, new Recurrence(null, PERIOD, LOCAL_TIME));
        groupService.addReminder(1L, reminder);
        //when
        final MvcResult result = mockMvc.perform(get("/group/1/reminders"))
                .andExpect(status().isOk())
                .andReturn();

        //then
        final String response = result.getResponse().getContentAsString();
        assertEquals(response, "[{\"id\":1,\"name\":\"reminder_name\",\"description\":\"reminder_description\",\"date\":\"2023-01-29\",\"period\":228,\"time\":\"00:28:09\",\"userId\":null,\"groupId\":1}]");
    }

    @Test
    public void shouldReturnNotFoundWhenGetGroupRemindersGivenGroupWithGroupIDDoesntExist() throws Exception {
        //given
        //when
        final MvcResult result = mockMvc.perform(get("/group/1/reminders"))
                .andExpect(status().isNotFound())
                .andReturn();

        //then
        final String response = result.getResolvedException().getMessage();
        assertEquals(response, "NO_GROUP");
    }


    private Group getGroup() {
        return new Group(null, GROUP_NAME, GROUP_DESCRIPTION, Set.of(), List.of());
    }

    private User createUser() {
        return new User(1L, PASSWORD, NAME, SURNAME, EMAIL, Set.of(), List.of());
    }
}