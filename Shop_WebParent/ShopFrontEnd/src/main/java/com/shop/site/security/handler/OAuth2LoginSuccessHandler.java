package com.shop.site.security.handler;

import com.shop.model.AuthenticationType;
import com.shop.site.security.oauth2.CustomerOAuth2User;
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
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final CustomerOAuth2Service service;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        var user = (CustomerOAuth2User) authentication.getPrincipal();

        var found = service.findByEmail(user.getEmail());
        if (found.isEmpty())
            service.addNewCustomerUponOAuthLogin(user.getName(), user.getEmail(), request.getLocale().getCountry());
        else
            service.updateAuthenticationType(AuthenticationType.GOOGLE, found.get().getId());

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
