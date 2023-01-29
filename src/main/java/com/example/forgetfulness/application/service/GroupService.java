package com.example.forgetfulness.application.service;

import com.example.forgetfulness.application.entity.Group;
import com.example.forgetfulness.application.entity.Reminder;
import com.example.forgetfulness.application.exception.ForgetfulnessException;
import com.example.forgetfulness.application.exception.ForgetfulnessExceptionType;
import com.example.forgetfulness.application.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;
    private final ReminderService reminderService;

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    public Optional<Group> getGroupById(Long id) {
        return groupRepository.findById(id);
    }

    public Group save(Group Group) {
        if (Group.isIdNotNull()) {
            throw new ForgetfulnessException(ForgetfulnessExceptionType.GROUP_ALREADY_EXISTS);
        }

        return groupRepository.save(Group);
    }

    public void delete(Long id) {
        groupRepository.delete(getValidatedGroup(id));
    }

    public Reminder addReminder(Long groupId, Reminder reminderRequest) {
        if (groupId == null || reminderRequest.isIdNotNull()) {
            throw new ForgetfulnessException(ForgetfulnessExceptionType.ID_PROBLEM);
        }

        reminderRequest.setGroup(getValidatedGroup(groupId));

        return reminderService.create(reminderRequest);
    }

    public void deleteReminder(Long groupId, Long reminderId) {
        if (groupId == null || reminderId == null) {
            throw new ForgetfulnessException(ForgetfulnessExceptionType.ID_PROBLEM);
        }

        Optional<Reminder> reminder = reminderService.getReminderById(reminderId);
        if (reminder.isEmpty()) {
            throw new ForgetfulnessException(ForgetfulnessExceptionType.NO_REMINDER);
        }

        reminderService.delete(reminder.get().getId());
    }

    public List<Reminder> getGroupReminders(Long groupId) {
        if (groupId == null) {
            throw new ForgetfulnessException(ForgetfulnessExceptionType.ID_PROBLEM);
        }
        Group group = getValidatedGroup(groupId);

        return new ArrayList<>(group.getReminders());
    }


    private Group getValidatedGroup(Long id) {
        Optional<Group> group = groupRepository.findById(id);
        if (group.isEmpty()) {
            throw new ForgetfulnessException(ForgetfulnessExceptionType.NO_GROUP);
        }
        return group.get();
    }
}
