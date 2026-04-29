package com.cs251.backend.service;

import com.cs251.backend.repository.BloodBagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BloodBagExpiryScheduler {

    private final BloodBagRepository bloodBagRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void markExpiredBags() {
        bloodBagRepository.markExpiredBags();
        log.info("Auto-expiry job complete: expired blood bags marked as BagStatus=4");
    }
}
