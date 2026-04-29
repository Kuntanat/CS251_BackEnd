package com.cs251.backend.controller;

import com.cs251.backend.dto.request.DeferralRequest;
import com.cs251.backend.dto.response.ApiResponse;
import com.cs251.backend.dto.response.DashboardResponse;
import com.cs251.backend.service.DeferralService;
import com.cs251.backend.service.DashboardService;
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
@RequestMapping("/api")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class ManagementController {

    private final DeferralService deferralService;
    private final DashboardService dashboardService;

    /** Function 20: บันทึกการระงับ */
    @PostMapping("/deferrals")
    @Tag(name = "Deferral", description = "จัดการการระงับสิทธิ์")
    @Operation(summary = "บันทึกการระงับสิทธิ์ผู้บริจาค (Function 20)")
    public ResponseEntity<ApiResponse<Integer>> recordDeferral(@Valid @RequestBody DeferralRequest req) {
        Integer id = deferralService.record(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Deferral recorded", id));
    }

    /** Function 21, 22, 23: Dashboard */
    @GetMapping("/dashboard")
    @Tag(name = "Dashboard", description = "สรุปข้อมูลสำหรับ Dashboard")
    @Operation(summary = "สรุปปริมาณเลือดรับเข้า/ใช้/สูญเสียรายเดือน (Function 21-23)")
    public ResponseEntity<ApiResponse<DashboardResponse>> getDashboard(
            @RequestParam int month,
            @RequestParam int year) {
        return ResponseEntity.ok(ApiResponse.ok(dashboardService.getSummary(month, year)));
    }

    /** Function 24: ตารางรายงานการรับบริจาค */
    @GetMapping("/reports/donations")
    @Tag(name = "Dashboard")
    @Operation(summary = "รายงานการรับบริจาครายเดือน (Function 24)")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getDonationReport(
            @RequestParam int month,
            @RequestParam int year) {
        return ResponseEntity.ok(ApiResponse.ok(dashboardService.getDonationReport(month, year)));
    }

    /** Dashboard counts: active donors, available bags, expiring, dispensed today */
    @GetMapping("/dashboard/stats")
    @Tag(name = "Dashboard")
    @Operation(summary = "นับจำนวนสถิติหลักสำหรับ Dashboard")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStats() {
        return ResponseEntity.ok(ApiResponse.ok(dashboardService.getStats()));
    }

    /** รายงานการจ่ายเลือดรายวัน — ใช้สำหรับ Chart จ่ายออก */
    @GetMapping("/reports/usage")
    @Tag(name = "Dashboard")
    @Operation(summary = "รายงานการจ่ายเลือดรายวันในเดือน (สำหรับกราฟเปรียบเทียบ)")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getUsageReport(
            @RequestParam int month,
            @RequestParam int year) {
        return ResponseEntity.ok(ApiResponse.ok(dashboardService.getUsageReport(month, year)));
    }

    /** Blood stock by group/Rh */
    @GetMapping("/dashboard/blood-stock")
    @Tag(name = "Dashboard")
    @Operation(summary = "สต็อกเลือดจำแนกตามกรุ๊ปและ Rh")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getBloodStock() {
        return ResponseEntity.ok(ApiResponse.ok(dashboardService.getBloodStock()));
    }
}
