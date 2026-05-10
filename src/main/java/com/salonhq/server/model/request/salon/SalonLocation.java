package com.salonhq.server.model.request.salon;

import lombok.Data;

@Data
public class SalonLocation {
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private String country;
}
