package com.salonhq.server.model.response.stats;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DailyRevenuePoint {
    private String date;
    private double revenue;
}
