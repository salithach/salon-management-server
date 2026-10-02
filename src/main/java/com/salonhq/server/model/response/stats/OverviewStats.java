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
    private int totalAppointments;
    private int monthlyJobs;
    private int newClients;
    private int newClientsChange;
    private double avgJobRevenue;
}

