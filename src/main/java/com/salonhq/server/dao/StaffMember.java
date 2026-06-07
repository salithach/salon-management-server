package com.salonhq.server.dao;

import com.salonhq.server.model.tenant.TenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "staff")
public class StaffMember extends TenantEntity {
    @Id
    String id;
    String username;
    String name;
    String email;
    String phone;
    String address;
    String specialty;
    Role role;
}
