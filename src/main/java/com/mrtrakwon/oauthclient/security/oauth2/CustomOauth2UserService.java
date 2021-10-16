package com.mrtrakwon.oauthclient.security.oauth2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.mrtrakwon.oauthclient.domain.user.User;
import com.mrtrakwon.oauthclient.domain.user.UserRepository;
import com.mrtrakwon.oauthclient.security.oauth2.authuserinfo.OauthUserInfo;
import com.mrtrakwon.oauthclient.security.oauth2.authuserinfo.OauthUserInfoFactory;
import com.mrtrakwon.oauthclient.security.oauth2.authuserinfo.ProviderId;
import com.mrtrakwon.oauthclient.security.principals.UserPrincipal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    private final static Logger logger = LoggerFactory.getLogger(CustomOauth2UserService.class);
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User auth2User = super.loadUser(userRequest);

        final String providerId = userRequest.getClientRegistration().getRegistrationId();
        ProviderId providerType = ProviderId.valueOf(providerId.toUpperCase());

        OauthUserInfo userInfo = OauthUserInfoFactory.getUserInfo(providerType, auth2User.getAttributes());
        User user = User.builder()
            .name(userInfo.getName())
            .email(userInfo.getEmail())
            .providerId(userInfo.getProviderId())
            .build();
        userRepository.save(user);

        return UserPrincipal.create(user, auth2User.getAttributes());
    }
}
