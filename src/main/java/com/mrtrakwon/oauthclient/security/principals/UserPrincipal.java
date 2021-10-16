package com.mrtrakwon.oauthclient.security.principals;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.mrtrakwon.oauthclient.domain.user.User;
import com.mrtrakwon.oauthclient.security.oauth2.authuserinfo.ProviderId;

public class UserPrincipal implements OAuth2User, UserDetails {

	private final long id;
	private final String name;
	private final String email;
	private final String password;
	private final ProviderId providerId;
	private Map<String, Object> attributes;
	private Collection<? extends GrantedAuthority> authorities;

	public UserPrincipal(long id, String name, String email,
		String password, ProviderId providerId,
		Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.providerId = providerId;
		this.authorities = authorities;
	}

	public static UserPrincipal create(User user, Map<String, Object> attributes) {
		UserPrincipal userPrincipal = UserPrincipal.create(user);
		userPrincipal.setAttributes(attributes);
		return userPrincipal;
	}


	public static UserPrincipal create(User user) {
		List<GrantedAuthority> authorities = Collections.
			singletonList(new SimpleGrantedAuthority("ROLE_USER"));

		return new UserPrincipal(
			user.getId(),
			user.getEmail(),
			user.getName(),
			user.getPassword(),
			ProviderId.valueOf(user.getProviderId().toUpperCase()),
			authorities
		);
	}
	private void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	public Map<String, Object> toClaims() {
		return Map.of(
			"email", this.email,
			"name", this.name,
			"providerId", this.providerId.name()
		);
	}

	@Override
	public Map<String, Object> getAttributes() {
		return this.attributes;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public String getName() {
		return Long.toString(this.id);
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
