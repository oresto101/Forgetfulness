package com.example.forgetfulness;

import com.example.forgetfulness.api.DTO.request.ReminderRequest;
import com.example.forgetfulness.api.DTO.response.ReminderResponse;
import com.example.forgetfulness.application.entity.Reminder;
import com.example.forgetfulness.application.service.ReminderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ReminderControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ReminderService reminderService;

    @Test
    public void testCreateReminder() {
        ReminderRequest reminderRequest = new ReminderRequest();
        reminderRequest.setName("Test reminder");
        reminderRequest.setDescription("Test description");
        reminderRequest.setDate(LocalDate.parse("2022-01-01"));
        reminderRequest.setPeriod(1L);
        HttpEntity<ReminderRequest> request = new HttpEntity<>(reminderRequest);

        ResponseEntity<ReminderResponse> response = restTemplate.postForEntity("/reminder", request, ReminderResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Test reminder");
        assertThat(response.getBody().getDescription()).isEqualTo("Test description");
        assertThat(response.getBody().getDate()).isEqualTo("2022-01-01");
        assertThat(response.getBody().getPeriod()).isEqualTo(1L);

        // Clean up
        reminderService.delete(response.getBody().getId());
    }

    @Test
    public void testGetAllReminders() {
        ReminderRequest reminderRequest = new ReminderRequest();
        reminderRequest.setName("Test reminder");
        reminderRequest.setDescription("Test description");
        reminderRequest.setDate(LocalDate.parse("2022-01-01"));
        reminderRequest.setPeriod(1L);
        HttpEntity<ReminderRequest> request = new HttpEntity<>(reminderRequest);
        restTemplate.postForEntity("/reminder", request, ReminderResponse.class);

        ResponseEntity<List> response = restTemplate.getForEntity("/reminder", List.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isGreaterThan(0);

    }

    @Test
    public void testGetReminderById() {
        ReminderRequest reminderRequest = new ReminderRequest();
        reminderRequest.setName("Test reminder");
        reminderRequest.setDescription("Test description");
        reminderRequest.setDate(LocalDate.parse("2022-01-01"));
        reminderRequest.setPeriod(1L);
        HttpEntity<ReminderRequest> request = new HttpEntity<>(reminderRequest);
        ResponseEntity<ReminderResponse> postResponse = restTemplate.postForEntity("/reminder", request, ReminderResponse.class);
        Long id = postResponse.getBody().getId();

        ResponseEntity<ReminderResponse> response = restTemplate.getForEntity("/reminder/" + id, ReminderResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Test reminder");
        assertThat(response.getBody().getDescription()).isEqualTo("Test description");
        assertThat(response.getBody().getDate()).isEqualTo("2022-01-01");
        assertThat(response.getBody().getPeriod()).isEqualTo(1L);

        // Clean up
        reminderService.delete(id);
    }

    @Test
    public void testUpdateReminder() {
        ReminderRequest reminderRequest = new ReminderRequest();
        reminderRequest.setName("Test reminder");
        reminderRequest.setDescription("Test description");
        reminderRequest.setDate(LocalDate.parse("2022-01-01"));
        reminderRequest.setPeriod(1L);
        HttpEntity<ReminderRequest> request = new HttpEntity<>(reminderRequest);
        ResponseEntity<ReminderResponse> postResponse = restTemplate.postForEntity("/reminder", request, ReminderResponse.class);
        Long id = postResponse.getBody().getId();

        reminderRequest.setName("Updated reminder");
        reminderRequest.setId(id);
        request = new HttpEntity<>(reminderRequest);
        restTemplate.exchange("/reminder", HttpMethod.PUT, request, ReminderResponse.class);

        ResponseEntity<ReminderResponse> response = restTemplate.getForEntity("/reminder/" + id, ReminderResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Updated reminder");
        assertThat(response.getBody().getDescription()).isEqualTo("Test description");
        assertThat(response.getBody().getDate()).isEqualTo("2022-01-01");
        assertThat(response.getBody().getPeriod()).isEqualTo(1L);

        // Clean up
        reminderService.delete(id);
    }
    @Test
    public void testDeleteReminder() {
        ReminderRequest reminderRequest = new ReminderRequest();
        reminderRequest.setName("Test reminder");
        reminderRequest.setDescription("Test description");
        reminderRequest.setDate(LocalDate.parse("2022-01-01"));
        reminderRequest.setPeriod(1L);
        HttpEntity<ReminderRequest> request = new HttpEntity<>(reminderRequest);
        ResponseEntity<ReminderResponse> postResponse = restTemplate.postForEntity("/reminder", request, ReminderResponse.class);
        Long id = postResponse.getBody().getId();

        ResponseEntity<String> deleteResponse = restTemplate.exchange("/reminder/" + id, HttpMethod.DELETE, null, String.class);

        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Verify that the reminder was deleted
        ResponseEntity<ReminderResponse> getResponse = restTemplate.getForEntity("/reminder/" + id, ReminderResponse.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

}

