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

@PostMapping("/voice-response-handler")
public ResponseEntity<String> handleVoiceResponse(
    @RequestParam("CallSid") String callSid,
    @RequestParam("Digits") String digits) {

    // Log incoming parameters
    System.out.println("CallSid: " + callSid);
    System.out.println("Digits: " + digits);

    // Process response based on DTMF input
    String message = "You pressed: " + digits;
    ScheduledCallService.processVoiceResponse(callSid, message);

    // Return TwiML response
    String twiml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
        + "<Response>\n"
        + "    <Say voice=\"alice\">Thank you for your response. Goodbye!</Say>\n"
        + "</Response>";

    return ResponseEntity.ok()
        .header("Content-Type", "application/xml")
        .body(twiml);
}


  @PostMapping("/voice-response-handler")
  public String handleVoiceResponse(@RequestParam("CallSid") String callSid,
      @RequestParam("SpeechResult") String speechResult) {
    return ScheduledCallService.processVoiceResponse(callSid, speechResult);
  }

@RequestMapping(value = "/voice-url", method = {RequestMethod.GET, RequestMethod.POST})
public ResponseEntity<String> voiceUrl(@RequestParam("message") String message) {
    // Create TwiML XML
    String twiml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
        + "<Response>\n"
        + "    <Say voice=\"alice\">" + message + "</Say>\n"
        + "    <Gather input=\"dtmf\" timeout=\"10\" numDigits=\"1\" action=\"/twilio/voice-response-handler\" method=\"POST\">\n"
        + "        <Say voice=\"alice\">Please press any key to confirm you received this message.</Say>\n"
        + "    </Gather>\n"
        + "    <Say voice=\"alice\">We didn't get a response. Goodbye!</Say>\n"
        + "</Response>";

    // Return response with correct Content-Type
    return ResponseEntity.ok()
        .header("Content-Type", "application/xml")
        .body(twiml);
}

  
}
