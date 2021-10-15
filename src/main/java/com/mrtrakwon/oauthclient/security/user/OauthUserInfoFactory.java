package com.mrtrakwon.oauthclient.security.user;

import java.util.Map;

public class OauthUserInfoFactory {

	public static OauthUserInfo getUserInfo(ProviderType providerType, Map<String, Object> attributes) {
		switch (providerType) {
			case KAKAO: return new KakaoOauthUserInfo(attributes);
		}

		throw new RuntimeException("not fount provider");
	}
}
