package com.shop.admin.utils.exporter;

import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.shop.model.User;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

public class UserExcelExporter extends AbstractExporter {

  private XSSFWorkbook workbook;
  private XSSFSheet sheet;

  public UserExcelExporter() {
    workbook = new XSSFWorkbook();
  }

  public void export(List<User> users, HttpServletResponse response) throws IOException {
    setResponseHeader(response, ".xlsx", "application/octet-stream");

    writeHeaderLine();
    writeDataLines(users);

    ServletOutputStream os = response.getOutputStream();
    workbook.write(os);
    workbook.close();
  }

  private void createCell(XSSFRow row, int columnIndx, Object value, CellStyle style) {
    XSSFCell cell = row.createCell(columnIndx);
    sheet.autoSizeColumn(columnIndx);

    if (value instanceof Integer)
      cell.setCellValue((Integer) value);
    else if (value instanceof Long)
      cell.setCellValue((Long) value);
    else if (value instanceof Boolean)
      cell.setCellValue((Boolean) value);
    else
      cell.setCellValue((String) value);

    cell.setCellStyle(style);
  }

  private void writeHeaderLine() {
    sheet = workbook.createSheet("Users");
    XSSFRow row = sheet.createRow(0);

    XSSFCellStyle style = setCellStyle(true, 14);

    createCell(row, 0, "User ID", style);
    createCell(row, 1, "E-mail", style);
    createCell(row, 2, "First Name", style);
    createCell(row, 3, "Last Name", style);
    createCell(row, 4, "Roles", style);
    createCell(row, 5, "Enabled", style);
  }

  private XSSFCellStyle setCellStyle(boolean isBold, int fontHeight) {
    XSSFCellStyle style = workbook.createCellStyle();
    XSSFFont font = workbook.createFont();
    font.setBold(isBold);
    font.setFontHeight(fontHeight);
    style.setFont(font);

    return style;
  }

  private void writeDataLines(List<User> users) {
    int rowIndx = 1;
    for (var user : users) {
      XSSFRow row = sheet.createRow(rowIndx++);

      int colIndx = 0;
      XSSFCellStyle style = setCellStyle(false, 12);
      createCell(row, colIndx++, user.getId(), style);
      createCell(row, colIndx++, user.getEmail(), style);
      createCell(row, colIndx++, user.getFirstName(), style);
      createCell(row, colIndx++, user.getLastName(), style);
      createCell(row, colIndx++, user.getRoles().toString(), style);
      createCell(row, colIndx++, user.getEnabled(), style);
    }
  }
}
