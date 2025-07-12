package com.codewithmosh.store.session;

import com.codewithmosh.store.common.EntityNotFoundException;
import com.codewithmosh.store.equipments.CreateEquipmentLoanRequest;
import com.codewithmosh.store.equipments.EquipmentLoanService;
import com.codewithmosh.store.group.Group;
import com.codewithmosh.store.group.GroupRepository;
import com.codewithmosh.store.lab.Lab;
import com.codewithmosh.store.lab.LabRepository;
import com.codewithmosh.store.product.usage.CreateUsageRequest;
import com.codewithmosh.store.product.usage.UsageService;
import com.codewithmosh.store.user.User;
import com.codewithmosh.store.user.UserRepository;
import jakarta.transaction.Transactional;
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
    private final UsageService usageService;
    private final EquipmentLoanService equipmentLoanService;
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

    @Transactional
    public LabSessionDto create(LabSessionCreateRequest request) {
        Group group = null;
        if (request.getGroupId() != null) {
            group = groupRepository.findById(request.getGroupId())
                    .orElseThrow(() -> new EntityNotFoundException("Group not found with ID: " + request.getGroupId()));
        }

        Lab lab = labRepository.findById(request.getLabId())
                .orElseThrow(() -> new EntityNotFoundException("Lab not found with ID: " + request.getLabId()));

        User createdBy = userRepository.findById(request.getCreatedBy())
                .orElseThrow(() -> new EntityNotFoundException("User (creator) not found with ID: " + request.getCreatedBy()));

        validateSessionTime(lab.getId(), request.getScheduledStart(), request.getScheduledEnd(), null);

        LabSession labSession = labSessionMapper.toEntity(request);
        labSession.setGroup(group);
        labSession.setLab(lab);
        labSession.setCreatedBy(createdBy);

        labSessionRepository.save(labSession); // Persist first to get ID
        Integer sessionId = labSession.getId();

        // Attach sessionId to each usage and loan
        if (request.getProductUsages() != null) {
            for (CreateUsageRequest usage : request.getProductUsages()) {
                usage.setSessionId(sessionId);
            }
            usageService.createBatchUsages(request.getProductUsages());
        }

        if (request.getEquipmentLoans() != null) {
            for (CreateEquipmentLoanRequest loan : request.getEquipmentLoans()) {
                loan.setSessionId(sessionId);
            }
            // Save loans (assuming you have a loanService)
             equipmentLoanService.createBatchEquipmentLoans(request.getEquipmentLoans());
        }

        return labSessionMapper.toDto(labSession);
    }


    @Transactional
    public LabSessionDto update(Integer id, LabSessionUpdateRequest request) {
        LabSession labSession = labSessionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("LabSession not found with ID: " + id));

        Group group = null;
        if (request.getGroupId() != null) {
            group = groupRepository.findById(request.getGroupId())
                    .orElseThrow(() -> new EntityNotFoundException("Group not found with ID: " + request.getGroupId()));
        }

        Lab lab = labRepository.findById(request.getLabId())
                .orElseThrow(() -> new EntityNotFoundException("Lab not found with ID: " + request.getLabId()));

        User createdBy = userRepository.findById(request.getCreatedBy())
                .orElseThrow(() -> new EntityNotFoundException("User (creator) not found with ID: " + request.getCreatedBy()));

        // Validate time (exclude current session to avoid self-conflict)
        validateSessionTime(lab.getId(), request.getScheduledStart(), request.getScheduledEnd(), id);

        // Update basic fields
        labSessionMapper.update(request, labSession);
        labSession.setGroup(group);
        labSession.setLab(lab);
        labSession.setCreatedBy(createdBy);

        labSessionRepository.save(labSession);

        Integer sessionId = labSession.getId();

        // 🧽 Delete existing usages and loans
        usageService.deleteBySessionId(sessionId);
        equipmentLoanService.deleteBySessionId(sessionId);

        // 🔁 Re-insert new usages
        if (request.getProductUsages() != null) {
            for (CreateUsageRequest usage : request.getProductUsages()) {
                usage.setSessionId(sessionId);
            }
            usageService.createBatchUsages(request.getProductUsages());
        }

        // 🔁 Re-insert new equipment loans
        if (request.getEquipmentLoans() != null) {
            for (CreateEquipmentLoanRequest loan : request.getEquipmentLoans()) {
                loan.setSessionId(sessionId);
            }
            equipmentLoanService.createBatchEquipmentLoans(request.getEquipmentLoans());
        }

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
