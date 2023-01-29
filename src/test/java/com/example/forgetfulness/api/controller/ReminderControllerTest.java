package com.example.forgetfulness.api.controller;

import com.example.forgetfulness.api.DTO.request.ReminderRequest;
import com.example.forgetfulness.application.entity.Recurrence;
import com.example.forgetfulness.application.entity.Reminder;
import com.example.forgetfulness.application.repository.ReminderRepository;
import com.example.forgetfulness.application.service.ReminderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static com.example.forgetfulness.api.controller.TestValues.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReminderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReminderService reminderService;

    @Autowired
    private ReminderRepository reminderRepository;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void shouldReturnListOfReminders() throws Exception {
        //given
        final Reminder reminder = new Reminder(null, REMINDER_NAME, REMINDER_DESCRIPTION, LOCAL_DATE, null, null, null);
        reminderRepository.save(reminder);
        //when
        final MvcResult result = mockMvc.perform(get("/reminder"))
                .andExpect(status().isOk())
                .andReturn();

        //then
        assertEquals(result.getResponse().getContentAsString(), "[{\"id\":1,\"name\":\"reminder_name\",\"description\":\"reminder_description\",\"date\":\"2023-01-29\",\"period\":null,\"time\":null,\"userId\":null,\"groupId\":null}]");

    }

    @Test
    public void shouldReturnEmptyListGivenNoRemindersExist() throws Exception {
        //given

        //when
        final MvcResult result = mockMvc.perform(get("/reminder"))
                .andExpect(status().isOk())
                .andReturn();

        //then
        assertEquals(result.getResponse().getContentAsString(), "[]");

    }

    @Test
    public void shouldGetReminderByID() throws Exception {
        //given
        final Reminder reminder = new Reminder(null, REMINDER_NAME, REMINDER_DESCRIPTION, LOCAL_DATE, null, null, null);
        reminderRepository.save(reminder);
        //when
        final MvcResult result = mockMvc.perform(get("/reminder/1"))
                .andExpect(status().isOk())
                .andReturn();

        //then
        assertEquals(result.getResponse().getContentAsString(), "{\"id\":1,\"name\":\"reminder_name\",\"description\":\"reminder_description\",\"date\":\"2023-01-29\",\"period\":null,\"time\":null,\"userId\":null,\"groupId\":null}");
    }

    @Test
    public void shouldReturnNotFoundGivenReminderWithIdDoesntExist() throws Exception {
        //given

        //when then
        final MvcResult result = mockMvc.perform(get("/reminder/1"))
                .andExpect(status().isNotFound())
                .andReturn();

    }

    @Test
    public void shouldCreateReminder() throws Exception {
        //given
        final ReminderRequest request = new ReminderRequest(null, REMINDER_NAME, REMINDER_DESCRIPTION, LOCAL_DATE, PERIOD, LOCAL_TIME);

        //when
        final MvcResult result = mockMvc.perform(post("/reminder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        //then
        final List<Reminder> reminders = reminderRepository.findAll();
        assertThat(reminders).isNotEmpty();
        final Reminder reminder = reminders.get(0);
        assertEquals(reminder.getName(), REMINDER_NAME);
        assertEquals(reminder.getDescription(), REMINDER_DESCRIPTION);
        assertEquals(reminder.getRecurrence().getPeriod(), PERIOD);
        assertEquals(reminder.getDate(), LOCAL_DATE);
        assertEquals(result.getResponse().getContentAsString(), "{\"id\":1,\"name\":\"reminder_name\",\"description\":\"reminder_description\",\"date\":\"2023-01-29\",\"period\":228,\"time\":\"00:28:09.437523\",\"userId\":null,\"groupId\":null}");
    }

    @Test
    public void shouldUpdateReminder() throws Exception {
        //given
        final Reminder reminder = new Reminder(null, REMINDER_NAME, REMINDER_DESCRIPTION, LOCAL_DATE, null, null, new Recurrence(null, PERIOD, LOCAL_TIME));
        reminderService.create(reminder);
        final ReminderRequest request = new ReminderRequest(1L, REMINDER_NAME, NEW_REMINDER_DESCRIPTION, LOCAL_DATE, NEW_PERIOD, LOCAL_TIME);

        //when
        final MvcResult result = mockMvc.perform(put("/reminder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        //then
        final List<Reminder> reminders = reminderRepository.findAll();
        assertThat(reminders).isNotEmpty();
        final Reminder resulting_reminder = reminders.get(0);
        assertEquals(resulting_reminder.getName(), REMINDER_NAME);
        assertEquals(resulting_reminder.getDescription(), NEW_REMINDER_DESCRIPTION);
        assertEquals(resulting_reminder.getRecurrence().getPeriod(), NEW_PERIOD);
        assertEquals(resulting_reminder.getDate(), LOCAL_DATE);
        assertEquals(result.getResponse().getContentAsString(), "{\"id\":1,\"name\":\"reminder_name\",\"description\":\"new_reminder_description\",\"date\":\"2023-01-29\",\"period\":322,\"time\":\"00:28:09.437523\",\"userId\":null,\"groupId\":null}");
    }

    @Test
    public void shouldReturnNotFoundGivenReminderWithIDDoesntExist() throws Exception {
        //given
        final ReminderRequest request = new ReminderRequest(1L, REMINDER_NAME, NEW_REMINDER_DESCRIPTION, LOCAL_DATE, NEW_PERIOD, LOCAL_TIME);

        //when
        final MvcResult result = mockMvc.perform(put("/reminder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andReturn();

        //then
        final String response = result.getResolvedException().getMessage();
        assertEquals(response, "NO_REMINDER");
    }

    @Test
    public void shouldDeleteReminder() throws Exception {
        //given
        final Reminder reminder = new Reminder(null, REMINDER_NAME, REMINDER_DESCRIPTION, LOCAL_DATE, null, null, new Recurrence(null, PERIOD, LOCAL_TIME));
        reminderService.create(reminder);

        //when
        final MvcResult result = mockMvc.perform(delete("/reminder/1"))
                .andExpect(status().isOk())
                .andReturn();

        //then
        final List<Reminder> reminders = reminderRepository.findAll();
        assertThat(reminders).isEmpty();
    }
}
