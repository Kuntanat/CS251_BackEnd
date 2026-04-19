package com.cs251.backend.service;

import com.cs251.backend.dto.request.EmployeeRegisterRequest;
import com.cs251.backend.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    // ── Function 2 ────────────────────────────────────────────────────────────
    public Integer register(EmployeeRegisterRequest req) {
        String hashed = passwordEncoder.encode(req.getPassword());
        Integer id = employeeRepository.register(req, hashed);
        log.info("Employee registered: id={}", id);
        return id;
    }
}
