package com.codewithmosh.store.subject;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final SubjectMapper subjectMapper;

    public List<SubjectDto> getAllSubjects() {
        return subjectRepository.findAll()
                .stream()
                .map(subjectMapper::toDto)
                .collect(Collectors.toList());
    }

    public Optional<SubjectDto> getSubjectById(Integer id) {
        return subjectRepository.findById(id)
                .map(subjectMapper::toDto);
    }
    public long countNumberSubjects() {
        return subjectRepository.count();
    }
}
