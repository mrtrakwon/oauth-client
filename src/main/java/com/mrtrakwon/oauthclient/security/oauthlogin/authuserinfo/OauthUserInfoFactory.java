package com.mrtrakwon.oauthclient.security.oauthlogin.authuserinfo;

import java.util.Map;

public class OauthUserInfoFactory {

	public static OauthUserInfo getUserInfo(ProviderId providerType, Map<String, Object> attributes) {
		switch (providerType) {
			case KAKAO: return new KakaoOauthUserInfo(attributes);
		}

		throw new RuntimeException("not fount provider");
	}
}
