package com.salonhq.server.model.request;

import com.salonhq.server.dao.StaffMember;
import lombok.Data;

import java.util.List;

@Data
public class JobRequest {
    String date;
    Double price;
    String description;
    List<String> service;
    StaffMember assignee;
}
