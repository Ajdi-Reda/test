package com.codewithmosh.store.rapport;

import com.codewithmosh.store.equipments.EquipmentLoanRepository;
import com.codewithmosh.store.equipments.Equipmentloan;
import com.codewithmosh.store.product.item.ChemicalProduct;
import com.codewithmosh.store.product.item.ProductRepository;
import com.codewithmosh.store.product.stock.StockAlert;
import com.codewithmosh.store.product.stock.StockAlertRepository;
import com.codewithmosh.store.product.usage.ChemicalUsage;
import com.codewithmosh.store.product.usage.UsageRepository;
import com.codewithmosh.store.session.LabSession;
import com.codewithmosh.store.session.LabSessionRepository;
import com.codewithmosh.store.user.User;
import com.codewithmosh.store.user.UserRepository;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.net.URL;
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
    private final UsageRepository usageRepository;
    private final StockAlertRepository stockAlertRepository;
    private final ProductRepository productRepository;

    public LogReportServiceImpl(LabSessionRepository labSessionRepository , UserRepository user , EquipmentLoanRepository equipmentLoanRepository , UsageRepository usageRepository , StockAlertRepository  stockAlertRepository , ProductRepository  productRepository) {
        this.labSessionRepository = labSessionRepository;
        this.userRepository = user;
        this.equipmentLoanRepository = equipmentLoanRepository;
        this.usageRepository = usageRepository;
        this.stockAlertRepository = stockAlertRepository;
        this.productRepository = productRepository;
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
    public List<OperationLogDTO> getReportChemical(LocalDateTime start, LocalDateTime end) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        List<ChemicalUsage> usages = usageRepository.findByCreatedAtBetween(start, end);

        return usages.stream().map(usage -> {
            OperationLogDTO dto = new OperationLogDTO();
            dto.setType("CHEMICAL_USAGE");

            String productName = usage.getProduct() != null ? usage.getProduct().getName() : "Unknown Product";
            String userName = usage.getTakenBy() != null ? usage.getTakenBy().getName() : "Unknown User";

            String formattedDescription = "Chemical used: '" + productName + "', taken by " + userName +
                    " | Amount: " + usage.getAmount() + " | Date: " + usage.getDate().format(dateFormatter);

            dto.setDescription(formattedDescription);

            dto.setTimestamp(usage.getCreatedAt().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDateTime());

            dto.setPerformedBy(userName);

            return dto;
        }).collect(Collectors.toList());
    }

    public List<OperationLogDTO> getReportStock(LocalDateTime start, LocalDateTime end){
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        List<StockAlert> alerts = stockAlertRepository.findByCreatedAtBetween(start, end);

        return alerts.stream().map(alert -> {
            OperationLogDTO dto = new OperationLogDTO();
            dto.setType("STOCK_ALERT");

            // Resolve product name from itemId
            String productName = productRepository.findById(alert.getItemId())
                    .map(ChemicalProduct::getName)
                    .orElse("Unknown Product");

            // Resolved status
            boolean isResolved = Boolean.TRUE.equals(alert.getResolved());
            String resolvedBy = isResolved && alert.getResolvedBy() != null
                    ? alert.getResolvedBy().getName()
                    : "N/A";

            // Build description
            StringBuilder description = new StringBuilder();
            description.append("Stock alert for product: '")
                    .append(productName)
                    .append("' | Resolved: ")
                    .append(isResolved ? "true" : "false");

            if (isResolved) {
                description.append(" | Resolved by: ").append(resolvedBy);
            }

            dto.setDescription(description.toString());

            dto.setTimestamp(alert.getCreatedAt().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDateTime());

            dto.setPerformedBy(isResolved ? resolvedBy : "System");

            return dto;
        }).collect(Collectors.toList());
    }


    public byte[] generatePdfReport(List<OperationLogDTO> logs) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4, 40, 40, 50, 50); // add margins
            PdfWriter.getInstance(document, baos);

            ClassLoader classLoader = getClass().getClassLoader();
            URL imageUrl = classLoader.getResource("static/lastlast.png");

            if (imageUrl == null) {
                throw new FileNotFoundException("Logo image not found in classpath: static/logolast.png");
            }

            Image logo1 = Image.getInstance(imageUrl);

            logo1.scaleAbsolute(150, 150);
            logo1.setAlignment(Image.ALIGN_LEFT | Image.ALIGN_LEFT);

            document.open();
            PdfPTable logoTable = new PdfPTable(1);
            logoTable.setWidthPercentage(100);
            logoTable.setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell logoCell1 = new PdfPCell();
            logoCell1.setBorder(Rectangle.NO_BORDER);
            logoCell1.setHorizontalAlignment(Element.ALIGN_LEFT);
            logoCell1.setVerticalAlignment(Element.ALIGN_LEFT);
            logoCell1.setPadding(0f);
            logoCell1.addElement(logo1);
            logoTable.addCell(logoCell1);
            logoTable.setSpacingAfter(20f);
            document.add(logoTable);









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
            titleTable.setSpacingBefore(20f);
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



// Add some space before the signature line
            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);

// Create a paragraph for the signature line
            Font signatureFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 14, Color.BLACK);
            Paragraph signature = new Paragraph("Signature de chef de departement : ", signatureFont);
            Paragraph sign = new Paragraph("__________________________", signatureFont);
            signature.setAlignment(Element.ALIGN_RIGHT);
            sign.setAlignment(Element.ALIGN_RIGHT);
            signature.setSpacingBefore(40f); // space from the table above

            document.add(signature);
            document.add(sign);
            document.close();


            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF report", e);
        }
    }
}
