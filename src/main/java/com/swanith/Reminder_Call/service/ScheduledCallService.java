package com.swanith.Reminder_Call.service;

import com.swanith.Reminder_Call.model.CallResponse;
import com.swanith.Reminder_Call.repository.CallResponseRepository;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
public class ScheduledCallService {

  @Value("${twilio.phone-number}")
  private String twilioPhoneNumber;

  private final CallResponseRepository callResponseRepository;

  public ScheduledCallService(CallResponseRepository callResponseRepository) {
    this.callResponseRepository = callResponseRepository;
  }

  // @Scheduled(fixedRate = 60000) // Adjust as per your scheduling needs
  public void makeScheduledCall() {
    String toPhoneNumber = "+916301666195"; // Replace with recipient's phone number
    String message = "Hello, this is a scheduled call. Please respond.";

    Call call = Call.creator(
        new PhoneNumber(toPhoneNumber),
        new PhoneNumber(twilioPhoneNumber),
        URI.create("http://localhost:8080/voice-response-handler")) // TwiML for handling the call
        .create();

    CallResponse callResponse = new CallResponse();
    callResponse.setCallSid(call.getSid());
    callResponse.setToPhoneNumber(toPhoneNumber);
    callResponse.setMessage(message);
    callResponseRepository.save(callResponse);
  }
}
