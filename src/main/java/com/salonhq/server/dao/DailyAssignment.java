package com.salonhq.server.dao;

import com.salonhq.server.model.tenant.TenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "assignments")
public class DailyAssignment extends TenantEntity {
    @Id
    String id;
    List<StaffMember> members;
    String date;
}
