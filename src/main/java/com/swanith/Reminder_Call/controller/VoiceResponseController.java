package com.swanith.Reminder_Call.controller;

import com.swanith.Reminder_Call.service.ScheduledCallService;
import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.voice.Gather;
import com.twilio.twiml.voice.Say;

import org.springframework.http.ResponseEntity;
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

 @GetMapping("/voice-url")
  public ResponseEntity<String> voiceUrl(@RequestParam("message") String message) {
    // You can customize the message or use the one passed in the URL
    String twiml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
        + "<Response>\n"
        + "    <Say voice=\"alice\">" + message + "</Say>\n"
        + "    <Gather input=\"dtmf\" timeout=\"10\" numDigits=\"1\" action=\"/twilio/voice-response-handler\" method=\"POST\">\n"
        + "        <Say voice=\"alice\">Please press any key to confirm you received this message.</Say>\n"
        + "    </Gather>\n"
        + "    <Say voice=\"alice\">We didn't get a response. Goodbye!</Say>\n"
        + "</Response>";

    return ResponseEntity.ok(twiml);
  }
  
}
