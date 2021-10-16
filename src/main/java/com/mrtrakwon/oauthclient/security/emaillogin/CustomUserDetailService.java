package com.mrtrakwon.oauthclient.security.emaillogin;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mrtrakwon.oauthclient.domain.user.User;
import com.mrtrakwon.oauthclient.domain.user.UserRepository;
import com.mrtrakwon.oauthclient.security.principals.UserPrincipal;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		User user = userRepository.findByEmail(email)
			.orElseThrow(() -> new RuntimeException("못찾음"));

		return UserPrincipal.create(user);
	}
}
