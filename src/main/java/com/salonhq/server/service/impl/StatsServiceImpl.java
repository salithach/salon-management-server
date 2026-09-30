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
import com.salonhq.server.model.response.stats.RevenuePoint;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
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
    private static final int DEFAULT_TRAILING_DAYS = 7;
    private static final int DEFAULT_TRAILING_MONTHS = 7;
    private static final int MAX_TRAILING_DAYS = 90;
    private static final int MAX_TRAILING_MONTHS = 24;

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
    public StatsResponse getStats(String date, Integer trailingMonths, Integer trailingDays) {
        if (date == null || date.isBlank()) {
            date = LocalDate.now().toString();
        }
        LocalDate targetDate = LocalDate.parse(date);
        int resolvedTrailingMonths = resolveWindowSize(trailingMonths, DEFAULT_TRAILING_MONTHS, MAX_TRAILING_MONTHS);
        int resolvedTrailingDays = resolveWindowSize(trailingDays, DEFAULT_TRAILING_DAYS, MAX_TRAILING_DAYS);

        List<SalonAppointment> appointments = appointmentService.getAppointments(date);
        DailyAssignment dailyAssignment = assignmentService.getDailyAssignment(date);

        YearMonth currentMonth = YearMonth.from(targetDate);
        YearMonth previousMonth = currentMonth.minusMonths(1);

        List<MonthlyBreakdownRow> monthlyBreakdown = buildMonthlyBreakdown(currentMonth, resolvedTrailingMonths);
        List<RevenuePoint> monthlyRevenueTrend = monthlyBreakdown.stream()
            .map(row -> RevenuePoint.builder().label(row.getMonth()).revenue(row.getRevenue()).build())
            .collect(Collectors.toList());

        MonthlyBreakdownRow currentMonthRow = monthlyBreakdown.get(monthlyBreakdown.size() - 1);
        MonthlyBreakdownRow previousMonthRow = buildMonthRow(previousMonth);

        List<Job> currentMonthJobs = jobService.getJobsBetween(currentMonth.atDay(1).toString(), currentMonth.atEndOfMonth().toString());

        OverviewStats overview = buildOverview(currentMonthRow, previousMonthRow, currentMonth, previousMonth);
        List<ServiceRevenue> revenueByService = buildRevenueByService(currentMonthJobs);
        List<CategoryShare> servicesMix = buildServicesMix(
            appointmentService.getAppointmentsBetween(currentMonth.atDay(1).toString(), currentMonth.atEndOfMonth().toString())
        );

        WeeklyRevenueResult weeklyRevenueResult = buildWeeklyRevenue(targetDate);
        AppointmentStatusStats appointmentStatus = buildAppointmentStatus(appointments);
        JobStaffAnalytics jobStaffAnalytics = buildJobStaffAnalytics(targetDate, appointments, dailyAssignment, resolvedTrailingDays);

        return StatsResponse.builder()
            .overview(overview)
            .monthlyRevenueTrend(monthlyRevenueTrend)
            .revenueByService(revenueByService)
            .weeklyRevenue(weeklyRevenueResult.points)
            .weeklyRevenueChangePercent(weeklyRevenueResult.changePercent)
            .servicesMix(servicesMix)
            .appointmentStatus(appointmentStatus)
            .monthlyBreakdown(monthlyBreakdown)
            .jobStaffAnalytics(jobStaffAnalytics)
        .build();
    }

    /**
     * Resolves a caller-supplied window size (e.g. ?months=12 or ?days=30), falling back to the
     * default when null/non-positive, and clamping to a sane upper bound to prevent abuse.
     */
    private int resolveWindowSize(Integer requested, int defaultValue, int maxValue) {
        if (requested == null || requested <= 0) {
            return defaultValue;
        }
        return Math.min(requested, maxValue);
    }

    // ---------- Monthly revenue / breakdown ----------

    private List<MonthlyBreakdownRow> buildMonthlyBreakdown(YearMonth currentMonth, int trailingMonths) {
        List<MonthlyBreakdownRow> rows = new ArrayList<>();
        for (int i = trailingMonths - 1; i >= 0; i--) {
            rows.add(buildMonthRow(currentMonth.minusMonths(i)));
        }
        return rows;
    }

    private MonthlyBreakdownRow buildMonthRow(YearMonth month) {
        String start = month.atDay(1).toString();
        String end = month.atEndOfMonth().toString();
        List<Job> monthJobs = jobService.getJobsBetween(start, end);
        List<SalonAppointment> monthAppointments = appointmentService.getAppointmentsBetween(start, end);
        double revenue = sumJobRevenue(monthJobs);
        int appointmentsCount = monthAppointments.size();
        double avgTicket = appointmentsCount == 0 ? 0 : round1(revenue / appointmentsCount);
        String label = month.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
        return MonthlyBreakdownRow.builder()
            .month(label)
            .revenue(round1(revenue))
            .appointments(appointmentsCount)
            .avgTicket(avgTicket)
        .build();
    }

    // ---------- Overview ----------

    private OverviewStats buildOverview(
        MonthlyBreakdownRow currentMonthRow,
        MonthlyBreakdownRow previousMonthRow,
        YearMonth currentMonth,
        YearMonth previousMonth
    ) {
        int newClientsCurrentMonth = clientService.getClientsCreatedBetween(
            currentMonth.atDay(1).toString(), currentMonth.atEndOfMonth().toString()
        ).size();
        int newClientsPreviousMonth = clientService.getClientsCreatedBetween(
            previousMonth.atDay(1).toString(), previousMonth.atEndOfMonth().toString()
        ).size();

        double avgTicket = currentMonthRow.getAvgTicket();
        double previousAvgTicket = previousMonthRow.getAvgTicket();

        return OverviewStats.builder()
            .monthlyRevenue(currentMonthRow.getRevenue())
            .monthlyRevenueChangePercent(percentChange(currentMonthRow.getRevenue(), previousMonthRow.getRevenue()))
            .totalAppointments(currentMonthRow.getAppointments())
            .appointmentsChangePercent(percentChange(currentMonthRow.getAppointments(), previousMonthRow.getAppointments()))
            .newClients(newClientsCurrentMonth)
            .newClientsChange(newClientsCurrentMonth - newClientsPreviousMonth)
            .avgTicket(avgTicket)
            .avgTicketChangePercent(percentChange(avgTicket, previousAvgTicket))
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

    // ---------- Weekly revenue ----------

    private static class WeeklyRevenueResult {
        List<RevenuePoint> points;
        double changePercent;
    }

    private WeeklyRevenueResult buildWeeklyRevenue(LocalDate targetDate) {
        LocalDate weekStart = targetDate.minusDays(targetDate.getDayOfWeek().getValue() - 1L);
        LocalDate weekEnd = weekStart.plusDays(6);
        LocalDate prevWeekStart = weekStart.minusWeeks(1);
        LocalDate prevWeekEnd = weekEnd.minusWeeks(1);

        List<Job> currentWeekJobs = jobService.getJobsBetween(weekStart.toString(), weekEnd.toString());
        List<Job> prevWeekJobs = jobService.getJobsBetween(prevWeekStart.toString(), prevWeekEnd.toString());

        Map<String, Double> revenueByDate = new LinkedHashMap<>();
        for (Job job : currentWeekJobs) {
            revenueByDate.merge(job.getDate(), sumJobRevenue(List.of(job)), Double::sum);
        }

        List<RevenuePoint> points = new ArrayList<>();
        for (LocalDate d = weekStart; !d.isAfter(weekEnd); d = d.plusDays(1)) {
            String label = d.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
            double revenue = revenueByDate.getOrDefault(d.toString(), 0.0);
            points.add(RevenuePoint.builder().label(label).revenue(round1(revenue)).build());
        }

        double currentWeekTotal = sumJobRevenue(currentWeekJobs);
        double prevWeekTotal = sumJobRevenue(prevWeekJobs);

        WeeklyRevenueResult result = new WeeklyRevenueResult();
        result.points = points;
        result.changePercent = percentChange(currentWeekTotal, prevWeekTotal);
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
        LocalDate targetDate,
        List<SalonAppointment> dayAppointments,
        DailyAssignment dailyAssignment,
        int trailingDays
    ) {
        List<SalonAppointment> nonCancelled = dayAppointments.stream()
            .filter(a -> !CANCELLED.equalsIgnoreCase(a.getStatus()))
            .collect(Collectors.toList());

        int totalJobs = nonCancelled.size();
        int confirmedJobs = countByStatus(dayAppointments, CONFIRMED);

        Set<String> staffWithJobsSet = nonCancelled.stream()
            .map(SalonAppointment::getAssignee)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
        int staffWithJobs = staffWithJobsSet.size();

        // "Active Staff" reflects who was actually scheduled/on duty that day (the daily
        // assignment roster), not merely who happened to receive a job. Fall back to staff
        // derived from appointments only when no roster has been recorded for the day.
        List<String> rosterNames = dailyAssignment != null && dailyAssignment.getMembers() != null
            ? dailyAssignment.getMembers().stream().map(StaffMember::getName).filter(Objects::nonNull).toList()
            : List.of();
        int activeStaff = !rosterNames.isEmpty()
            ? (int) rosterNames.stream().distinct().count()
            : staffWithJobs;
        double avgJobsPerStaff = activeStaff == 0 ? 0 : round1((double) totalJobs / activeStaff);

        List<StaffWorkRow> staffWorkDistribution = buildStaffWorkDistribution(nonCancelled, totalJobs, rosterNames);

        LocalDate trailingStart = targetDate.minusDays(trailingDays - 1L);
        List<SalonAppointment> trailingAppointments = appointmentService.getAppointmentsBetween(trailingStart.toString(), targetDate.toString());
        Map<String, List<SalonAppointment>> byDate = trailingAppointments.stream()
            .collect(Collectors.groupingBy(SalonAppointment::getDate));

        List<JobActivityPoint> dailyJobActivity = new ArrayList<>();
        List<DailyBreakdownRow> dailyJobBreakdown = new ArrayList<>();
        for (LocalDate d = trailingStart; !d.isAfter(targetDate); d = d.plusDays(1)) {
            List<SalonAppointment> dayList = byDate.getOrDefault(d.toString(), List.of());
            List<SalonAppointment> dayNonCancelled = dayList.stream()
                .filter(a -> !CANCELLED.equalsIgnoreCase(a.getStatus()))
                .toList();
            int dayConfirmed = countByStatus(dayList, CONFIRMED);
            int dayPending = countByStatus(dayList, PENDING);
            int dayCancelled = countByStatus(dayList, CANCELLED);
            dailyJobActivity.add(JobActivityPoint.builder().date(d.toString()).jobCount(dayNonCancelled.size()).build());
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
