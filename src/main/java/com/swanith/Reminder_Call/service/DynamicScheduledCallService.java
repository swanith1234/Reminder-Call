package com.swanith.Reminder_Call.service;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ScheduledFuture;

@Service
public class DynamicScheduledCallService {

    private final ThreadPoolTaskScheduler taskScheduler;
    private final ScheduledCallService scheduledCallService;

    public DynamicScheduledCallService(ScheduledCallService scheduledCallService) {
        this.scheduledCallService = scheduledCallService;
        this.taskScheduler = new ThreadPoolTaskScheduler();
        this.taskScheduler.setPoolSize(5); // Set thread pool size
        this.taskScheduler.initialize();
    }

    public ScheduledFuture<?> scheduleCall(String toPhoneNumber, String message, LocalDateTime scheduleTime) {
        // Calculate delay
        long delayInMillis = Duration.between(LocalDateTime.now(), scheduleTime).toMillis();
System.out.print("intiating call");
        return taskScheduler.schedule(() -> {
            scheduledCallService.initiateCall(toPhoneNumber, message);
        }, new java.util.Date(System.currentTimeMillis() + delayInMillis));
    }
}
