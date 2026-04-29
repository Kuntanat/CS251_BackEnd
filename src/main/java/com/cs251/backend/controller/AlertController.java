package com.cs251.backend.controller;

import com.cs251.backend.dto.response.AlertResponse;
import com.cs251.backend.dto.response.ApiResponse;
import com.cs251.backend.service.AlertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
@Tag(name = "Alerts", description = "แจ้งเตือน/ประกาศ (สำหรับหน้าจอ Alerts)")
@SecurityRequirement(name = "Bearer Authentication")
public class AlertController {

    private final AlertService alertService;

    @GetMapping
    @Operation(summary = "ดึงรายการแจ้งเตือนล่าสุด")
    public ResponseEntity<ApiResponse<List<AlertResponse>>> getAlerts() {
        return ResponseEntity.ok(ApiResponse.ok(alertService.getAlerts()));
    }
}

