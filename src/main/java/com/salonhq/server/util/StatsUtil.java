package com.salonhq.server.util;

import com.salonhq.server.dao.Job;
import com.salonhq.server.dao.SalonAppointment;
import com.salonhq.server.model.JobDetails;

import java.util.List;

/**
 * Pure, stateless helper methods shared across stats computations (StatsServiceImpl).
 * Kept free of any repository/service dependencies so they can be unit tested in isolation.
 */
public class StatsUtil {

    private StatsUtil() {}

    /**
     * Counts how many appointments in the given list have the specified status (case-insensitive).
     */
    public static int countByStatus(List<SalonAppointment> appointments, String status) {
        return (int) appointments.stream()
            .filter(a -> status.equalsIgnoreCase(a.getStatus()))
            .count();
    }

    /**
     * Sums the price of every job detail entry across the given jobs.
     */
    public static double sumJobRevenue(List<Job> jobs) {
        double total = 0;
        for (Job job : jobs) {
            if (job.getJobs() == null) continue;
            for (JobDetails jobDetails : job.getJobs()) {
                if (jobDetails.getPrice() != null) {
                    total += jobDetails.getPrice();
                }
            }
        }
        return total;
    }

    /**
     * Computes the percentage change between a current and previous value, rounded to 1 decimal.
     * When previous is 0, returns 100 if current is non-zero, otherwise 0.
     */
    public static double percentChange(double current, double previous) {
        if (previous == 0) {
            return current == 0 ? 0 : 100.0;
        }
        return round1(((current - previous) / previous) * 100.0);
    }

    /**
     * Rounds a double to 1 decimal place.
     */
    public static double round1(double value) {
        return Math.round(value * 10.0) / 10.0;
    }
}

