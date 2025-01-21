package com.swanith.Reminder_Call.service;

import com.swanith.Reminder_Call.model.CallResponse;
import com.swanith.Reminder_Call.repository.CallResponseRepository;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
public class ScheduledCallService {

  @Value("${twilio_phone-number}")
  private String twilioPhoneNumber;

  // // @Value("${twilio_voice-url}")
  // private String twilioVoiceUrl;

  private final CallResponseRepository callResponseRepository;

  public ScheduledCallService(CallResponseRepository callResponseRepository) {
    this.callResponseRepository = callResponseRepository;
  }

  public String initiateCall(String toPhoneNumber, String message) {
    String twilioVoiceUrl="https://reminder-call.onrender.com/twilio/voice-url?message={message_variable}"
    try {
      Call call = Call.creator(
          new PhoneNumber(toPhoneNumber),
          new PhoneNumber(twilioPhoneNumber),
          URI.create(twilioVoiceUrl))
          .create();

      CallResponse callResponse = new CallResponse();
      callResponse.setCallSid(call.getSid());
      callResponse.setToPhoneNumber(toPhoneNumber);
      callResponse.setMessage(message);
      callResponseRepository.save(callResponse);

      return "Call placed successfully!";
    } catch (Exception e) {
      e.printStackTrace();
      return "Failed to place call: " + e.getMessage();
    }
  }

  public String processVoiceResponse(String callSid, String speechResult) {
    try {
      CallResponse callResponse = callResponseRepository.findByCallSid(callSid)
          .orElseThrow(() -> new IllegalArgumentException("Call SID not found"));

      callResponse.setResponseText(speechResult);
      callResponseRepository.save(callResponse);

      return "Response saved successfully!";
    } catch (Exception e) {
      e.printStackTrace();
      return "Failed to save response: " + e.getMessage();
    }
  }
}
