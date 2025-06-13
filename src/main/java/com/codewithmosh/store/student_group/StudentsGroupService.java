package com.codewithmosh.store.student_group;

import com.codewithmosh.store.common.EntityNotFoundException;
import com.codewithmosh.store.group.Group;
import com.codewithmosh.store.group.GroupRepository;
import com.codewithmosh.store.user.User;
import com.codewithmosh.store.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class StudentsGroupService {

    private final StudentsGroupRepository studentsGroupRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final StudentsGroupMapper studentsGroupMapper;

    public List<StudentsGroupDto> findAll() {
        return studentsGroupRepository.findAll()
                .stream()
                .map(studentsGroupMapper::toDto)
                .toList();
    }

    public StudentsGroupDto create(CreateStudentsGroupRequest request) {
        StudentsGroup studentsGroup = studentsGroupMapper.toEntity(request);

        Group group = groupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new EntityNotFoundException("Group not found"));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        studentsGroup.setGroup(group);
        studentsGroup.setUser(user);

        studentsGroupRepository.save(studentsGroup);
        return studentsGroupMapper.toDto(studentsGroup);
    }

    public StudentsGroupDto update(Integer id, UpdateStudentsGroupRequest request) {
        StudentsGroup studentsGroup = studentsGroupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("StudentsGroup not found"));

        studentsGroupMapper.update(request, studentsGroup);

        Group group = groupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new EntityNotFoundException("Group not found"));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        studentsGroup.setGroup(group);
        studentsGroup.setUser(user);

        studentsGroupRepository.save(studentsGroup);
        return studentsGroupMapper.toDto(studentsGroup);
    }

    public void delete(Integer id) {
        studentsGroupRepository.deleteById(id);
    }

    public StudentsGroupDto findById(Integer id) {
        StudentsGroup studentsGroup = studentsGroupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("StudentsGroup not found"));
        return studentsGroupMapper.toDto(studentsGroup);
    }
}
