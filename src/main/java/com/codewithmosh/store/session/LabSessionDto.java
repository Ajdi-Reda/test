package com.codewithmosh.store.session;

import com.codewithmosh.store.equipments.EquipmentLoanDto;
import com.codewithmosh.store.product.usage.UsageDto;
import lombok.Data;
import java.time.Instant;
import java.util.List;

@Data
public class LabSessionDto {
    private Integer id;
    private Integer groupId;
    private Integer labId;
    private Instant scheduledStart;
    private Instant scheduledEnd;
    private Boolean validated;
    private Integer createdBy;
    private List<EquipmentLoanDto> equipmentLoans;
    private List<UsageDto> chemicalUsages;
}
