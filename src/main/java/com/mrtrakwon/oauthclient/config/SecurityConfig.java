package com.mrtrakwon.oauthclient.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.header.writers.frameoptions.WhiteListedAllowFromStrategy;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

import com.mrtrakwon.oauthclient.security.services.CustomOauth2UserService;
import com.mrtrakwon.oauthclient.security.repositories.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.mrtrakwon.oauthclient.security.handlers.FailureHandler;
import com.mrtrakwon.oauthclient.security.handlers.SuccessHandler;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOauth2UserService customOauth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                    .cors()
                .and()
                    .csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/h2-console/**").permitAll()
                .and()
                    .headers()
                        .frameOptions().sameOrigin()
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
                    .successHandler(new SuccessHandler())
                    .failureHandler(new FailureHandler());
    }


    @Bean
    public AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }
}
