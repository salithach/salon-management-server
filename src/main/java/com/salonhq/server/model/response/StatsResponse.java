package com.salonhq.server.model.response;

import com.salonhq.server.dao.DailyAssignment;
import com.salonhq.server.dao.Job;
import com.salonhq.server.dao.SalonAppointment;
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
    private List<SalonAppointment> appointments;
    private List<Job> jobs;
    private DailyAssignment dailyAssignment;
}
