package com.mrtrakwon.oauthclient.security.oauthlogin;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mrtrakwon.oauthclient.security.principals.UserPrincipal;
import com.mrtrakwon.oauthclient.security.oauthlogin.token.AccessToken;
import com.mrtrakwon.oauthclient.security.oauthlogin.token.RefreshToken;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class AuthTokenService {

	private final long accessTokenExpiration;
	private final long refreshTokenExpiration;
	private final SecretKey secretKey;
	private final SignatureAlgorithm defaultSignatureAlgorithm = SignatureAlgorithm.HS256;
	private final  Map<String, Object> defaultHeaders;

	public AuthTokenService(
		@Value("${jwt.auth.access-token.expiration}") long accessTokenExpiration,
		@Value("${jwt.auth.refresh-token.expiration}") long refreshTokenExpiration,
		@Value("${jwt.auth.secret-key}") String secretKey
		)
	{
		this.accessTokenExpiration = accessTokenExpiration;
		this.refreshTokenExpiration = refreshTokenExpiration;
		this.secretKey =  Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
		this.defaultHeaders = Map.of("alg", defaultSignatureAlgorithm.getValue());
	}

	public AccessToken generateAccessToken(UserPrincipal userPrincipal) {
		final String accessToken = generateToken(userPrincipal, this.accessTokenExpiration);
		return new AccessToken(accessToken);
	}

	public RefreshToken generateRefreshToken(UserPrincipal userPrincipal) {
		final String refreshToken = generateToken(userPrincipal, this.refreshTokenExpiration);
		return new RefreshToken(refreshToken);
	}

	private String generateToken(UserPrincipal userPrincipal, long expiration) {
		Date now = new Date();
		Date calcExpiration = new Date(now.getTime() + expiration);

		return Jwts.builder()
			.setHeader(this.defaultHeaders)
			.setSubject(userPrincipal.getName())
			.setIssuedAt(now)
			.setExpiration(calcExpiration)
			.addClaims(userPrincipal.toClaims())
			.signWith(this.secretKey)
			.compact();
	}
}
