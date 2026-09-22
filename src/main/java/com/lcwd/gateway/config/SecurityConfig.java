package com.lcwd.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.server.DefaultServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, ReactiveClientRegistrationRepository repo) {

        // Custom resolver to attach the Auth0 audience parameter
        DefaultServerOAuth2AuthorizationRequestResolver resolver =
                new DefaultServerOAuth2AuthorizationRequestResolver(repo);

        resolver.setAuthorizationRequestCustomizer(
                customizer -> customizer.additionalParameters(params -> params.put("audience", "http://localhost:8084"))
        );

        http
                .authorizeExchange()
                .anyExchange().authenticated()
                .and()
                .oauth2Login(oauth2 -> oauth2.authorizationRequestResolver(resolver))
                .oauth2Client()
                .and()
                .oauth2ResourceServer().jwt();

        return http.build();
    }
}