package com.salonhq.server.model.response.stats;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DailyBreakdownRow {
    private String date;
    private int jobs;
    private int confirmed;
    private int pending;
    private int cancelled;
}

