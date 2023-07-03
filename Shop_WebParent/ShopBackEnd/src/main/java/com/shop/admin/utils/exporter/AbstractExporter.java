package com.shop.admin.utils.exporter;

import jakarta.servlet.http.HttpServletResponse;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class AbstractExporter {

    public void setResponseHeader(HttpServletResponse response, String fileExtension, String contentType, String fileNameStartsWith) {
        var dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        var timestamp = dateFormat.format(new Date());
        var fileName = fileNameStartsWith + timestamp + fileExtension;

        response.setContentType(contentType);

        var headerKey = "Content-Disposition";
        var headerValue = "attachment; filename=" + fileName;

        response.setHeader(headerKey, headerValue);
    }
}
