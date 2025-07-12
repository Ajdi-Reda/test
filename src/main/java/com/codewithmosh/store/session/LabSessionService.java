package com.codewithmosh.store.session;

import com.codewithmosh.store.common.EntityNotFoundException;
import com.codewithmosh.store.group.Group;
import com.codewithmosh.store.group.GroupRepository;
import com.codewithmosh.store.lab.Lab;
import com.codewithmosh.store.lab.LabRepository;
import com.codewithmosh.store.user.User;
import com.codewithmosh.store.user.UserRepository;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LabSessionService {

    private final LabSessionRepository labSessionRepository;
    private final GroupRepository groupRepository;
    private final LabRepository labRepository;
    private final UserRepository userRepository;
    private final LabSessionMapper labSessionMapper;
    public long countNumberLabSessions() {
        return labSessionRepository.count();
    }


    public List<LabSessionDto> findAll() {
        return labSessionRepository.findAll()
                .stream()
                .map(labSessionMapper::toDto)
                .toList();
    }

    public LabSessionDto findById(Integer id) {
        LabSession labSession = labSessionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("LabSession not found"));
        return labSessionMapper.toDto(labSession);
    }

    public LabSessionDto create(LabSessionCreateRequest request) {
        Group group = null;
        if (request.getGroupId() != null) {
            group = groupRepository.findById(request.getGroupId())
                    .orElseThrow(() -> new EntityNotFoundException("Group not found with ID: " + request.getGroupId()));
        }

        // Fetch required Lab
        Lab lab = labRepository.findById(request.getLabId())
                .orElseThrow(() -> new EntityNotFoundException("Lab not found with ID: " + request.getLabId()));

        // Fetch required User (creator)
        User createdBy = userRepository.findById(request.getCreatedBy())
                .orElseThrow(() -> new EntityNotFoundException("User (creator) not found with ID: " + request.getCreatedBy()));

        // Ensure session times don't conflict with existing sessions
        validateSessionTime(lab.getId(), request.getScheduledStart(), request.getScheduledEnd(), null);

        // Map request to entity
        LabSession labSession = labSessionMapper.toEntity(request);
        labSession.setGroup(group);
        labSession.setLab(lab);
        labSession.setCreatedBy(createdBy);

        // Persist entity
        labSessionRepository.save(labSession);

        // Return DTO
        return labSessionMapper.toDto(labSession);
    }

    public LabSessionDto update(Integer id, LabSessionUpdateRequest request) {
        LabSession labSession = labSessionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("LabSession not found"));

        Group group = groupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new EntityNotFoundException("Group not found"));

        Lab lab = labRepository.findById(request.getLabId())
                .orElseThrow(() -> new EntityNotFoundException("Lab not found"));

        User createdBy = userRepository.findById(request.getGroupId())
                .orElseThrow(() -> new EntityNotFoundException("User (creator) not found"));

        // exclude current session id to avoid conflict with itself
        validateSessionTime(lab.getId(), request.getScheduledStart(), request.getScheduledEnd(), id);

        labSessionMapper.update(request, labSession);

        labSession.setGroup(group);
        labSession.setLab(lab);
        labSession.setCreatedBy(createdBy);

        labSessionRepository.save(labSession);
        return labSessionMapper.toDto(labSession);
    }

    public void delete(Integer id) {
        if (!labSessionRepository.existsById(id)) {
            throw new EntityNotFoundException("LabSession not found");
        }
        labSessionRepository.deleteById(id);
    }

    public void validateSessionTime(Integer labId, Instant newStart, Instant newEnd, Integer excludeId) {
        Optional<LabSession> conflictingSession = labSessionRepository.findOverlappingSession(labId, newStart, newEnd, excludeId);

        if (conflictingSession.isPresent()) {
            LabSession conflict = conflictingSession.get();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    .withZone(ZoneId.systemDefault());

            String existingStart = formatter.format(conflict.getScheduledStart());
            String existingEnd = formatter.format(conflict.getScheduledEnd());

            throw new ValidationException(
                    "Session time overlaps with existing session scheduled from "
                            + existingStart + " to " + existingEnd
                            + ". Please choose a different time slot."
            );
        }
    }
}
