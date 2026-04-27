package com.cs251.backend.service;

import com.cs251.backend.dto.response.AlertResponse;
import com.cs251.backend.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertRepository alertRepository;

    public List<AlertResponse> getAlerts() {
        return alertRepository.getAlerts();
    }
}

