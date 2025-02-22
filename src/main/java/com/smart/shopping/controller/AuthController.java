package com.smart.shopping.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smart.shopping.entity.UsersInfo;
import com.smart.shopping.service.UserAuthService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private UserAuthService authService;

	@PostMapping("/login")
	public String login(@RequestBody UsersInfo usersInfo) {

		return authService.varify(usersInfo);
	}

	@GetMapping("/details")
	public String details(HttpServletRequest httpRequest) {
		return "this are the details ";
	}

	@GetMapping("/info")
	public String info(HttpServletRequest httpRequest) {
		return "info";
	}

	@PostMapping("/create")
	public UsersInfo details(@RequestBody UsersInfo usersInfo) {

		return authService.registration(usersInfo);

	}
}
