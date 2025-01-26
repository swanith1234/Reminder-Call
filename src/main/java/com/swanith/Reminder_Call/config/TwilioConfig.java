package com.swanith.Reminder_Call.config;

import com.twilio.Twilio;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

// import javax.annotation.PostConstruct;

@Configuration
public class TwilioConfig {

  @Value("${twilio_account-sid}")
  private String accountSid;

  @Value("${twilio_auth-token}")
  private String authToken;

  @Value("${twilio_phone-number}")
  private String twilioPhoneNumber;

  // @PostConstruct
@EventListener(ApplicationReadyEvent.class)
public void initTwilio() {
    System.out.println("Twilio Account SID: " + accountSid); // Verify SID
    System.out.println("Twilio Auth Token: " + authToken); // Verify Token
    System.out.println("Twilio Phone Number: " + twilioPhoneNumber); // Verify Phone Number
    Twilio.init(accountSid, authToken);
    System.out.println("Twilio initialized successfully.");
}

  public String getTwilioPhoneNumber() {
    return twilioPhoneNumber;
  }
}
