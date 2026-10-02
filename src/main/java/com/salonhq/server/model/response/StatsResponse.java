package com.salonhq.server.model.response;

import com.salonhq.server.model.response.stats.AppointmentStatusStats;
import com.salonhq.server.model.response.stats.CategoryShare;
import com.salonhq.server.model.response.stats.JobStaffAnalytics;
import com.salonhq.server.model.response.stats.DailyRevenuePoint;
import com.salonhq.server.model.response.stats.OverviewStats;
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
    private List<ServiceRevenue> revenueByService;
    private List<DailyRevenuePoint> dailyRevenue;
    private double dailyRevenueChangePercent;
    private List<CategoryShare> servicesMix;
    private AppointmentStatusStats appointmentStatus;
    private JobStaffAnalytics jobStaffAnalytics;
}
