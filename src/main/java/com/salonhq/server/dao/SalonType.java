package com.salonhq.server.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.salonhq.server.model.KeyValueCategoryPair;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "salonTypes")
public class SalonType extends KeyValueCategoryPair {
    @Id
    @JsonIgnore
    String id;
}
