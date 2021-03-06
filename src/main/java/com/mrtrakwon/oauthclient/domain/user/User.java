package com.mrtrakwon.oauthclient.domain.user;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column
	private String name;

	@Column
	private String email;

	@Column
	private String password;

	@Column
	private String providerId;

	@Builder
	public User(String name, String email, String password,String providerId) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.providerId = providerId;
	}
}
