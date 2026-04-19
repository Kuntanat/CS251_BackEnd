package com.cs251.backend.controller;

import com.cs251.backend.dto.request.EmployeeRegisterRequest;
import com.cs251.backend.dto.response.ApiResponse;
import com.cs251.backend.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@Tag(name = "Employee", description = "จัดการข้อมูลเจ้าหน้าที่")
@SecurityRequirement(name = "Bearer Authentication")
public class EmployeeController {

    private final EmployeeService employeeService;

    /** Function 2: ลงทะเบียน Employee */
    @PostMapping("/register")
    @Operation(summary = "ลงทะเบียนเจ้าหน้าที่ใหม่ (Function 2)")
    public ResponseEntity<ApiResponse<Integer>> register(@Valid @RequestBody EmployeeRegisterRequest req) {
        Integer id = employeeService.register(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Employee registered", id));
    }
}
