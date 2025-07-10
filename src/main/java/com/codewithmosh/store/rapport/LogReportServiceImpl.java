package com.codewithmosh.store.rapport;

import com.codewithmosh.store.equipments.EquipmentLoanRepository;
import com.codewithmosh.store.equipments.Equipmentloan;
import com.codewithmosh.store.session.LabSession;
import com.codewithmosh.store.session.LabSessionRepository;
import com.codewithmosh.store.user.User;
import com.codewithmosh.store.user.UserRepository;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class LogReportServiceImpl implements LogReportService {
    private final LabSessionRepository labSessionRepository;
    private final UserRepository userRepository;
    private final EquipmentLoanRepository equipmentLoanRepository;

    public LogReportServiceImpl(LabSessionRepository labSessionRepository , UserRepository user , EquipmentLoanRepository equipmentLoanRepository) {
        this.labSessionRepository = labSessionRepository;
        this.userRepository = user;
        this.equipmentLoanRepository = equipmentLoanRepository;
    }


    public List<OperationLogDTO> getReportSession(LocalDateTime start, LocalDateTime end) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        List<LabSession> sessions = labSessionRepository.findByCreatedAtBetween(start, end);

        return sessions.stream().map(session -> {
            OperationLogDTO dto = new OperationLogDTO();
            dto.setType("LAB_SESSION");

            LocalDateTime startTime = session.getScheduledStart()
                    .atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime endTime = session.getScheduledEnd()
                    .atZone(ZoneId.systemDefault()).toLocalDateTime();

            String formattedDescription = "Lab session scheduled: " +
                    startTime.format(dateFormatter) + " | " +
                    startTime.format(timeFormatter) + " → " +
                    endTime.format(timeFormatter);

            dto.setDescription(formattedDescription);
            dto.setTimestamp(session.getCreatedAt().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDateTime());
            dto.setPerformedBy(session.getCreatedBy().getName());
            return dto;
        }).collect(Collectors.toList());
    }
    public List<OperationLogDTO> getReportUsers(LocalDateTime start, LocalDateTime end){
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        List<User> users = userRepository.findByCreatedAtBetween(start, end);

        return users.stream().map(user -> {
            OperationLogDTO dto = new OperationLogDTO();
            dto.setType("USER_CREATION");

            String formattedDescription = "User created: " +
                    user.getName() + " (" + user.getEmail() + ") on " +
                    user.getCreatedAt().toInstant()
                            .atZone(ZoneId.systemDefault()).toLocalDate().format(dateFormatter) + " at " +
                    user.getCreatedAt().toInstant()
                            .atZone(ZoneId.systemDefault()).toLocalTime().format(timeFormatter);

            dto.setDescription(formattedDescription);
            dto.setTimestamp(user.getCreatedAt().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDateTime());
            dto.setPerformedBy(user.getName());

            return dto;
        }).collect(Collectors.toList());

    }
    public List<OperationLogDTO> getReportEquipment(LocalDateTime start, LocalDateTime end){
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        List<Equipmentloan> loans = equipmentLoanRepository.findByCreatedAtBetween(start, end);

        return loans.stream().map(loan -> {
            OperationLogDTO dto = new OperationLogDTO();
            dto.setType("EQUIPMENT_LOAN");

            String equipmentName = loan.getEquipment() != null ? loan.getEquipment().getBarcode()+" "+ loan.getEquipment().getBrand() +" "+loan.getEquipment().getModel()  : "Unknown Equipment";
            String userName = loan.getUser() != null ? loan.getUser().getName() : "Unknown User";

            String fromTime = loan.getReservedFrom() != null
                    ? loan.getReservedFrom().atZone(ZoneId.systemDefault()).format(timeFormatter)
                    : "??";
            String toTime = loan.getReservedTo() != null
                    ? loan.getReservedTo().atZone(ZoneId.systemDefault()).format(timeFormatter)
                    : "??";

            String reservationDate = loan.getReservedFrom() != null
                    ? loan.getReservedFrom().atZone(ZoneId.systemDefault()).toLocalDate().format(dateFormatter)
                    : "Unknown Date";

            String formattedDescription = "Equipment '" + equipmentName + "' reserved by " + userName +
                    " on " + reservationDate + " | " + fromTime + " → " + toTime;

            dto.setDescription(formattedDescription);
            dto.setTimestamp(loan.getCreatedAt().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDateTime());
            dto.setPerformedBy(userName);

            return dto;
        }).collect(Collectors.toList());
    }
    public List<OperationLogDTO> getReportChemical(LocalDateTime start, LocalDateTime end){
        return null;
    }
    public List<OperationLogDTO> getReportStock(LocalDateTime start, LocalDateTime end){
        return null;
    }


    public byte[] generatePdfReport(List<OperationLogDTO> logs) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4, 40, 40, 50, 50); // add margins
            PdfWriter.getInstance(document, baos);
            document.open();

            // Title font with white color for contrast
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, Color.WHITE);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.BLACK);
            Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.DARK_GRAY);

            // Title background container
            PdfPTable titleTable = new PdfPTable(1);
            titleTable.setWidthPercentage(100);
            PdfPCell titleCell = new PdfPCell(new Phrase(
                    logs.isEmpty() ? "Operation Log Report" : logs.get(0).getType() + " Report", titleFont));
            titleCell.setBackgroundColor(new Color(0, 121, 184)); // nice blue
            titleCell.setPadding(15);
            titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            titleCell.setBorder(Rectangle.NO_BORDER);
            titleTable.addCell(titleCell);
            document.add(titleTable);
            document.add(Chunk.NEWLINE); // space after title

            // Table with 4 columns: Type, Description, Timestamp, Performed By
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{4, 6, 4, 4});
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Add header cells with background color
            Stream.of("Type", "Description", "Timestamp", "Performed By").forEach(header -> {
                PdfPCell headerCell = new PdfPCell(new Phrase(header, headerFont));
                headerCell.setBackgroundColor(new Color(220, 230, 241)); // light blue-gray
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerCell.setPadding(15);
                table.addCell(headerCell);
            });

            DateTimeFormatter timestampFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            boolean alternate = false;
            for (OperationLogDTO log : logs) {
                Color rowColor = alternate ? new Color(245, 247, 250) : Color.WHITE;
                alternate = !alternate;

                PdfPCell typeCell = new PdfPCell(new Phrase(log.getType(), bodyFont));
                typeCell.setBackgroundColor(rowColor);
                typeCell.setPadding(10);
                typeCell.setNoWrap(true);            // Prevent wrapping, keep in one line
                table.addCell(typeCell);

                PdfPCell descCell = new PdfPCell(new Phrase(log.getDescription(), bodyFont));
                descCell.setBackgroundColor(rowColor);
                descCell.setPadding(10);
                // no setNoWrap here, so it can wrap normally
                table.addCell(descCell);

                PdfPCell timeCell = new PdfPCell(new Phrase(log.getTimestamp().format(timestampFormatter), bodyFont));
                timeCell.setBackgroundColor(rowColor);
                timeCell.setPadding(10);
                timeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                timeCell.setNoWrap(true);            // Prevent wrapping
                table.addCell(timeCell);

                PdfPCell performedByCell = new PdfPCell(new Phrase(log.getPerformedBy(), bodyFont));
                performedByCell.setBackgroundColor(rowColor);
                performedByCell.setPadding(10);
                performedByCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                performedByCell.setNoWrap(true);    // Prevent wrapping
                table.addCell(performedByCell);
            }


            document.add(table);
            document.close();

            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF report", e);
        }
    }
}
