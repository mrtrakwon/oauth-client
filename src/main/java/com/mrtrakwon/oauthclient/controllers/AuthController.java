package com.mrtrakwon.oauthclient.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "auth")
public class AuthController {

	@GetMapping(value = "/issueToken")
	public String issueToken() {
		return "accessToken: asdasd";
	}

	@RequestMapping(value = "/failure")
	public String failure() {
		return "failure";
	}
}
