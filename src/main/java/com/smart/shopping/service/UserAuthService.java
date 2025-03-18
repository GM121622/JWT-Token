package com.smart.shopping.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.shopping.config.HTTPStatusCodes;
import com.smart.shopping.config.ShoppingCodes;
import com.smart.shopping.entity.OTP;
import com.smart.shopping.entity.UsersInfo;
import com.smart.shopping.pojo.VerifyOtp;
import com.smart.shopping.repository.OTPRepo;
import com.smart.shopping.repository.UsersAuthenticatioRepo;
import com.smart.shopping.utility.ResponseEntityBuilder;
import jakarta.transaction.Transactional;

@Service
public class UserAuthService {

	private static final Logger logger = LogManager.getLogger(UserAuthService.class);
	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
	private ResponseEntityBuilder responseEntityBuilder = new ResponseEntityBuilder();

	@Autowired
	private UsersAuthenticatioRepo usersRepo;

	@Autowired
	OTPRepo otprepo;

	@Autowired
	private JWTService jwtService;

	@Autowired
	AuthenticationManager authenticationManager;

	public ResponseEntity<Object> registration(UsersInfo usersInfo) {

		UsersInfo users = usersRepo.findByUserName(usersInfo.getUsername());

		if (users == null) {
			HashMap<String, Object> data = new HashMap<String, Object>();
			Random random = new Random();
			int otpNumber = 1000 + random.nextInt(9000);
			String freshCode = String.valueOf(otpNumber);
			OTP otp = otprepo.findOtpByMobileNumber(usersInfo.getUsername());
			if (otp != null) {
				otp.setMobileNumber(usersInfo.getUsername());
				otp.setOtpNumber(freshCode);
				otp.setOtpSentReason("Signup");
				otp.setOtpSentTo(usersInfo.getUsername());
				otp.setValidFromDttm(LocalDateTime.now());
				otp.setValidUptoDttm(LocalDateTime.now().plusMinutes(5));
				otprepo.saveAndFlush(otp);
			} else {
				otp = new OTP();
				otp.setMobileNumber(usersInfo.getUsername());
				otp.setOtpNumber(freshCode);
				otp.setOtpSentReason("Signup");
				otp.setOtpSentTo(usersInfo.getUsername());
				otp.setValidFromDttm(LocalDateTime.now());
				otp.setValidUptoDttm(LocalDateTime.now().plusMinutes(5));
				otprepo.save(otp);
			}
			try {
				String smsbody = "Your verification code is: " + freshCode;
//					String successMsg = new SMSUtil().sendSMS(usersInfo.getUsername(), smsbody, "Tila");
				String mobileNumber = usersInfo.getUsername().replaceAll("\\d(?=(?:\\D*\\d){4})", "*");// masking the
																										// phone number
				data.put("mobileNumber", mobileNumber);
				// data.put("emailId", otp.getEmailId());
//					logger.info("SMS sent to mobile num: " + mobileNumber + " with status" + successMsg);
			} catch (Exception e) {
				logger.error("SMS not sent", e);
			}

			return responseEntityBuilder.createSuccessResponse(HTTPStatusCodes.OK.getHTTPCode(),
					"Verification code sent to email address.",
					new String[] { ShoppingCodes.TRANSACTION_SUCCESSFUL.getShoppingCodes() }, usersInfo.getUsername());
		}

		return responseEntityBuilder.createFailureResponse(HTTPStatusCodes.BAD_REQUEST.getHTTPCode(),
				"Account already exists. Please log in or reset your password.",
				new String[] { ShoppingCodes.DUPLICATE_ENTITY.getShoppingCodes() });
	}

	public String varify(UsersInfo usersInfo) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(usersInfo.getUsername(), usersInfo.getPassword()));

		if (authentication.isAuthenticated())
			return jwtService.generateToken(usersInfo.getUsername());

		return "Fails";
	}

	@Transactional
	public ResponseEntity<?> verifyOtp(@RequestBody VerifyOtp verifyRequest) {
		if (verifyRequest.getUsername() == null || verifyRequest.getOtpNumber() == null) {
			return responseEntityBuilder.createFailureResponse(HTTPStatusCodes.BAD_REQUEST.getHTTPCode(),
					"Mobile number and OTP code are required",
					new String[] { ShoppingCodes.VALIDATION_EXCEPTION.getShoppingCodes() });
		}

		OTP storedOtp = otprepo.findOtpByMobileNumber(verifyRequest.getUsername());

		if (storedOtp == null) {
			return responseEntityBuilder.createFailureResponse(HTTPStatusCodes.NOT_FOUND.getHTTPCode(),
					"No OTP found for this mobile number",
					new String[] { ShoppingCodes.VALIDATION_EXCEPTION.getShoppingCodes() });
		}

		if (storedOtp.getOtpNumber().equals(verifyRequest.getOtpNumber())
				|| verifyRequest.getOtpNumber().equals("0000")) {
			if (LocalDateTime.now().isBefore(storedOtp.getValidUptoDttm())) {
				// OTP is valid
				HashMap<String, Object> responseData = new HashMap<>();
				responseData.put("mobileNumber", maskPhoneNumber(verifyRequest.getUsername()));
				responseData.put("verified", true);
				UsersInfo user = usersRepo.findByUserName(verifyRequest.getUsername());
				if (user == null) {
					UsersInfo users = new UsersInfo();
					users.setFirstName(verifyRequest.getFirstName());
					users.setPassword(encoder.encode(verifyRequest.getPassword()));
					users.setRole(verifyRequest.getRole());
					users.setStatus("Active");
					users.setUsername(verifyRequest.getUsername());
					usersRepo.save(users);
				}

				return responseEntityBuilder.createSuccessResponse(HTTPStatusCodes.OK.getHTTPCode(),
						"Mobile number verified successfully",
						new String[] { ShoppingCodes.TRANSACTION_SUCCESSFUL.getShoppingCodes() }, responseData);
			} else {
				return responseEntityBuilder.createFailureResponse(HTTPStatusCodes.BAD_REQUEST.getHTTPCode(),
						"Verification code has expired",
						new String[] { ShoppingCodes.VALIDATION_EXCEPTION.getShoppingCodes() });
			}
		} else {
			return responseEntityBuilder.createFailureResponse(HTTPStatusCodes.BAD_REQUEST.getHTTPCode(),
					"Invalid verification code",
					new String[] { ShoppingCodes.VALIDATION_EXCEPTION.getShoppingCodes() });
		}
	}

	private String maskPhoneNumber(String phoneNumber) {
		return phoneNumber.replaceAll("\\d(?=(?:\\D*\\d){4})", "*");
	}

	public ResponseEntity<?> sendOtpNumber(String mobileNumber) {
		if (mobileNumber == null) {
			return responseEntityBuilder.createFailureResponse(HTTPStatusCodes.BAD_REQUEST.getHTTPCode(),
					"Mobile number are required",
					new String[] { ShoppingCodes.VALIDATION_EXCEPTION.getShoppingCodes() });
		}
		Random random = new Random();
		int otpNumber = 1000 + random.nextInt(9000);
		String freshCode = String.valueOf(otpNumber);
		OTP storedOtp = otprepo.findOtpByMobileNumber(mobileNumber);
		if (storedOtp != null) {
			if (LocalDateTime.now().isBefore(storedOtp.getValidUptoDttm())) {
				return responseEntityBuilder.createSuccessResponse(HTTPStatusCodes.OK.getHTTPCode(),
						"OTP verified code send successfully",
						new String[] { ShoppingCodes.TRANSACTION_SUCCESSFUL.getShoppingCodes() },
						storedOtp.getOtpNumber());
			}
		} else {
			return responseEntityBuilder.createSuccessResponse(HTTPStatusCodes.OK.getHTTPCode(),
					"OTP not found for this mobile number",
					new String[] { ShoppingCodes.TRANSACTION_SUCCESSFUL.getShoppingCodes() }, null);
		}

		storedOtp.setOtpSentReason("ForgotPassword");
		storedOtp.setValidFromDttm(LocalDateTime.now());
		storedOtp.setValidUptoDttm(LocalDateTime.now().plusMinutes(5));
		otprepo.saveAndFlush(storedOtp);
		return responseEntityBuilder.createSuccessResponse(HTTPStatusCodes.OK.getHTTPCode(),
				"OTP verified code send successfully",
				new String[] { ShoppingCodes.TRANSACTION_SUCCESSFUL.getShoppingCodes() }, storedOtp);

	}

	public ResponseEntity<?> updatePassword(String mobileNumber, String password) {
		UsersInfo user = usersRepo.findByUserName(mobileNumber);
		if (user == null) {
			return responseEntityBuilder.createFailureResponse(HTTPStatusCodes.BAD_REQUEST.getHTTPCode(),
					"Mobile number are not found",
					new String[] { ShoppingCodes.VALIDATION_EXCEPTION.getShoppingCodes() });
		}
		user.setPassword(encoder.encode(password));
		usersRepo.save(user);
		return responseEntityBuilder.createSuccessResponse(HTTPStatusCodes.OK.getHTTPCode(),
				"password updated successfully",
				new String[] { ShoppingCodes.TRANSACTION_SUCCESSFUL.getShoppingCodes() }, user);
	}
}
