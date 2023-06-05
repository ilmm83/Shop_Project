package com.shop.admin.utils.exporter.user;

import com.common.model.User;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.shop.admin.utils.exporter.AbstractExporter;
import jakarta.servlet.http.HttpServletResponse;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class UserPDFExporter extends AbstractExporter {

  public void export(List<User> users, HttpServletResponse response) throws IOException {
    setResponseHeader(response, ".pdf", "application/pdf", "user_");

    Document doc = new Document(PageSize.A4);
    PdfWriter.getInstance(doc, response.getOutputStream());
    doc.open();

    com.lowagie.text.Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
    font.setSize(18);
    font.setColor(Color.BLACK);

    Paragraph paragraph = new Paragraph("List of Users", font);
    paragraph.setAlignment(Element.ALIGN_CENTER);

    doc.add(paragraph);

    PdfPTable table = new PdfPTable(6);
    table.setWidthPercentage(100f);
    table.setSpacingBefore(10);
    table.setWidths(new float[] { 1.2f, 4f, 3f, 3f, 3f, 1.7f });

    writeTableHeader(table);
    writeTableData(table, users);

    doc.add(table);

    doc.close();
  }

  private void writeTableData(PdfPTable table, List<User> users) {
    PdfPCell cell = new PdfPCell();
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    cell.setPadding(5);

    for (var user : users) {
      cell.setPhrase(new Phrase(String.valueOf(user.getId())));
      table.addCell(cell);

      cell.setPhrase(new Phrase(user.getEmail()));
      table.addCell(cell);

      cell.setPhrase(new Phrase(user.getFirstName()));
      table.addCell(cell);

      cell.setPhrase(new Phrase(user.getLastName()));
      table.addCell(cell);

      cell.setPhrase(new Phrase(user.getRoles().toString()));
      table.addCell(cell);

      cell.setPhrase(new Phrase(user.getEnabled().toString()));
      table.addCell(cell);
    }
  }

  private void writeTableHeader(PdfPTable table) {
    PdfPCell cell = new PdfPCell();
    cell.setBackgroundColor(Color.GRAY);
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    cell.setPadding(5);

    com.lowagie.text.Font font = FontFactory.getFont(FontFactory.HELVETICA);
    font.setColor(Color.WHITE);

    cell.setPhrase(new Phrase("User ID", font));
    table.addCell(cell);

    cell.setPhrase(new Phrase("Email", font));
    table.addCell(cell);

    cell.setPhrase(new Phrase("First Name", font));
    table.addCell(cell);

    cell.setPhrase(new Phrase("Last Name", font));
    table.addCell(cell);

    cell.setPhrase(new Phrase("Roles", font));
    table.addCell(cell);

    cell.setPhrase(new Phrase("Enabled", font));
    table.addCell(cell);
  }
}
