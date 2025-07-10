package com.codewithmosh.store.rapport;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/report")
public class LogReportController {

    private final LogReportService logReportService;

    public LogReportController(LogReportService logReportService) {
        this.logReportService = logReportService;
    }

    /**
     * GET /api/report?start=2025-07-01T00:00:00&end=2025-07-10T23:59:59
     */
    @GetMapping("/session")
    public List<OperationLogDTO> getReportSession(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        return logReportService.getReportSession(start, end);
    }


    @GetMapping("/session/pdf")
    public ResponseEntity<byte[]> getReportSessionPdf(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        List<OperationLogDTO> logs = logReportService.getReportSession(start, end);
        byte[] pdfBytes = logReportService.generatePdfReport(logs);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=lab_sessions_report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    @GetMapping("/users")
    public List<OperationLogDTO> getReportUsers(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        return logReportService.getReportUsers(start, end);
    }


    @GetMapping("/users/pdf")
    public ResponseEntity<byte[]> getReportUsersPdf(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        List<OperationLogDTO> logs = logReportService.getReportUsers(start, end);
        byte[] pdfBytes = logReportService.generatePdfReport(logs);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=lab_users_report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    @GetMapping("/equipment")
    public List<OperationLogDTO> getReportEquipment(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        return logReportService.getReportEquipment(start, end);
    }


    @GetMapping("/equipment/pdf")
    public ResponseEntity<byte[]> getReportEquipmentPdf(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        List<OperationLogDTO> logs = logReportService.getReportEquipment(start, end);
        byte[] pdfBytes = logReportService.generatePdfReport(logs);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=lab_equipments_report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
    @GetMapping("/usage")
    public List<OperationLogDTO> getReportUsage(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        return logReportService.getReportChemical(start, end);
    }


    @GetMapping("/usage/pdf")
    public ResponseEntity<byte[]> getReportChemicalPdf(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        List<OperationLogDTO> logs = logReportService.getReportChemical(start, end);
        byte[] pdfBytes = logReportService.generatePdfReport(logs);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=lab_chemical_report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    @GetMapping("/stock")
    public List<OperationLogDTO> getReportStock(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        return logReportService.getReportStock(start, end);
    }


    @GetMapping("/stock/pdf")
    public ResponseEntity<byte[]> getReportStockPdf(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        List<OperationLogDTO> logs = logReportService.getReportStock(start, end);
        byte[] pdfBytes = logReportService.generatePdfReport(logs);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=lab_stock_report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}
