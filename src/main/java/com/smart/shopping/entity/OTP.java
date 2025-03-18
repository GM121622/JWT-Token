package com.smart.shopping.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "otp")
public class OTP implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "mobile_number")
	private String mobileNumber;

	@Column(name = "otp_sent_to")
	private String otpSentTo;

	@Column(name = "otp_sent_reason")
	private String otpSentReason;

	@Column(name = "otp_number")
	private String otpNumber;

	@Column(name = "valid_from_dttm")
	private LocalDateTime validFromDttm;

	@Column(name = "valid_upto_dttm")
	private LocalDateTime validUptoDttm;

}
