package com.smart.shopping.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.smart.shopping.entity.UsersInfo;
import com.smart.shopping.repository.UsersRepo;

@Service
public class UserAuthService {

	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

	@Autowired
	private UsersRepo usersRepo;

	@Autowired
	private JWTService jwtService;

	@Autowired
	AuthenticationManager authenticationManager;

	public UsersInfo registration(UsersInfo usersInfo) {
		usersInfo.setPassword(encoder.encode(usersInfo.getPassword()));
		return usersRepo.save(usersInfo);
	}

	public String varify(UsersInfo usersInfo) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(usersInfo.getUsername(), usersInfo.getPassword()));

		if (authentication.isAuthenticated())
			return jwtService.generateToken(usersInfo.getUsername());

		return "Fails";
	}
}
