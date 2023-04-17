package com.shop.site.setting;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class SettingFilter implements Filter {

    @Autowired
    private SettingService service;


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        var servletRequest = (HttpServletRequest) request;
        var url = servletRequest.getRequestURL().toString();

        if (url.endsWith(".css") || url.endsWith(".js") || url.endsWith(".jpg") || url.endsWith(".png")) {
            filterChain.doFilter(request, response);
            return;
        }

        service.getGeneralAndCurrencySettings()
                .forEach(setting -> request.setAttribute(setting.getKey(), setting.getValue()));

        filterChain.doFilter(request, response);
    }
}
