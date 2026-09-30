package com.salonhq.server.model.response.stats;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OverviewStats {
    private double monthlyRevenue;
    private double monthlyRevenueChangePercent;
    private int totalAppointments;
    private double appointmentsChangePercent;
    private int monthlyJobs;
    private double monthlyJobsChangePercent;
    private int newClients;
    private int newClientsChange;
    private double avgJobRevenue;
    private double avgJobRevenueChangePercent;
}

