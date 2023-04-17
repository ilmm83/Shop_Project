package com.shop.admin.utils.exporter.customer;

import com.shop.admin.utils.exporter.AbstractExporter;
import com.shop.model.Customer;
import jakarta.servlet.http.HttpServletResponse;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.util.List;

public class CustomerCSVExporter extends AbstractExporter {

    public void export(List<Customer> customers, HttpServletResponse response) throws IOException {
        setResponseHeader(response, ".csv", "text/csv", "customers_");

        var csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

        var csvHeader = new String[]{"Customer ID", "First Name", "Last Name", "E-mail", "City", "State", "Country", "Enabled"};
        var fieldMapping = new String[]{"id", "firstName", "lastName", "email", "city", "state", "country", "enabled"};

        csvWriter.writeHeader(csvHeader);

        for (var customer : customers) csvWriter.write(customer, fieldMapping);
        csvWriter.close();
    }
}
