package com.salonhq.server.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.salonhq.server.model.KeyValuePair;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "jobRoles")
public class JobRole extends KeyValuePair {
    @Id
    @JsonIgnore
    String id;
}
