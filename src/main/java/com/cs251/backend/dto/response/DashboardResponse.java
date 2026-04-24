package com.cs251.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Function 21, 22, 23 */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class DashboardResponse {
    private int month;
    private int year;
    private int totalVolumeIn;    // Function 21
    private int totalVolumeUsed;  // Function 22
    private int totalVolumeLost;  // Function 23
}
