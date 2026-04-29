package com.cs251.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertResponse {
    private String type;        // LOW_STOCK | EXPIRING_SOON | INFO
    private String severity;    // HIGH | MEDIUM | LOW
    private String title;
    private String message;
    private LocalDate date;
    private Map<String, Object> meta;
}

