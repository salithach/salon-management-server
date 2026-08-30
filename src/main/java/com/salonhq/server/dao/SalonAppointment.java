package com.salonhq.server.dao;

import com.salonhq.server.model.request.AppointmentRequest;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "appointments")
public class SalonAppointment extends AppointmentRequest {
    @Id
    String id;
}
