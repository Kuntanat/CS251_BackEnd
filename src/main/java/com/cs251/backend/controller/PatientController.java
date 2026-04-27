package com.cs251.backend.controller;

import com.cs251.backend.dto.request.PatientRequest;
import com.cs251.backend.dto.response.ApiResponse;
import com.cs251.backend.dto.response.PatientResponse;
import com.cs251.backend.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@Tag(name = "Patient", description = "จัดการข้อมูลผู้ป่วย")
@SecurityRequirement(name = "Bearer Authentication")
public class PatientController {

    private final PatientService patientService;

    /** Function 5: แสดงตารางผู้ป่วย */
    @GetMapping
    @Operation(summary = "ดูรายชื่อผู้ป่วยทั้งหมด (Function 5)")
    public ResponseEntity<ApiResponse<List<PatientResponse>>> findAll() {
        return ResponseEntity.ok(ApiResponse.ok(patientService.findAll()));
    }

    /** Function 9: เพิ่มผู้ป่วยใหม่ */
    @PostMapping
    @Operation(summary = "เพิ่มผู้ป่วยใหม่ (Function 9)")
    public ResponseEntity<ApiResponse<Integer>> create(@Valid @RequestBody PatientRequest req) {
        Integer id = patientService.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Patient created", id));
    }

    /** Function 10: แก้ไขข้อมูลผู้ป่วย */
    @PutMapping("/{patientId}")
    @Operation(summary = "แก้ไขข้อมูลผู้ป่วย (Function 10)")
    public ResponseEntity<ApiResponse<String>> update(@PathVariable Integer patientId,
                                                     @Valid @RequestBody PatientRequest req) {
        patientService.update(patientId, req);
        return ResponseEntity.ok(ApiResponse.ok("Updated"));
    }

    /** Function 12: ค้นหาผู้ป่วย */
    @GetMapping("/search")
    @Operation(summary = "ค้นหาผู้ป่วย (Function 12)")
    public ResponseEntity<ApiResponse<List<PatientResponse>>> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer patientId) {
        return ResponseEntity.ok(ApiResponse.ok(patientService.search(name, patientId)));
    }
}
