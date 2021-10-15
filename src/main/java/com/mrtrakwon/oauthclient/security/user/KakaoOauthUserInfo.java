package com.mrtrakwon.oauthclient.security.user;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class KakaoOauthUserInfo implements OauthUserInfo{

	private final Map<String, Object> attributes;
	public KakaoOauthUserInfo(Map<String, Object> attributes) {
		this.attributes = Collections.unmodifiableMap(attributes);
	}

	@Override
	public String getId() {
		return this.attributes.get("id").toString();
	}

	@Override
	public String getName() {
		Map<String, Object> properties =  (Map<String, Object>)this.attributes.get("properties");
		if (properties == null) {
			return null;
		}
		return  properties.get("nickname").toString();

	}

	@Override
	public String getEmail() {
		Map<String, Object> kakaoAccount =  (Map<String, Object>)this.attributes.get("kakao_account");

		if (kakaoAccount == null) {
			return null;
		}

		return kakaoAccount.get("email").toString();
	}

	@Override
	public String getProviderId() {
		return ProviderType.KAKAO.name();
	}
}
