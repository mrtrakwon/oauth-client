package com.mrtrakwon.oauthclient.security.user;

public interface OauthUserInfo {
	String getId();
	String getName();
	String getEmail();
	String getProviderId();
}
