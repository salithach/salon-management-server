package com.salonhq.server.service.impl;

import com.salonhq.server.dao.DailyAssignment;
import com.salonhq.server.dao.Job;
import com.salonhq.server.dao.JobType;
import com.salonhq.server.dao.SalonAppointment;
import com.salonhq.server.dao.StaffMember;
import com.salonhq.server.model.JobDetails;
import com.salonhq.server.model.response.StatsResponse;
import com.salonhq.server.model.response.stats.AppointmentStatusStats;
import com.salonhq.server.model.response.stats.CategoryShare;
import com.salonhq.server.model.response.stats.DailyBreakdownRow;
import com.salonhq.server.model.response.stats.JobActivityPoint;
import com.salonhq.server.model.response.stats.JobStaffAnalytics;
import com.salonhq.server.model.response.stats.MonthlyBreakdownRow;
import com.salonhq.server.model.response.stats.OverviewStats;
import com.salonhq.server.model.response.stats.DailyRevenuePoint;
import com.salonhq.server.model.response.stats.ServiceRevenue;
import com.salonhq.server.model.response.stats.StaffWorkRow;
import com.salonhq.server.service.AppointmentService;
import com.salonhq.server.service.AssignmentService;
import com.salonhq.server.service.ClientService;
import com.salonhq.server.service.JobService;
import com.salonhq.server.service.MetaDataService;
import com.salonhq.server.service.StatsService;
import com.salonhq.server.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.salonhq.server.util.StatsUtil.countByStatus;
import static com.salonhq.server.util.StatsUtil.percentChange;
import static com.salonhq.server.util.StatsUtil.round1;
import static com.salonhq.server.util.StatsUtil.sumJobRevenue;

@Service
public class StatsServiceImpl implements StatsService {

    private static final String CONFIRMED = Constants.AppointmentStatus.CONFIRMED;
    private static final String PENDING = Constants.AppointmentStatus.PENDING;
    private static final String CANCELLED = Constants.AppointmentStatus.CANCELLED;
    private static final String OTHERS = "Others";
    private static final String OTHER_CATEGORY = "Other";
    private static final int SERVICE_REVENUE_TOP_N = 4;
    private static final int MAX_RANGE_DAYS = 366;

    private final AppointmentService appointmentService;
    private final JobService jobService;
    private final AssignmentService assignmentService;
    private final MetaDataService metaDataService;
    private final ClientService clientService;

    @Autowired
    public StatsServiceImpl(
        AppointmentService appointmentService,
        JobService jobService,
        AssignmentService assignmentService,
        MetaDataService metaDataService,
        ClientService clientService
    ) {
        this.appointmentService = appointmentService;
        this.jobService = jobService;
        this.assignmentService = assignmentService;
        this.metaDataService = metaDataService;
        this.clientService = clientService;
    }

    @Override
    public StatsResponse getStats(String startDate, String endDate) {
        LocalDate end = (endDate == null || endDate.isBlank()) ? LocalDate.now() : LocalDate.parse(endDate);
        LocalDate start = (startDate == null || startDate.isBlank()) ? end.withDayOfMonth(1) : LocalDate.parse(startDate);
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("startDate must not be after endDate");
        }
        long days = ChronoUnit.DAYS.between(start, end) + 1;
        if (days > MAX_RANGE_DAYS) {
            throw new IllegalArgumentException("Date range must not exceed " + MAX_RANGE_DAYS + " days");
        }
        LocalDate prevEnd = start.minusDays(1);
        LocalDate prevStart = start.minusDays(days);
        List<SalonAppointment> appointments = appointmentService.getAppointmentsBetween(start.toString(), end.toString());
        List<Job> rangeJobs = jobService.getJobsBetween(start.toString(), end.toString());
        MonthlyBreakdownRow currentRow = buildPeriodRow("Range", start, end);
        MonthlyBreakdownRow previousRow = buildPeriodRow("Previous", prevStart, prevEnd);
        OverviewStats overview = buildOverview(currentRow, previousRow, start, end, prevStart, prevEnd);
        List<ServiceRevenue> revenueByService = buildRevenueByService(rangeJobs);
        List<CategoryShare> servicesMix = buildServicesMix(appointments);
        DailyRevenueResult dailyRevenueResult = buildDailyRevenue(start, end, prevStart, prevEnd, rangeJobs);
        AppointmentStatusStats appointmentStatus = buildAppointmentStatus(appointments);
        JobStaffAnalytics jobStaffAnalytics = buildJobStaffAnalytics(start, end, appointments, rangeJobs);
        return StatsResponse.builder()
            .overview(overview)
            .revenueByService(revenueByService)
            .dailyRevenue(dailyRevenueResult.points)
            .dailyRevenueChangePercent(dailyRevenueResult.changePercent)
            .servicesMix(servicesMix)
            .appointmentStatus(appointmentStatus)
            .jobStaffAnalytics(jobStaffAnalytics)
        .build();
    }
    // ---------- Period totals (used by overview) ----------
    private MonthlyBreakdownRow buildPeriodRow(String label, LocalDate from, LocalDate to) {
        List<Job> periodJobs = jobService.getJobsBetween(from.toString(), to.toString());
        List<SalonAppointment> periodAppointments = appointmentService.getAppointmentsBetween(from.toString(), to.toString());
        double revenue = sumJobRevenue(periodJobs);
        int appointmentsCount = periodAppointments.size();
        double avgJobRevenue = appointmentsCount == 0 ? 0 : round1(revenue / appointmentsCount);
        int totalJobs = 0;
        for (Job job : periodJobs) {
            if (job.getJobs() != null) {
                totalJobs += job.getJobs().size();
            }
        }
        return MonthlyBreakdownRow.builder()
            .month(label)
            .revenue(round1(revenue))
            .appointments(appointmentsCount)
            .avgJobRevenue(avgJobRevenue)
            .jobs(totalJobs)
        .build();
    }
    // ---------- Overview ----------
    private OverviewStats buildOverview(
        MonthlyBreakdownRow currentRow,
        MonthlyBreakdownRow previousRow,
        LocalDate start,
        LocalDate end,
        LocalDate prevStart,
        LocalDate prevEnd
    ) {
        int newClientsCurrent = clientService.getClientsCreatedBetween(start.toString(), end.toString()).size();
        int newClientsPrevious = clientService.getClientsCreatedBetween(prevStart.toString(), prevEnd.toString()).size();
        double avgJobRevenue = currentRow.getAvgJobRevenue();
        double previousAvgJobRevenue = previousRow.getAvgJobRevenue();
        return OverviewStats.builder()
            .monthlyRevenue(currentRow.getRevenue())
            .monthlyRevenueChangePercent(percentChange(currentRow.getRevenue(), previousRow.getRevenue()))
            .totalAppointments(currentRow.getAppointments())
            .appointmentsChangePercent(percentChange(currentRow.getAppointments(), previousRow.getAppointments()))
            .monthlyJobs(currentRow.getJobs())
            .monthlyJobsChangePercent(percentChange(currentRow.getJobs(), previousRow.getJobs()))
            .newClients(newClientsCurrent)
            .newClientsChange(newClientsCurrent - newClientsPrevious)
            .avgJobRevenue(avgJobRevenue)
            .avgJobRevenueChangePercent(percentChange(avgJobRevenue, previousAvgJobRevenue))
        .build();
    }
    // ---------- Revenue by service ----------

    private List<ServiceRevenue> buildRevenueByService(List<Job> monthJobs) {
        Map<String, Double> revenueByService = new LinkedHashMap<>();
        for (Job job : monthJobs) {
            if (job.getJobs() == null) continue;
            for (JobDetails jobDetails : job.getJobs()) {
                List<String> services = jobDetails.getServices();
                Double price = jobDetails.getPrice();
                if (services == null || services.isEmpty() || price == null) continue;
                double splitPrice = price / services.size();
                for (String service : services) {
                    revenueByService.merge(service, splitPrice, Double::sum);
                }
            }
        }

        List<Map.Entry<String, Double>> sorted = revenueByService.entrySet().stream()
            .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
            .toList();

        List<ServiceRevenue> result = new ArrayList<>();
        double othersTotal = 0;
        for (int i = 0; i < sorted.size(); i++) {
            Map.Entry<String, Double> entry = sorted.get(i);
            if (i < SERVICE_REVENUE_TOP_N) {
                result.add(ServiceRevenue.builder().service(entry.getKey()).revenue(round1(entry.getValue())).build());
            } else {
                othersTotal += entry.getValue();
            }
        }
        if (othersTotal > 0) {
            result.add(ServiceRevenue.builder().service(OTHERS).revenue(round1(othersTotal)).build());
        }
        return result;
    }

    // ---------- Services mix (appointments by category) ----------

    private List<CategoryShare> buildServicesMix(List<SalonAppointment> monthAppointments) {
        Map<String, String> serviceToCategory = buildServiceCategoryMap();
        Map<String, Long> countByCategory = new LinkedHashMap<>();
        long total = 0;
        for (SalonAppointment appointment : monthAppointments) {
            if (appointment.getServices() == null) continue;
            for (String service : appointment.getServices()) {
                String category = serviceToCategory.getOrDefault(service, OTHER_CATEGORY);
                countByCategory.merge(category, 1L, Long::sum);
                total++;
            }
        }

        long finalTotal = total;
        return countByCategory.entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .map(entry -> CategoryShare.builder()
                .category(entry.getKey())
                .count(entry.getValue())
                .percent(finalTotal == 0 ? 0 : round1(entry.getValue() * 100.0 / finalTotal))
            .build())
            .collect(Collectors.toList());
    }

    private Map<String, String> buildServiceCategoryMap() {
        List<JobType> jobTypes = metaDataService.getJobTypesList();
        Map<String, String> map = new LinkedHashMap<>();
        if (jobTypes != null) {
            for (JobType jobType : jobTypes) {
                if (jobType.getValue() != null) {
                    map.put(jobType.getValue(), jobType.getCategory());
                }
                if (jobType.getKey() != null) {
                    map.putIfAbsent(jobType.getKey(), jobType.getCategory());
                }
            }
        }
        return map;
    }

    // ---------- Daily revenue ----------

    private static class DailyRevenueResult {
        List<DailyRevenuePoint> points;
        double changePercent;
    }

    private DailyRevenueResult buildDailyRevenue(
        LocalDate start, LocalDate end, LocalDate prevStart, LocalDate prevEnd, List<Job> rangeJobs
    ) {
        List<Job> prevJobs = jobService.getJobsBetween(prevStart.toString(), prevEnd.toString());
        Map<String, Double> revenueByDate = new LinkedHashMap<>();
        for (Job job : rangeJobs) {
            revenueByDate.merge(job.getDate(), sumJobRevenue(List.of(job)), Double::sum);
        }
        List<DailyRevenuePoint> points = new ArrayList<>();
        for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
            double revenue = revenueByDate.getOrDefault(d.toString(), 0.0);
            points.add(DailyRevenuePoint.builder().date(d.toString()).revenue(round1(revenue)).build());
        }
        DailyRevenueResult result = new DailyRevenueResult();
        result.points = points;
        result.changePercent = percentChange(sumJobRevenue(rangeJobs), sumJobRevenue(prevJobs));
        return result;
    }
    // ---------- Appointment status (single day) ----------

    private AppointmentStatusStats buildAppointmentStatus(List<SalonAppointment> dayAppointments) {
        int confirmed = countByStatus(dayAppointments, CONFIRMED);
        int pending = countByStatus(dayAppointments, PENDING);
        return AppointmentStatusStats.builder().confirmed(confirmed).pending(pending).build();
    }

    // ---------- Job & staff analytics (single day + trailing window) ----------

    private JobStaffAnalytics buildJobStaffAnalytics(
        LocalDate start,
        LocalDate end,
        List<SalonAppointment> dayAppointments,
        List<Job> rangeJobs
    ) {
        List<SalonAppointment> nonCancelled = dayAppointments.stream()
            .filter(a -> !CANCELLED.equalsIgnoreCase(a.getStatus()))
            .collect(Collectors.toList());
        // Count actual jobs (JobDetails entries) within the range, not appointments
        int totalJobs = 0;
        for (Job job : rangeJobs) {
            if (job.getJobs() != null) {
                totalJobs += job.getJobs().size();
            }
        }
        int confirmedJobs = countByStatus(dayAppointments, CONFIRMED);
        Set<String> staffWithJobsSet = nonCancelled.stream()
            .map(SalonAppointment::getAssignee)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
        int staffWithJobs = staffWithJobsSet.size();
        // Active staff = distinct staff on the daily roster across the range; fall back to
        // staff derived from appointments when no roster was recorded.
        Set<String> rosterSet = new LinkedHashSet<>();
        for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
            DailyAssignment da = assignmentService.getDailyAssignment(d.toString());
            if (da != null && da.getMembers() != null) {
                da.getMembers().stream().map(StaffMember::getName).filter(Objects::nonNull).forEach(rosterSet::add);
            }
        }
        List<String> rosterNames = new ArrayList<>(rosterSet);
        int activeStaff = !rosterNames.isEmpty() ? rosterNames.size() : staffWithJobs;
        double avgJobsPerStaff = activeStaff == 0 ? 0 : round1((double) totalJobs / activeStaff);
        List<StaffWorkRow> staffWorkDistribution = buildStaffWorkDistribution(nonCancelled, totalJobs, rosterNames);


        // Daily Job Activity reflects jobs actually done/logged (Job records), not scheduled
        // appointments. Each JobDetails entry within a Job represents one completed job.
        List<Job> trailingJobs = rangeJobs;
        Map<String, Integer> jobsDoneByDate = new LinkedHashMap<>();
        for (Job job : trailingJobs) {
            int count = job.getJobs() == null ? 0 : job.getJobs().size();
            jobsDoneByDate.merge(job.getDate(), count, Integer::sum);
        }

        Map<String, List<SalonAppointment>> byDate = dayAppointments.stream()
            .collect(Collectors.groupingBy(SalonAppointment::getDate));

        List<JobActivityPoint> dailyJobActivity = new ArrayList<>();
        List<DailyBreakdownRow> dailyJobBreakdown = new ArrayList<>();
        for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
            List<SalonAppointment> dayList = byDate.getOrDefault(d.toString(), List.of());
            List<SalonAppointment> dayNonCancelled = dayList.stream()
                .filter(a -> !CANCELLED.equalsIgnoreCase(a.getStatus()))
                .toList();
            int dayConfirmed = countByStatus(dayList, CONFIRMED);
            int dayPending = countByStatus(dayList, PENDING);
            int dayCancelled = countByStatus(dayList, CANCELLED);
            dailyJobActivity.add(JobActivityPoint.builder()
                .date(d.toString())
                .jobCount(jobsDoneByDate.getOrDefault(d.toString(), 0))
            .build());
            dailyJobBreakdown.add(DailyBreakdownRow.builder()
                .date(d.toString())
                .jobs(dayNonCancelled.size())
                .confirmed(dayConfirmed)
                .pending(dayPending)
                .cancelled(dayCancelled)
            .build());
        }

        return JobStaffAnalytics.builder()
            .totalJobs(totalJobs)
            .confirmedJobs(confirmedJobs)
            .activeStaff(activeStaff)
            .staffWithJobs(staffWithJobs)
            .avgJobsPerStaff(avgJobsPerStaff)
            .dailyJobActivity(dailyJobActivity)
            .staffWorkDistribution(staffWorkDistribution)
            .dailyJobBreakdown(dailyJobBreakdown)
        .build();
    }

    private List<StaffWorkRow> buildStaffWorkDistribution(List<SalonAppointment> nonCancelled, int totalJobs, List<String> rosterNames) {
        Map<String, List<SalonAppointment>> byAssignee = nonCancelled.stream()
            .filter(a -> a.getAssignee() != null)
            .collect(Collectors.groupingBy(SalonAppointment::getAssignee, LinkedHashMap::new, Collectors.toList()));

        // Seed the distribution with every staff member assigned to work that day (via the daily
        // roster), so staff who were scheduled but received zero jobs still show up with 0 workload.
        Map<String, List<SalonAppointment>> byStaffName = new LinkedHashMap<>();
        for (String rosterName : rosterNames) {
            byStaffName.put(rosterName, new ArrayList<>());
        }
        byAssignee.forEach((assignee, staffAppointments) ->
            byStaffName.merge(assignee, staffAppointments, (existing, incoming) -> incoming)
        );

        return byStaffName.entrySet().stream()
            .map(entry -> {
                List<SalonAppointment> staffAppointments = entry.getValue();
                int jobs = staffAppointments.size();
                int confirmed = countByStatus(staffAppointments, CONFIRMED);
                int pending = countByStatus(staffAppointments, PENDING);
                double workloadPercent = totalJobs == 0 ? 0 : round1(jobs * 100.0 / totalJobs);
                return StaffWorkRow.builder()
                    .staff(entry.getKey())
                    .jobs(jobs)
                    .confirmed(confirmed)
                    .pending(pending)
                    .workloadPercent(workloadPercent)
                .build();
            })
            .sorted(Comparator.comparingInt(StaffWorkRow::getJobs).reversed())
            .collect(Collectors.toList());
    }
}
