package com.cs251.backend.controller;

import com.cs251.backend.dto.request.LoginRequest;
import com.cs251.backend.dto.request.RegisterRequest;
import com.cs251.backend.dto.response.ApiResponse;
import com.cs251.backend.dto.response.AuthResponse;
import com.cs251.backend.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "API สำหรับ register, login, และข้อมูล user")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "ลงทะเบียนผู้ใช้ใหม่")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest request) {
        AuthResponse authResponse = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Registration successful", authResponse));
    }

    @PostMapping("/login")
    @Operation(summary = "เข้าสู่ระบบ")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {
        AuthResponse authResponse = authService.login(request);
        return ResponseEntity.ok(ApiResponse.ok("Login successful", authResponse));
    }

    @GetMapping("/me")
    @Operation(summary = "ดึงข้อมูลตัวเอง", security = @SecurityRequirement(name = "Bearer Authentication"))
    public ResponseEntity<ApiResponse<AuthResponse.UserInfo>> getMe(
            @AuthenticationPrincipal UserDetails userDetails) {
        AuthResponse.UserInfo userInfo = authService.getMe(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.ok(userInfo));
    }
}
