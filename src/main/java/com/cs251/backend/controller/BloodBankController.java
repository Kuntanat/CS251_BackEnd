package com.cs251.backend.controller;

import com.cs251.backend.dto.request.BloodBagUpdateRequest;
import com.cs251.backend.dto.request.BloodTestRequest;
import com.cs251.backend.dto.request.BloodUsageRequest;
import com.cs251.backend.dto.request.DonationRequest;
import com.cs251.backend.dto.response.ApiResponse;
import com.cs251.backend.dto.response.BloodBagResponse;
import com.cs251.backend.service.BloodBagService;
import com.cs251.backend.service.BloodTestService;
import com.cs251.backend.service.BloodUsageService;
import com.cs251.backend.service.DonationService;
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
@RequestMapping("/api/blood")
@RequiredArgsConstructor
@Tag(name = "Blood Bank", description = "จัดการคลังเลือด การบริจาค และการจ่ายเลือด")
@SecurityRequirement(name = "Bearer Authentication")
public class BloodBankController {

    private final BloodBagService bloodBagService;
    private final BloodTestService bloodTestService;
    private final BloodUsageService bloodUsageService;
    private final DonationService donationService;

    /** Function 13: แสดงตารางคลังเลือด */
    @GetMapping("/bags")
    @Operation(summary = "ดูคลังเลือดทั้งหมด เรียงตามวันหมดอายุ (Function 13)")
    public ResponseEntity<ApiResponse<List<BloodBagResponse>>> findAllBags() {
        return ResponseEntity.ok(ApiResponse.ok(bloodBagService.findAll()));
    }

    /** Function 14: หาเลือดใกล้หมดอายุ */
    @GetMapping("/bags/expiring")
    @Operation(summary = "ถุงเลือดใกล้หมดอายุภายใน 7 วัน (Function 14)")
    public ResponseEntity<ApiResponse<List<BloodBagResponse>>> findExpiring() {
        return ResponseEntity.ok(ApiResponse.ok(bloodBagService.findExpiringSoon()));
    }

    /** Function 15: อัปเดตถุงเลือด */
    @PutMapping("/bags/{bagId}")
    @Operation(summary = "อัปเดตข้อมูลถุงเลือด (Function 15)")
    public ResponseEntity<ApiResponse<String>> updateBag(@PathVariable Integer bagId,
                                                        @RequestBody BloodBagUpdateRequest req) {
        bloodBagService.update(bagId, req);
        return ResponseEntity.ok(ApiResponse.ok("BloodBag updated"));
    }

    /** Function 16: บันทึกรับบริจาค */
    @PostMapping("/donations")
    @Operation(summary = "บันทึกการรับบริจาคเลือด (Function 16)")
    public ResponseEntity<ApiResponse<Integer>> recordDonation(@Valid @RequestBody DonationRequest req) {
        Integer id = donationService.record(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Donation recorded", id));
    }

    /** Function 17: บันทึกผลทางห้องปฏิบัติการ */
    @PostMapping("/tests")
    @Operation(summary = "บันทึกผลการตรวจเลือด (Function 17)")
    public ResponseEntity<ApiResponse<Integer>> recordTest(@Valid @RequestBody BloodTestRequest req) {
        Integer id = bloodTestService.record(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("BloodTest recorded", id));
    }

    /** Function 18 + 19: บันทึกการจ่ายเลือด + อัปเดต BagStatus */
    @PostMapping("/usage")
    @Operation(summary = "บันทึกการจ่ายเลือด + อัปเดตสถานะถุงอัตโนมัติ (Function 18 & 19)")
    public ResponseEntity<ApiResponse<Integer>> recordUsage(@Valid @RequestBody BloodUsageRequest req) {
        Integer id = bloodUsageService.record(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("BloodUsage recorded", id));
    }
}
