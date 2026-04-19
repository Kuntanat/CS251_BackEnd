package com.cs251.backend.controller;

import com.cs251.backend.dto.request.DonorRegisterRequest;
import com.cs251.backend.dto.request.UpdateDonorRequest;
import com.cs251.backend.dto.response.ApiResponse;
import com.cs251.backend.dto.response.DonorResponse;
import com.cs251.backend.service.DonorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/donors")
@RequiredArgsConstructor
@Tag(name = "Donor", description = "จัดการข้อมูลผู้บริจาคเลือด")
@SecurityRequirement(name = "Bearer Authentication")
public class DonorController {

    private final DonorService donorService;

    /** Function 1: ลงทะเบียน Donor */
    @PostMapping("/register")
    @Operation(summary = "ลงทะเบียนผู้บริจาคใหม่ (Function 1)")
    public ResponseEntity<ApiResponse<Integer>> register(@Valid @RequestBody DonorRegisterRequest req) {
        Integer id = donorService.register(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Donor registered", id));
    }

    /** Function 4: แสดงตาราง Donor */
    @GetMapping
    @Operation(summary = "ดูรายชื่อผู้บริจาคทั้งหมด (Function 4)")
    public ResponseEntity<ApiResponse<List<DonorResponse>>> findAll() {
        return ResponseEntity.ok(ApiResponse.ok(donorService.findAll()));
    }

    /** Function 6: แก้ไขข้อมูล Donor */
    @PutMapping("/{donorId}")
    @Operation(summary = "แก้ไขข้อมูลผู้บริจาค (Function 6)")
    public ResponseEntity<ApiResponse<Void>> update(@PathVariable Integer donorId,
                                                     @RequestBody UpdateDonorRequest req) {
        donorService.update(donorId, req);
        return ResponseEntity.ok(ApiResponse.ok("Updated"));
    }

    /** Function 7: ระงับสิทธิ์ */
    @PatchMapping("/{donorId}/suspend")
    @Operation(summary = "ระงับสิทธิ์ผู้บริจาค (Function 7)")
    public ResponseEntity<ApiResponse<Void>> suspend(@PathVariable Integer donorId,
                                                      @RequestBody Map<String, String> body) {
        donorService.suspend(donorId, body.get("remark"));
        return ResponseEntity.ok(ApiResponse.ok("Suspended"));
    }

    /** Function 8: ยกเลิกการระงับ */
    @PatchMapping("/{donorId}/reinstate")
    @Operation(summary = "ยกเลิกการระงับสิทธิ์ (Function 8)")
    public ResponseEntity<ApiResponse<Void>> reinstate(@PathVariable Integer donorId,
                                                        @RequestBody Map<String, String> body) {
        donorService.reinstate(donorId, body.get("remark"));
        return ResponseEntity.ok(ApiResponse.ok("Reinstated"));
    }

    /** Function 11: ค้นหาผู้บริจาค */
    @GetMapping("/search")
    @Operation(summary = "ค้นหาผู้บริจาค (Function 11)")
    public ResponseEntity<ApiResponse<List<DonorResponse>>> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer donorId) {
        return ResponseEntity.ok(ApiResponse.ok(donorService.search(name, donorId)));
    }
}
