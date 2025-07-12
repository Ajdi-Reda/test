package com.codewithmosh.store.lab;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LabService {

    private final LabRepository labRepository;
    private final LabMapper labMapper;

    public LabService(LabRepository labRepository, LabMapper labMapper) {
        this.labRepository = labRepository;
        this.labMapper = labMapper;
    }

    public List<LabDto> getAllLabs() {
        return labRepository.findAll().stream().map(labMapper::toDto).collect(Collectors.toList());
    }

    public Optional<LabDto> getLabById(Integer id) {
        return labRepository.findById(id)
                .map(labMapper::toDto);
    }
    public long countNumberLabs() {
        return labRepository.count();
    }
}
