package com.shop.admin.utils.exporter.category;

import java.io.IOException;
import java.util.List;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shop.admin.utils.exporter.AbstractExporter;
import com.shop.model.Category;

import jakarta.servlet.http.HttpServletResponse;

public class CategoryCSVExporter extends AbstractExporter {

  public void export(List<Category> categories, HttpServletResponse response) throws IOException {
      setResponseHeader(response, ".csv", "text/csv", "categories_");

      ICsvBeanWriter writer = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

    String[] csvHeader = { "ID", "Category Name", "Category Alias", "Enabled" };
    String[] fieldMapping = { "id", "name", "alias", "enabled" };

    writer.writeHeader(csvHeader);

    for (var category : categories)
      writer.write(category, fieldMapping);

    writer.close();
  }
}
