package com.smart.shopping.pojo;

import lombok.Data;

@Data
public class VerifyOtp {
	private String firstName;
	private String username;
	private String password;
	private String role;
	private String status;
	private String otpNumber;
}
