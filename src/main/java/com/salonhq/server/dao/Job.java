package com.salonhq.server.dao;

import com.salonhq.server.model.JobDetails;
import com.salonhq.server.model.tenant.TenantEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "jobs")
public class Job extends TenantEntity {
    @Id
    String id;
    String date;
    String assignee;
    @Builder.Default
    List<JobDetails> jobs = new ArrayList<>();
}
