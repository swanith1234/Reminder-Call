package com.swanith.Reminder_Call.controller;

import com.swanith.Reminder_Call.service.DynamicScheduledCallService;
import com.swanith.Reminder_Call.service.ScheduledCallService;
import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.voice.Gather;
import com.twilio.twiml.voice.Say;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/twilio")
public class VoiceResponseController {

  private final ScheduledCallService scheduledCallService;
  private final DynamicScheduledCallService dynamicScheduledCallService;

  // Constructor to inject dependencies
  public VoiceResponseController(ScheduledCallService scheduledCallService,
      DynamicScheduledCallService dynamicScheduledCallService) {
    this.scheduledCallService = scheduledCallService;
    this.dynamicScheduledCallService = dynamicScheduledCallService;
  }

  @PostMapping("/schedule-call")
  public ResponseEntity<String> scheduleCall(
      @RequestParam("toPhoneNumber") String toPhoneNumber,
      @RequestParam("message") String message,
      @RequestParam("scheduledTime") String scheduledTime) {

    try {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
      LocalDateTime scheduleTime = LocalDateTime.parse(scheduledTime, formatter);

      dynamicScheduledCallService.scheduleCall(toPhoneNumber, message, scheduleTime);
      return ResponseEntity.ok("Call scheduled successfully for: " + scheduledTime);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.badRequest().body("Error scheduling call: " + e.getMessage());
    }
  }

  @PostMapping("/make-call")
  public ResponseEntity<String> makeCall(
      @RequestParam("toPhoneNumber") String toPhoneNumber,
      @RequestParam("message") String message) {

    try {
      String result = scheduledCallService.initiateCall(toPhoneNumber, message);
      return ResponseEntity.ok(result);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.badRequest().body("Error initiating call: " + e.getMessage());
    }
  }

  @PostMapping("/voice-response-handler")
  public ResponseEntity<String> handleVoiceResponse(
      @RequestParam("CallSid") String callSid,
      @RequestParam("SpeechResult") String speechResult) {

    // Log incoming parameters for debugging
    System.out.println("CallSid: " + callSid);
    System.out.println("SpeechResult: " + speechResult);

    try {
      // Process the speech input (save to database or log)
      String message = "User said: " + speechResult;
      scheduledCallService.processVoiceResponse(callSid, message);

      // Return TwiML response
      String twiml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
          + "<Response>\n"
          + "    <Say voice=\"alice\">Thank you for your response. Goodbye!</Say>\n"
          + "</Response>";

      return ResponseEntity.ok()
          .header("Content-Type", "application/xml")
          .body(twiml);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.badRequest().body("Error processing voice response: " + e.getMessage());
    }
  }

  @RequestMapping(value = "/voice-url", method = { RequestMethod.GET, RequestMethod.POST })
  public ResponseEntity<String> voiceUrl(@RequestParam("message") String message) {
    try {
      // Create TwiML XML to collect speech from the user
      String twiml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
          + "<Response>\n"
          + "    <Say voice=\"alice\">" + message + "</Say>\n"
          + "    <Gather input=\"speech\" timeout=\"10\" action=\"/twilio/voice-response-handler\" method=\"POST\">\n"
          + "        <Say voice=\"alice\">Please say something to confirm you received this message.</Say>\n"
          + "    </Gather>\n"
          + "    <Say voice=\"alice\">We didn't get a response. Goodbye!</Say>\n"
          + "</Response>";

      // Return response with correct Content-Type
      return ResponseEntity.ok()
          .header("Content-Type", "application/xml")
          .body(twiml);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.badRequest().body("Error generating voice URL TwiML: " + e.getMessage());
    }
  }
}
