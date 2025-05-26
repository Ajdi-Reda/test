package com.codewithmosh.store.equipments;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EquipmentService {

    private final EquipmentRepository EquipmentRepository;
    private final EquipmentMapper EquipmentMapper;

    public Iterable<EquipmentDto> getEquipments() {
        return EquipmentRepository.findAll().stream().map(EquipmentMapper::toDto).toList();
    }

    public EquipmentDto getEquipment(Integer id) {
        var Equipment = EquipmentRepository.findById(id).orElse(null);
        if (Equipment == null) {
            return null;
        }

        return EquipmentMapper.toDto(Equipment);
    }

    public EquipmentDto createEquipment(CreateEquipmentRequest request) {
        if(EquipmentRepository.existsByBarcode(request.getBarcode())) {
            throw new EquipmentAlreadyExistsException();
        }

        Equipment equipment = EquipmentMapper.toEntity(request);
        EquipmentRepository.save(equipment);

        return EquipmentMapper.toDto(equipment);
    }

    public EquipmentDto updateEquipment(Integer id, UpdateEquipmentRequest request) {
        var Equipment = EquipmentRepository.findById(id).orElse(null);
        if (Equipment == null) {
            return null;
        }

        EquipmentMapper.update(request, Equipment);
        EquipmentRepository.save(Equipment);
        return EquipmentMapper.toDto(Equipment);
    }

    public void deleteEquipment(Integer id) {
        var Equipment = EquipmentRepository.findById(id).orElse(null);

        EquipmentRepository.deleteById(id);
    }
}
