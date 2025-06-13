package com.codewithmosh.store.group;

import com.codewithmosh.store.common.EntityNotFoundException;
import com.codewithmosh.store.subject.Subject;
import com.codewithmosh.store.subject.SubjectRepository;
import com.codewithmosh.store.user.User;
import com.codewithmosh.store.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;
    private final GroupMapper groupMapper;

    public List<GroupDto> findAll() {
        return groupRepository.findAll()
                .stream()
                .map(groupMapper::toDto)
                .toList();
    }

    public GroupDto create(GroupCreateRequest request) {
        Group group = groupMapper.toEntity(request);

        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new EntityNotFoundException("Subject not found"));

        User teacher = userRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found"));

        group.setSubject(subject);
        group.setTeacher(teacher);

        groupRepository.save(group);
        return groupMapper.toDto(group);
    }


    public GroupDto update(Integer id, GroupCreateRequest request) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Group not found"));

        groupMapper.update(request, group);
        setSubjectAndTeacher(group, request.getSubjectId(), request.getTeacherId());
        groupRepository.save(group);
        return groupMapper.toDto(group);
    }

    public void delete(Integer id) {
        groupRepository.deleteById(id);
    }

    public GroupDto findById(Integer id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Group not found"));
        return groupMapper.toDto(group);
    }

    private void setSubjectAndTeacher(Group group, Integer subjectId, Integer teacherId) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new EntityNotFoundException("Subject not found"));

        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found"));

        group.setSubject(subject);
        group.setTeacher(teacher);
    }
}
