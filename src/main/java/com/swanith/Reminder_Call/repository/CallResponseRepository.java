package com.swanith.Reminder_Call.repository;

import com.swanith.Reminder_Call.model.CallResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CallResponseRepository extends JpaRepository<CallResponse, Long> {
  Optional<CallResponse> findByCallSid(String callSid);
}
