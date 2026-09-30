package com.salonhq.server.model.response;

import com.salonhq.server.model.response.stats.AppointmentStatusStats;
import com.salonhq.server.model.response.stats.CategoryShare;
import com.salonhq.server.model.response.stats.JobStaffAnalytics;
import com.salonhq.server.model.response.stats.MonthlyBreakdownRow;
import com.salonhq.server.model.response.stats.OverviewStats;
import com.salonhq.server.model.response.stats.RevenuePoint;
import com.salonhq.server.model.response.stats.ServiceRevenue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatsResponse {
private OverviewStats overview;
    private List<RevenuePoint> monthlyRevenueTrend;
    private List<ServiceRevenue> revenueByService;
    private List<RevenuePoint> weeklyRevenue;
    private double weeklyRevenueChangePercent;
    private List<CategoryShare> servicesMix;
    private AppointmentStatusStats appointmentStatus;
    private List<MonthlyBreakdownRow> monthlyBreakdown;
    private JobStaffAnalytics jobStaffAnalytics;
}
