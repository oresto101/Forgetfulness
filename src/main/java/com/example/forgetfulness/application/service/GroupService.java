package com.example.forgetfulness.application.service;

import com.example.forgetfulness.application.entity.Group;
import com.example.forgetfulness.application.exception.ForgetfulnessException;
import com.example.forgetfulness.application.exception.ForgetfulnessExceptionType;
import com.example.forgetfulness.application.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    public Optional<Group> getGroupById(Long id) {
        return groupRepository.findById(id);
    }

    public Group save(Group Group) {
        if (Group.isIdNull()) {
            return groupRepository.save(Group);
        }

        throw new ForgetfulnessException(ForgetfulnessExceptionType.ID_PROBLEM);
    }

    public Group update(Group Group) {
        if (Group.isIdNotNull()) {
            return groupRepository.save(Group);
        }

        throw new ForgetfulnessException(ForgetfulnessExceptionType.ID_PROBLEM);
    }

    public void delete(Long id) {
        if (id == null) {
            throw new ForgetfulnessException(ForgetfulnessExceptionType.ID_PROBLEM);
        }

        groupRepository.deleteById(id);
    }
}
