package com.ajmayen.smartdepo.service;

import com.ajmayen.smartdepo.model.Chalan;
import com.ajmayen.smartdepo.model.ChalanItem;
import com.ajmayen.smartdepo.model.ChalanType;
import com.ajmayen.smartdepo.model.Dealer;
import com.ajmayen.smartdepo.model.DealerDeposit;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class PdfGenerationService {

    private final DealerDepositService dealerDepositService;

    public PdfGenerationService(DealerDepositService dealerDepositService) {
        this.dealerDepositService = dealerDepositService;
    }

    public byte[] generateChalanPdf(Chalan chalan) throws DocumentException {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, out);
        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.BLACK);
        Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.BLACK);
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Color.BLACK);

        // Title
        Paragraph title = new Paragraph(
                chalan.getType() == ChalanType.OUTGOING ? "OUTGOING CHALAN" : "INCOMING CHALAN",
                titleFont
        );
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        // Header Information
        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);
        headerTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        if (chalan.getType() == ChalanType.OUTGOING) {
            Dealer dealer = chalan.getDealer();
            headerTable.addCell(new Phrase("Dealer Name: " + (dealer != null ? dealer.getName() : "N/A"), boldFont));
            headerTable.addCell(new Phrase("Chalan No: " + chalan.getChalanNo(), normalFont));

            headerTable.addCell(new Phrase("Address: " + (dealer != null ? dealer.getAddress() : "N/A"), normalFont));
            headerTable.addCell(new Phrase("Date: " + chalan.getChalanDate(), normalFont));

            headerTable.addCell(new Phrase("Phone: " + (dealer != null ? dealer.getPhone() : "N/A"), normalFont));
            headerTable.addCell(new Phrase("", normalFont));
        } else {
            headerTable.addCell(new Phrase("Track No: " + (chalan.getTrackNo() != null ? chalan.getTrackNo() : "N/A"), boldFont));
            headerTable.addCell(new Phrase("Chalan No: " + chalan.getChalanNo(), normalFont));

            headerTable.addCell(new Phrase("", normalFont));
            headerTable.addCell(new Phrase("Date: " + chalan.getChalanDate(), normalFont));
        }

        document.add(headerTable);
        document.add(new Paragraph(" "));

        // Item Table
        PdfPTable itemTable = new PdfPTable(7);
        itemTable.setWidthPercentage(100);
        itemTable.setWidths(new float[]{1f, 3f, 1.5f, 1.5f, 1.5f, 2f, 1.5f});
        itemTable.setSpacingBefore(10);

        // Table Headers
        String[] headers = {"SL", "Product", "Pcs/Ctn", "Price", "Ctn Qty", "Total", "Free"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, boldFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(Color.LIGHT_GRAY);
            cell.setPadding(5);
            cell.setBorder(Rectangle.BOX);
            itemTable.addCell(cell);
        }

        // Table Rows
        int sl = 1;
        for (ChalanItem item : chalan.getItems()) {
            itemTable.addCell(createCell(String.valueOf(sl++), Element.ALIGN_CENTER));
            itemTable.addCell(createCell(item.getProduct().getName(), Element.ALIGN_LEFT));
            itemTable.addCell(createCell(String.valueOf(item.getProduct().getPerCartonPieces()), Element.ALIGN_CENTER));
            itemTable.addCell(createCell(String.format("%.2f", item.getPricePerCarton()), Element.ALIGN_RIGHT));
            itemTable.addCell(createCell(String.valueOf(item.getCartonQty()), Element.ALIGN_CENTER));
            itemTable.addCell(createCell(String.format("%.2f", item.getTotalPrice()), Element.ALIGN_RIGHT));
            itemTable.addCell(createCell(item.getFreeCartonQty() != null ? String.valueOf(item.getFreeCartonQty()) : "-", Element.ALIGN_CENTER));
        }

        // Subtotal row
        PdfPCell subTotalLabelCell = new PdfPCell(new Phrase("Subtotal", boldFont));
        subTotalLabelCell.setColspan(5);
        subTotalLabelCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        subTotalLabelCell.setPadding(5);
        subTotalLabelCell.setBorder(Rectangle.BOX);
        itemTable.addCell(subTotalLabelCell);

        PdfPCell subTotalValueCell = new PdfPCell(new Phrase(String.format("%.2f", chalan.getSubTotal()), boldFont));
        subTotalValueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        subTotalValueCell.setPadding(5);
        subTotalValueCell.setBorder(Rectangle.BOX);
        itemTable.addCell(subTotalValueCell);
        
        PdfPCell emptyCell = new PdfPCell(new Phrase(""));
        emptyCell.setBorder(Rectangle.BOX);
        itemTable.addCell(emptyCell);

        document.add(itemTable);

        // Deposit calculations for Outgoing Chalan
        if (chalan.getType() == ChalanType.OUTGOING && chalan.getDealer() != null) {
            document.add(new Paragraph(" "));
            
            Paragraph depositTitle = new Paragraph("Deposit History", boldFont);
            depositTitle.setSpacingAfter(10);
            document.add(depositTitle);

            // Fetch deposits
            List<DealerDeposit> deposits = dealerDepositService.getIndividualDeposit(chalan.getDealer().getId());
            
            if (deposits != null && !deposits.isEmpty()) {
                PdfPTable depositTable = new PdfPTable(2);
                depositTable.setWidthPercentage(50);
                depositTable.setHorizontalAlignment(Element.ALIGN_LEFT);

                PdfPCell dateHeader = new PdfPCell(new Phrase("Date", boldFont));
                dateHeader.setBackgroundColor(Color.LIGHT_GRAY);
                dateHeader.setBorder(Rectangle.BOX);
                depositTable.addCell(dateHeader);

                PdfPCell amountHeader = new PdfPCell(new Phrase("Amount", boldFont));
                amountHeader.setBackgroundColor(Color.LIGHT_GRAY);
                amountHeader.setBorder(Rectangle.BOX);
                depositTable.addCell(amountHeader);

                double totalDeposit = 0;
                for (DealerDeposit deposit : deposits) {
                    depositTable.addCell(createCell(deposit.getDepositDate().toString(), Element.ALIGN_LEFT));
                    depositTable.addCell(createCell(String.format("%.2f", deposit.getAmount()), Element.ALIGN_RIGHT));
                    totalDeposit += deposit.getAmount();
                }

                PdfPCell totalLabel = new PdfPCell(new Phrase("Total Deposit", boldFont));
                totalLabel.setBorder(Rectangle.BOX);
                depositTable.addCell(totalLabel);

                PdfPCell totalVal = new PdfPCell(new Phrase(String.format("%.2f", totalDeposit), boldFont));
                totalVal.setHorizontalAlignment(Element.ALIGN_RIGHT);
                totalVal.setBorder(Rectangle.BOX);
                depositTable.addCell(totalVal);

                document.add(depositTable);
            } else {
                document.add(new Paragraph("No deposits found.", normalFont));
            }

            document.add(new Paragraph(" "));

            // Dues Summary
            PdfPTable dueTable = new PdfPTable(2);
            dueTable.setWidthPercentage(40);
            dueTable.setHorizontalAlignment(Element.ALIGN_LEFT);
            dueTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);

            dueTable.addCell(new Phrase("Chalan Subtotal:", boldFont));
            dueTable.addCell(new Phrase(String.format("%.2f", chalan.getSubTotal()), normalFont));

            if (chalan.getDealerDue() != null && chalan.getDealerDue() > 0) {
                dueTable.addCell(new Phrase("Dealer Due Amount:", boldFont));
                dueTable.addCell(new Phrase(String.format("%.2f", chalan.getDealerDue()), normalFont));
            } else if (chalan.getDepoDue() != null && chalan.getDepoDue() > 0) {
                dueTable.addCell(new Phrase("Depo Due Amount:", boldFont));
                dueTable.addCell(new Phrase(String.format("%.2f", chalan.getDepoDue()), normalFont));
            } else {
                dueTable.addCell(new Phrase("Due Amount:", boldFont));
                dueTable.addCell(new Phrase("0.00", normalFont));
            }
            
            document.add(dueTable);
        }

        document.close();
        return out.toByteArray();
    }

    private PdfPCell createCell(String text, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA, 10)));
        cell.setHorizontalAlignment(alignment);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(5);
        cell.setBorder(Rectangle.BOX);
        return cell;
    }
}
