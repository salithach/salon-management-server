package com.salonhq.server.controller;

import com.salonhq.server.dao.SalonType;
import com.salonhq.server.model.KeyValueCategoryPair;
import com.salonhq.server.model.response.EnvelopedResponse;
import com.salonhq.server.service.MetaDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/salons")
public class SalonController {

    private final MetaDataService metaDataService;

    @Autowired
    public SalonController(MetaDataService metaDataService) {
        this.metaDataService = metaDataService;
    }


    @GetMapping("/types")
    public ResponseEntity<?> getSalonTypes() {
        List<SalonType> salonTypesResponse = metaDataService.getSalonTypesList();
        EnvelopedResponse<Object> response = EnvelopedResponse.builder()
            .data(salonTypesResponse)
            .errors(List.of())
        .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/types")
    public ResponseEntity<?> addSalonTypes(@RequestBody List<KeyValueCategoryPair> salonTypeRequest) {
        List<SalonType> salonTypesResponse = metaDataService.createSalonTypes(salonTypeRequest);
        EnvelopedResponse<Object> response = EnvelopedResponse.builder()
            .data(salonTypesResponse)
            .errors(List.of())
        .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
