package com.smart.shopping.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.smart.shopping.entity.UsersInfo;
import com.smart.shopping.entity.UsersPrinciple;
import com.smart.shopping.repository.UsersAuthenticatioRepo;

@Service
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	private UsersAuthenticatioRepo usersRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		UsersInfo usersInfo = null;
		usersInfo = usersRepo.findByUserName(username);

		if (usersInfo == null) {
			throw new UsernameNotFoundException("oops! User Not Found. ");
		}

		return new UsersPrinciple(usersInfo);
	}

}
