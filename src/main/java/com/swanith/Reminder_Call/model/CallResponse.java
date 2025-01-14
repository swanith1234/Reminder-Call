package com.swanith.Reminder_Call.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class CallResponse {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String callSid;
  private String toPhoneNumber;
  private String message;
  private String responseText; // Store the voice-to-text response

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCallSid() {
    return callSid;
  }

  public void setCallSid(String callSid) {
    this.callSid = callSid;
  }

  public String getToPhoneNumber() {
    return toPhoneNumber;
  }

  public void setToPhoneNumber(String toPhoneNumber) {
    this.toPhoneNumber = toPhoneNumber;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getResponseText() {
    return responseText;
  }

  public void setResponseText(String responseText) {
    this.responseText = responseText;
  }

}
