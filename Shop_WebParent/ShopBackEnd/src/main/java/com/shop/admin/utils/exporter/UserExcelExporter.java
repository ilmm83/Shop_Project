package com.shop.admin.utils.exporter;

import com.common.model.User;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.List;

public class UserExcelExporter extends AbstractExporter {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;


    public UserExcelExporter() {
        workbook = new XSSFWorkbook();
    }

    public void export(List<User> users, HttpServletResponse response) throws IOException {
        setResponseHeader(response, ".xlsx", "application/octet-stream", "user_");

        writeHeaderLine();
        writeDataLines(users);

        var os = response.getOutputStream();

        workbook.write(os);
        workbook.close();
    }

    private void createCell(XSSFRow row, int columnIndex, Object value, CellStyle style) {
        var cell = row.createCell(columnIndex);

        sheet.autoSizeColumn(columnIndex);

        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else {
            cell.setCellValue((String) value);
        }

        cell.setCellStyle(style);
    }

    private void writeHeaderLine() {
        sheet = workbook.createSheet("Users");

        var row = sheet.createRow(0);
        var style = setCellStyle(true, 14);

        createCell(row, 0, "User ID", style);
        createCell(row, 1, "E-mail", style);
        createCell(row, 2, "First Name", style);
        createCell(row, 3, "Last Name", style);
        createCell(row, 4, "Roles", style);
        createCell(row, 5, "Enabled", style);
    }

    private XSSFCellStyle setCellStyle(boolean isBold, int fontHeight) {
        var style = workbook.createCellStyle();
        var font = workbook.createFont();
        font.setBold(isBold);
        font.setFontHeight(fontHeight);

        style.setFont(font);

        return style;
    }

    private void writeDataLines(List<User> users) {
        var rowIndex = 1;

        for (var user : users) {
            var row = sheet.createRow(rowIndex++);

            var columnIndex = 0;
            var style = setCellStyle(false, 12);
            createCell(row, columnIndex++, user.getId(), style);
            createCell(row, columnIndex++, user.getEmail(), style);
            createCell(row, columnIndex++, user.getFirstName(), style);
            createCell(row, columnIndex++, user.getLastName(), style);
            createCell(row, columnIndex++, user.getRoles().toString(), style);
            createCell(row, columnIndex++, user.getEnabled(), style);
        }
    }
}
