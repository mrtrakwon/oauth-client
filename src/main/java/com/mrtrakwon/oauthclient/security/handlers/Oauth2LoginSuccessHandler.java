package com.mrtrakwon.oauthclient.security.handlers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.mrtrakwon.oauthclient.security.oauth2.AuthTokenService;
import com.mrtrakwon.oauthclient.security.oauth2.principals.UserPrincipal;
import com.mrtrakwon.oauthclient.security.oauth2.token.AccessToken;
import com.mrtrakwon.oauthclient.security.oauth2.token.RefreshToken;

@Slf4j
@Component
@RequiredArgsConstructor
public class Oauth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(Oauth2LoginSuccessHandler.class);

    private final AuthTokenService authTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        logger.debug("success : 1");
        UserPrincipal principal = (UserPrincipal)authentication.getPrincipal();

        final AccessToken accessToken = authTokenService.generateAccessToken(principal);
        final RefreshToken refreshToken = authTokenService.generateRefreshToken(principal);
        Cookie accessTokenCookie = new Cookie("accessToken", accessToken.getValue());
        accessTokenCookie.setHttpOnly(true);

        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken.getValue());
        refreshTokenCookie.setHttpOnly(true);

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

        // response.sendRedirect("/");
    }
}
