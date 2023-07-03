package com.shop.admin.utils.exporter;

import com.common.model.Category;
import jakarta.servlet.http.HttpServletResponse;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.util.List;

public class CategoryCSVExporter extends AbstractExporter {

    public void export(List<Category> categories, HttpServletResponse response) throws IOException {
        setResponseHeader(response, ".csv", "text/csv", "categories_");

        var writer = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

        var csvHeader = new String[]{"ID", "Category Name", "Category Alias", "Enabled"};
        var fieldMapping = new String[]{"id", "name", "alias", "enabled"};

        writer.writeHeader(csvHeader);

        for (var category : categories) {
            writer.write(category, fieldMapping);
        }

        writer.close();
    }
}
