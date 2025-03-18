package com.smart.shopping.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.smart.shopping.entity.OTP;
import com.smart.shopping.entity.UsersInfo;

@Repository
public interface OTPRepo extends JpaRepository<OTP, Long> {

	@Query("SELECT u FROM UsersInfo u WHERE u.username = :username")
	UsersInfo findByUserName(String username);

	@Query("SELECT o FROM OTP o WHERE o.mobileNumber = :mobileNumber")
	OTP findOtpByMobileNumber(String mobileNumber);

}
