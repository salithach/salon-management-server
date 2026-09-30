package com.salonhq.server.service.impl;

import com.salonhq.server.dao.DailyAssignment;
import com.salonhq.server.dao.Job;
import com.salonhq.server.dao.SalonAppointment;
import com.salonhq.server.model.response.StatsResponse;
import com.salonhq.server.service.AppointmentService;
import com.salonhq.server.service.AssignmentService;
import com.salonhq.server.service.JobService;
import com.salonhq.server.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class StatsServiceImpl implements StatsService {

    private final AppointmentService appointmentService;
    private final JobService jobService;
    private final AssignmentService assignmentService;

    @Autowired
    public StatsServiceImpl(
        AppointmentService appointmentService,
        JobService jobService,
        AssignmentService assignmentService
    ) {
        this.appointmentService = appointmentService;
        this.jobService = jobService;
        this.assignmentService = assignmentService;
    }

    @Override
    public StatsResponse getStats(String date) {
        if (date == null || date.isBlank()) {
            date = LocalDate.now().toString();
        }
        List<SalonAppointment> appointments = appointmentService.getAppointments(date);
        List<Job> jobs = jobService.getJobs(date);
        DailyAssignment dailyAssignment = assignmentService.getDailyAssignment(date);
        return StatsResponse.builder()
            .appointments(appointments)
            .jobs(jobs)
            .dailyAssignment(dailyAssignment)
        .build();
    }
}
