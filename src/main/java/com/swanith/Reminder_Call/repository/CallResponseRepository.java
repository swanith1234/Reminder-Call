package com.swanith.Reminder_Call.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swanith.Reminder_Call.model.CallResponse;

public interface CallResponseRepository extends JpaRepository<CallResponse, Long> {
}