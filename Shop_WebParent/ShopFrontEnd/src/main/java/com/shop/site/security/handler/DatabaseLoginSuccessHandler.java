package com.shop.site.security.handler;

import com.shop.model.AuthenticationType;
import com.shop.site.country.CountryNotFoundException;
import com.shop.site.customer.CustomerUserDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class DatabaseLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final CustomerOAuth2Service service;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        var userDetails = (CustomerUserDetails) authentication.getPrincipal();
        service.updateAuthenticationType(AuthenticationType.DATABASE, userDetails.getCustomer().getId());

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
