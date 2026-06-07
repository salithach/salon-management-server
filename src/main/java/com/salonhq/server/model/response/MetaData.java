package com.salonhq.server.model.response;

import com.salonhq.server.dao.JobRole;
import com.salonhq.server.dao.JobType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetaData {
    List<JobType> jobTypes;
    List<JobRole> jobRoles;
}
