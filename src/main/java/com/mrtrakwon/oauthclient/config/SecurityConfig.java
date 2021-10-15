package com.mrtrakwon.oauthclient.config;

import com.mrtrakwon.oauthclient.security.*;
import com.mrtrakwon.oauthclient.security.handler.FailureHandler;
import com.mrtrakwon.oauthclient.security.handler.SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;


@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOauth2UserService customOauth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                    .cors()
                .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .oauth2Login()
                    .authorizationEndpoint()
                    .baseUri("/oauth2/authorization") // 설정하지 않을경우 디폴트 경로 = /oauth2/authorization
                    .authorizationRequestRepository(this.authorizationRequestRepository()) // 설정하지 않을 경우 디폴트 레포지토리 = HttpSessionOAuth2AuthorizationRequestRepository
                .and()
                    .redirectionEndpoint()
                    .baseUri("/*/oauth2/code/*") // 설정하지 않을경우 디폴트 경로 패턴 = /login/oauth2/code/*
                .and()
                    .userInfoEndpoint()
                    .userService(this.customOauth2UserService) // 설정하지 않을 경우 디폴트 클래스 = DefaultOAuth2UserService
                .and()
                    .successHandler(authenticationSuccessHandler())
                    .failureHandler(authenticationFailureHandler());

    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new SuccessHandler();
    }
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new FailureHandler();
    }
    @Bean
    public AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }
}
