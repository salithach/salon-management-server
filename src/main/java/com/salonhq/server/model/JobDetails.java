package com.salonhq.server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobDetails {
    Double price;
    List<String> services;
    @Builder.Default
    String description = "N/A";
}
