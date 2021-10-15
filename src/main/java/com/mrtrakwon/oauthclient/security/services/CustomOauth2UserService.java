package com.mrtrakwon.oauthclient.security.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.mrtrakwon.oauthclient.domain.user.User;
import com.mrtrakwon.oauthclient.domain.user.UserRepository;
import com.mrtrakwon.oauthclient.security.user.OauthUserInfo;
import com.mrtrakwon.oauthclient.security.user.OauthUserInfoFactory;
import com.mrtrakwon.oauthclient.security.user.ProviderType;

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
        ProviderType providerType = ProviderType.valueOf(providerId.toUpperCase());

        OauthUserInfo userInfo = OauthUserInfoFactory.getUserInfo(providerType, auth2User.getAttributes());
        User user = User.builder()
            .name(userInfo.getName())
            .email(userInfo.getEmail())
            .providerId(userInfo.getProviderId())
            .build();
        userRepository.save(user);

        return auth2User;
    }
}
