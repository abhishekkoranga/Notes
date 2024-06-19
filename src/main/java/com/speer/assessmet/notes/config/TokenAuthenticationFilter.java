package com.speer.assessmet.notes.config;

import com.speer.assessmet.notes.service.AuthnService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final AuthnService authnService;

    public TokenAuthenticationFilter(AuthnService authnService) {
        this.authnService = authnService;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = extractAndParseToken(request);
        if (token != null) {
            Authentication authentication = new UsernamePasswordAuthenticationToken(token, null, null);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println("Valid token");
        }

        filterChain.doFilter(request, response);

    }

    private String extractAndParseToken(HttpServletRequest request) {
        // Implement logic to extract the token from the Authorization header
        // For example, request.getHeader("Authorization") and parse the token

        final String AUTHORIZATION_HEADER = "Authorization";
        if (request.getHeader(AUTHORIZATION_HEADER) != null && request.getHeader(AUTHORIZATION_HEADER).startsWith("Basic")) {
           String base64Encoded  = request.getHeader(AUTHORIZATION_HEADER).substring("Basic".length()+1);
           byte[] byteDecoded = Base64.getDecoder().decode(base64Encoded);
           String strDecode = new String(byteDecoded, StandardCharsets.UTF_8);
           final String[] values = strDecode.split(":", 2);

           if (authnService.validCredential(values[0], values[1])) {
               return values[0];
           }
       }

        return null;
    }

}