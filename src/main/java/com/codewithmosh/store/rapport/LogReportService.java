package com.codewithmosh.store.rapport;

import java.time.LocalDateTime;
import java.util.List;

public interface LogReportService {
    public List<OperationLogDTO> getReportSession(LocalDateTime start, LocalDateTime end);
    public byte[] generatePdfReport(List<OperationLogDTO> logs);
    public List<OperationLogDTO> getReportUsers(LocalDateTime start, LocalDateTime end);
    public List<OperationLogDTO> getReportEquipment(LocalDateTime start, LocalDateTime end);
    public List<OperationLogDTO> getReportChemical(LocalDateTime start, LocalDateTime end);
    public List<OperationLogDTO> getReportStock(LocalDateTime start, LocalDateTime end);

}
