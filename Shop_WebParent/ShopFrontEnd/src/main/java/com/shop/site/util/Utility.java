package com.shop.site.util;

import com.shop.site.repository.customer.EmailSettingBag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

public class Utility {

    public static String getSiteURL(HttpServletRequest request) {
        return request.getRequestURL().toString().replace(request.getServletPath(), "");
    }

    public static JavaMailSenderImpl prepareMailSender(EmailSettingBag settings) {
        var properties = new Properties();
        properties.setProperty("mail.smtp.auth", settings.getSMTPAuth());
        properties.setProperty("mail.smtp.starttls.enable", settings.getSMTPSecured());

        var sender = new JavaMailSenderImpl();
        sender.setHost(settings.getHost());
        sender.setPort(settings.getPort());
        sender.setUsername(settings.getUsername());
        sender.setPassword(settings.getPassword());
        sender.setJavaMailProperties(properties);

        return sender;
    }
}
