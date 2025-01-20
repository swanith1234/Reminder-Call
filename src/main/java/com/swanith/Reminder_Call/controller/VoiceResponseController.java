package com.swanith.Reminder_Call.controller;

import com.swanith.Reminder_Call.service.ScheduledCallService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/twilio")
public class VoiceResponseController {

  private final ScheduledCallService ScheduledCallService;

  public VoiceResponseController(ScheduledCallService ScheduledCallService) {
    this.ScheduledCallService = ScheduledCallService;
  }

  @PostMapping("/make-call")
  public String makeCall(@RequestParam("toPhoneNumber") String toPhoneNumber,
      @RequestParam("message") String message) {
    return ScheduledCallService.initiateCall(toPhoneNumber, message);
  }

  @PostMapping("/voice-response-handler")
  public String handleVoiceResponse(@RequestParam("CallSid") String callSid,
      @RequestParam("SpeechResult") String speechResult) {
    return ScheduledCallService.processVoiceResponse(callSid, speechResult);
  }
}
