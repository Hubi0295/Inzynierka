package com.example.productservice.entity;

import com.example.product.entity.ProductInfoDTO;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import java.io.InputStream;

public class DocumentWord {
    public static XWPFDocument createDocument(DocumentDTO documentDTO) {
        try{
            XWPFDocument document = new XWPFDocument();

            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();

            try (InputStream is = DocumentWord.class.getResourceAsStream("/static/logo.jpg")) {
                run.addPicture(
                        is,
                        Document.PICTURE_TYPE_JPEG,
                        "logo.jpg",
                        Units.toEMU(200),
                        Units.toEMU(150)
                );
            }


            XWPFParagraph title = document.createParagraph();
            title.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun titleRun = title.createRun();
            titleRun.setText(documentDTO.getTyp());
            titleRun.setBold(true);
            titleRun.setFontSize(18);

            XWPFParagraph subtitle = document.createParagraph();
            subtitle.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun subtitleRun = subtitle.createRun();
            String typOfDocs = "";
            switch (documentDTO.getTyp()) {
                case "RECEIPT" -> typOfDocs ="Przyjęcie magazynowe";
                case "EDITRECEIPT" -> typOfDocs ="Korekta przyjęcia magazynowe";
                case "ISSUE" -> typOfDocs ="Wydanie magazynowe";
                case "EDITISSUE" -> typOfDocs ="Korekta wydania magazynowe";
                default -> typOfDocs ="Dokument magazynowy";
            }
            subtitleRun.setText(typOfDocs);
            subtitleRun.setFontSize(14);

            XWPFTable table1 = document.createTable();
            setCellText(table1.getRow(0).getCell(0), "Typ", true);
            setCellText(table1.getRow(0).addNewTableCell(), typOfDocs, false);

            XWPFTableRow row1 = table1.createRow();
            setCellText(row1.getCell(0), "Data", true);
            setCellText(row1.getCell(1), documentDTO.getDate().toString(), false);

            XWPFTableRow row2 = table1.createRow();
            setCellText(row2.getCell(0), "Nazwa dokumentu", true);
            setCellText(row2.getCell(1), documentDTO.getDocumentName(), false);

            XWPFTableRow row3 = table1.createRow();
            setCellText(row3.getCell(0), "Kontrahent", true);
            setCellText(row3.getCell(1), documentDTO.getContractor().getName(), false);

            XWPFTableRow row4 = table1.createRow();
            setCellText(row4.getCell(0), "Przedstawiciel firmy", true);
            setCellText(row4.getCell(1), documentDTO.getUser().getName()+" "+documentDTO.getUser().getSurname(), false);


            XWPFParagraph header = document.createParagraph();
            XWPFRun headerRun = header.createRun();
            headerRun.setFontFamily("Courier New");
            headerRun.setFontSize(10);
            headerRun.setText(typOfDocs+" nazwa: "+documentDTO.getDocumentName());
            headerRun.setBold(true);

            XWPFParagraph header2 = document.createParagraph();
            XWPFRun headerRun2 = header2.createRun();
            headerRun2.setFontFamily("Courier New");
            headerRun2.setFontSize(10);
            headerRun2.setText(typOfDocs+" identyfikator: "+documentDTO.getDocumentUUID());
            headerRun2.setBold(true);


            XWPFTable table = document.createTable();
            XWPFTableRow headerRow = table.getRow(0);
            setCellText(headerRow.getCell(0), "Lp.", true);
            setCellText(headerRow.addNewTableCell(), "Nazwa produktu", true);
            setCellText(headerRow.addNewTableCell(), "Kategoria", true);
            setCellText(headerRow.addNewTableCell(), "Producent", true);
            setCellText(headerRow.addNewTableCell(), "Data Przyjazdu", true);
            setCellText(headerRow.addNewTableCell(), "Ilosc", true);
            int counter =1;
            for(ProductInfoDTO productInfoDTO: documentDTO.getProducts()){
                XWPFTableRow rowData1 = table.createRow();
                rowData1.getCell(0).setText(String.valueOf(counter));
                rowData1.getCell(1).setText(productInfoDTO.getName());
                rowData1.getCell(2).setText(productInfoDTO.getCategory().getName());
                rowData1.getCell(3).setText(productInfoDTO.getContractor());
                rowData1.getCell(4).setText(productInfoDTO.getUpdated_at().toString());
                rowData1.getCell(5).setText("1");
                counter++;
            }

            document.createParagraph().createRun().addBreak();
            XWPFTable table3 = document.createTable();
            XWPFTableRow rowSignTitle = table3.getRow(0);
            rowSignTitle.getCell(0).setText("Podpis przedstawiciela kontrahenta "+documentDTO.getContractor().getName());
            rowSignTitle.addNewTableCell().setText("Podpis "+documentDTO.getUser().getName()+" "+documentDTO.getUser().getSurname());

            XWPFTableRow rowSign = table3.createRow();
            rowSign.getCell(0).setText("     _________________________     ");
            rowSign.getCell(1).setText("     _________________________     ");

            System.out.println("Dokument "+typOfDocs+" utworzony.");
            return document;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void setCellText(XWPFTableCell cell, String text, boolean bold) {
        cell.removeParagraph(0);
        XWPFParagraph paragraph = cell.addParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run = paragraph.createRun();
        run.setText(text);
        run.setBold(bold);
    }
}
