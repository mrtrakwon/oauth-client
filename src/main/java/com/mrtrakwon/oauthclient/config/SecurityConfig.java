package com.mrtrakwon.oauthclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import com.mrtrakwon.oauthclient.security.normal.CustomUserDetailService;
import com.mrtrakwon.oauthclient.security.oauth2.CustomOauth2UserService;
import com.mrtrakwon.oauthclient.security.oauth2.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.mrtrakwon.oauthclient.security.handlers.LoginFailureHandler;
import com.mrtrakwon.oauthclient.security.handlers.LoginSuccessHandler;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // social login
    private final CustomOauth2UserService customOauth2UserService;
    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailureHandler loginFailureHandler;

    // email login
    private final CustomUserDetailService customUserDetailService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                    .cors()

                .and()
                    .csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/h2-console/**").permitAll()
                    .anyRequest().authenticated()
                .and()
                    .headers()
                        .frameOptions().sameOrigin()
                .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                    .formLogin()
                    .loginProcessingUrl("/authenticate")
                        .permitAll()
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .successHandler(loginSuccessHandler)
                    .failureHandler(loginFailureHandler)

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
                    .successHandler(loginSuccessHandler)
                    .failureHandler(loginFailureHandler);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailService)
            .passwordEncoder(bCryptPasswordEncoder());
    }

    @Bean BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }
}
