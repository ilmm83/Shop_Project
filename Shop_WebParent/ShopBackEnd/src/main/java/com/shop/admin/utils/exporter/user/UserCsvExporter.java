package com.shop.admin.utils.exporter.user;

import java.io.IOException;
import java.util.List;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shop.admin.utils.exporter.AbstractExporter;
import com.shop.model.User;

import jakarta.servlet.http.HttpServletResponse;

public class UserCsvExporter extends AbstractExporter {

  public void export(List<User> users, HttpServletResponse response) throws IOException {
    setResponseHeader(response, ".csv", "text/csv", "user_");

    ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

    String[] csvHeader = { "User ID", "E-mail", "First Name", "Last Name", "Roles", "Enabled" };
    String[] fieldMapping = { "id", "email", "firstName", "lastName", "roles", "enabled" };

    csvWriter.writeHeader(csvHeader);

    for (var user : users)
      csvWriter.write(user, fieldMapping);

    csvWriter.close();
  }
}
