package com.smart.shopping.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smart.shopping.entity.UsersInfo;
import com.smart.shopping.pojo.VerifyOtp;
import com.smart.shopping.service.UserAuthService;

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
	public String details(@RequestHeader HttpHeaders headers) {
		return "this are the details ";
	}
	
	@GetMapping("/information")
	public String details() {
		return "this are the information details ";
	}

	@GetMapping("/info")
	public String info(@RequestHeader HttpHeaders headers) {
		return "info";
	}

	@PostMapping("/create/account")
	public ResponseEntity<Object> details(@RequestBody UsersInfo usersInfo) {
		return authService.registration(usersInfo);
	}
	

	@PostMapping("/verify/otpNumber")
	public ResponseEntity<?> verifyOtp(@RequestBody VerifyOtp usersInfo) {
		
		return authService.verifyOtp(usersInfo);

	}
	
	@PostMapping("/send/otpNumber")
	public ResponseEntity<?> sendOtpNumber(@RequestParam String mobileNumber) {
		
		return authService.sendOtpNumber(mobileNumber);

	}
	
	@PostMapping("/update/password")
	public ResponseEntity<?> updatePassword(@RequestParam String mobileNumber,@RequestParam String password) {
		
		return authService.updatePassword(mobileNumber,password);
	}
}
