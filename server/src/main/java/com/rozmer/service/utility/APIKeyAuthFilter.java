package com.rozmer.service.utility;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class APIKeyAuthFilter extends AbstractAuthenticationProcessingFilter {

    private final String headerName;

    public APIKeyAuthFilter(String headerName) {
        super("/**");
        this.headerName = headerName;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        String apiKey = request.getHeader(headerName);
        if (apiKey == null) {
            apiKey = "";
        }

        APIKeyAuthenticationToken token = new APIKeyAuthenticationToken(apiKey);
        return getAuthenticationManager().authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            FilterChain chain, Authentication authResult) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        chain.doFilter(request, response);
    }

    public static class APIKeyAuthenticationToken extends AbstractAuthenticationToken {
        private final String apiKey;

        public APIKeyAuthenticationToken(String apiKey) {
            super(null);
            this.apiKey = apiKey;
            setAuthenticated(false);
        }

        @Override
        public Object getCredentials() {
            return null;
        }

        @Override
        public Object getPrincipal() {
            return apiKey;
        }
    }
}
