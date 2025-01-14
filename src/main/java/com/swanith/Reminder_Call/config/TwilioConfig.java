package com.swanith.Reminder_Call.config;

import com.twilio.Twilio;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

// import javax.annotation.PostConstruct;

@Configuration
public class TwilioConfig {

  @Value("${twilio.account-sid}")
  private String accountSid;

  @Value("${twilio.auth-token}")
  private String authToken;

  @Value("${twilio.phone-number}")
  private String twilioPhoneNumber;

  // @PostConstruct
  @EventListener(ApplicationReadyEvent.class)
  public void initTwilio() {
    Twilio.init(accountSid, authToken);
    System.out.println("Twilio initialized with account sid: " + accountSid);
  }

  public String getTwilioPhoneNumber() {
    return twilioPhoneNumber;
  }
}
