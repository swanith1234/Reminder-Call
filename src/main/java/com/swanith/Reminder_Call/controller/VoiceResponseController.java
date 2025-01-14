package com.swanith.Reminder_Call.controller;

import com.swanith.Reminder_Call.model.CallResponse;
import com.swanith.Reminder_Call.repository.CallResponseRepository;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/twilio")
public class VoiceResponseController {

  private final CallResponseRepository callResponseRepository;

  @Value("${twilio.phone-number}")
  private String twilioPhoneNumber;

  public VoiceResponseController(CallResponseRepository callResponseRepository) {
    this.callResponseRepository = callResponseRepository;
  }

  @PostMapping("/make-call")
  public String makeCall(@RequestParam("toPhoneNumber") String toPhoneNumber) {
    try {
      String message = "Hello, this is a scheduled call. Please respond.";

      Call call = Call.creator(
          new PhoneNumber(toPhoneNumber),
          new PhoneNumber(twilioPhoneNumber),
          URI.create("http://demo.twilio.com/docs/voice.xml"))
          .create();

      // Save the call information in the database
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

  @PostMapping("/voice-response-handler")
  public String handleVoiceResponse(@RequestParam("CallSid") String callSid,
      @RequestParam("SpeechResult") String speechResult) {
    try {
      CallResponse callResponse = callResponseRepository.findById(Long.valueOf(callSid))
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
