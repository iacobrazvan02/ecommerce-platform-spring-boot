package com.electrotech.store.controller;

import com.electrotech.store.service.AnalyticsService;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @PostMapping("/track/{productId}")
    public void trackView(@PathVariable @NonNull Long productId) {
        analyticsService.trackView(productId);
    }
}