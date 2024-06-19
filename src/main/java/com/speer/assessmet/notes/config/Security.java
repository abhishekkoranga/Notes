package com.speer.assessmet.notes.config;

import com.speer.assessmet.notes.service.AuthnService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@Slf4j
@RequiredArgsConstructor
@EnableMethodSecurity
public class Security {

    private static final List<String> EXCLUDE_URLS = List.of("/api/auth/signup", "/api/auth/login");

    @Autowired
    AuthnService authnService;

//    @Bean
//    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
//        http
//                .csrf(ServerHttpSecurity.CsrfSpec::disable)
//                .authorizeExchange();
//
//        return http.build();
//
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .addFilterBefore(new TokenAuthenticationFilter(authnService), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authz -> authz.requestMatchers("/api/auth/signup", "/api/auth/login" ).permitAll())
                .authorizeHttpRequests(authz -> authz.anyRequest().authenticated());


        return http.build();
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(authnService);
    }

//    @Override
//    public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
//
//        if (bypassUrls(exchange.getRequestURI().toString())) return;
//
//       if (exchange.getRequestHeaders().containsKey("Authorization") && exchange.getRequestHeaders().get("Authorization").get(0).startsWith("Basic")) {
//           String base64Encoded  = exchange.getRequestHeaders().get("Authorization").get(0).substring("Basic".length()-1);
//           byte[] byteDecoded = Base64.getDecoder().decode(base64Encoded);
//           String strDecode = new String(byteDecoded, StandardCharsets.UTF_8);
//           final String[] values = strDecode.split(":", 2);
//
//           if (authnService.validCredential(values[0], values[1])) {
//               chain.doFilter(exchange);
//               return;
//           }
//       }
//
//       exchange.sendResponseHeaders(400, 0);
//
//    }

    private boolean bypassUrls(String url) {
        return EXCLUDE_URLS.contains(url);
    }

}
