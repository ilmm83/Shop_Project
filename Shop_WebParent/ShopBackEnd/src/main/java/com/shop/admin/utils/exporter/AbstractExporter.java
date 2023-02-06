package com.shop.admin.utils.exporter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import jakarta.servlet.http.HttpServletResponse;

public abstract class AbstractExporter {

  public void setResponseHeader(HttpServletResponse response, String fileExtension, String contentType, String fileNameStarter) {
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
    String timestamp = dateFormat.format(new Date());
    String fileName = fileNameStarter + timestamp + fileExtension;

    response.setContentType(contentType);

    String headerKey = "Content-Disposition";
    String headerValue = "attachment; filename=" + fileName;
    response.setHeader(headerKey, headerValue);
  }
}
