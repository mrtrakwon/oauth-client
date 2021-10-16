package com.mrtrakwon.oauthclient.security.oauth2.authuserinfo;

import java.util.Map;

public interface  OauthUserInfo {
	String getId();
	String getName();
	String getEmail();
	String getProviderId();
}
