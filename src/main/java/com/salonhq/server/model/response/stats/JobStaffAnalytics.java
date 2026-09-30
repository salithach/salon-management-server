package com.salonhq.server.model.response.stats;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobStaffAnalytics {
    private int totalJobs;
    private int confirmedJobs;
    private int activeStaff;
    private int staffWithJobs;
    private double avgJobsPerStaff;
    private List<JobActivityPoint> dailyJobActivity;
    private List<StaffWorkRow> staffWorkDistribution;
    private List<DailyBreakdownRow> dailyJobBreakdown;
}

