package com.electrotech.store.controller;

import com.electrotech.store.model.AccessLog;
import com.electrotech.store.repository.AccessLogRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/logs")
public class LoggingController {

    private final AccessLogRepository repository;

    public LoggingController(AccessLogRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/track")
    public AccessLog trackAccess(@RequestBody AccessLog log) {
        return repository.save(log);
    }
}