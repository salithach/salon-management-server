package com.salonhq.server.dao;

import com.salonhq.server.model.JobDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "jobs")
public class Job {
    @Id
    String id;
    String date;
    String assignee;
    @Builder.Default
    List<JobDetails> jobs = new ArrayList<>();
}
