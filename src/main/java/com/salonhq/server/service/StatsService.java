package com.salonhq.server.service;

import com.salonhq.server.model.response.StatsResponse;

public interface StatsService {
    StatsResponse getStats(String startDate, String endDate);
}
