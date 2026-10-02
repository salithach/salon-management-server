package com.salonhq.server.model.response.stats;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyBreakdownRow {
    private String month;
    private double revenue;
    private int appointments;
    private double avgJobRevenue;
    private int jobs;
}

