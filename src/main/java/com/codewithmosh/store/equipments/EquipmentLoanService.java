package com.codewithmosh.store.equipments;

import com.codewithmosh.store.user.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.Instant;

@Service
@AllArgsConstructor
public class EquipmentLoanService {
    private final EquipmentLoanRepository equipmentLoanRepository;
    private final EquipmentRepository equipmentRepository;
    private final EquipmentLoanMapper equipmentLoanMapper;

    public Iterable<EquipmentLoanDto> getEquipmentLoans() {
        return equipmentLoanRepository.findAll()
                .stream()
                .map(equipmentLoanMapper::toDto)
                .toList();
    }

    public EquipmentLoanDto getEquipmentLoan(Integer id) {
        var loan = equipmentLoanRepository.findById(id).orElse(null);
        if (loan == null) {
            return null;
        }
        return equipmentLoanMapper.toDto(loan);
    }

    public EquipmentLoanDto createEquipmentLoan(CreateEquipmentLoanRequest request) {
        // Check if equipment exists
        equipmentRepository.findById(request.getEquipmentId())
                .orElseThrow(() -> new EquipmentNotFoundException());

        // Check if equipment is already on loan
        if (equipmentLoanRepository.existsByEquipmentIdAndReturnedFalse(request.getEquipmentId())) {
            throw new EquipmentNotAvailableException();
        }

        // Validate dates
        if (request.getReservedFrom().isAfter(request.getReservedTo())) {
            throw new InvalidLoanDateException("Reserved from date must be before reserved to date");
        }
        if (request.getExpectedReturnDate().isBefore(request.getReservedTo())) {
            throw new InvalidLoanDateException("Expected return date must be after reserved to date");
        }

        var loan = equipmentLoanMapper.toEntity(request);
        equipmentLoanRepository.save(loan);
        return equipmentLoanMapper.toDto(loan);
    }

    public EquipmentLoanDto updateEquipmentLoan(Integer id, UpdateEquipmentLoanRequest request) {
        var loan = equipmentLoanRepository.findById(id)
                .orElseThrow(EquipmentLoanNotFoundException::new);

        if (request.getReservedTo() != null && request.getReservedTo().isBefore(loan.getReservedFrom())) {
            throw new InvalidLoanDateException("Reserved to date must be after reserved from date");
        }

        equipmentLoanMapper.update(request, loan);
        equipmentLoanRepository.save(loan);
        return equipmentLoanMapper.toDto(loan);
    }


    public void returnEquipment(Integer id, Integer returnedToId) {
        var loan = equipmentLoanRepository.findById(id)
                .orElseThrow(EquipmentLoanNotFoundException::new);

        if (Boolean.TRUE.equals(loan.getReturned())) {
            throw new IllegalStateException("Equipment is already returned");
        }

        loan.setReturned(true);
        loan.setActualReturnDate(Instant.now());

        // Create a new User object if returnedTo is null
        if (loan.getReturnedTo() == null) {
            User returnedTo = new User();
            returnedTo.setId(returnedToId);
            loan.setReturnedTo(returnedTo);
        } else {
            loan.getReturnedTo().setId(returnedToId);
        }

        equipmentLoanRepository.save(loan);
    }

    public void deleteEquipmentLoan(Integer id) {
        if (!equipmentLoanRepository.existsById(id)) {
            throw new EquipmentLoanNotFoundException();
        }
        equipmentLoanRepository.deleteById(id);
    }
}