package com.krowdless.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.twilio.Twilio;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import com.twilio.rest.api.v2010.account.Message;

@Service
public class SmsService {
	@Value("${twilio.account_sid}")
	private String accountSid;

	@Value("${twilio.auth_token}")
	private String authToken;

	@Value("${twilio.phone-number}")
    private String fromNumber;

	@PostConstruct
	private void initTwilio() {
        Twilio.init(accountSid, authToken);
    }

	public void sendOtp(String phoneNumber, String otp) {

        String message = "Your OTP is: " + otp + ". It expires in 10 minutes.";

        Message.creator(
                new PhoneNumber(phoneNumber),
                new PhoneNumber(fromNumber),
                message
        ).create();
    }

}
