package org.afdb.aikp.modules.dataCollection.presentation.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

import org.afdb.aikp.modules.dataCollection.application.dto.*;
import org.afdb.aikp.modules.dataCollection.application.service.ObservationApplicationService;
import org.afdb.aikp.modules.dataCollection.domain.model.Observation;

@RestController
@RequestMapping("/api/v1/observations")
public class ObservationController {

    private final ObservationApplicationService service;


    public ObservationController(ObservationApplicationService service) {
        this.service = service;
    }

    @PostMapping
    public ObservationResponseDto create(
            @Validated @RequestBody CreateObservationRequestDto request){
        return service.create(request);
    }

    @GetMapping("/collection/{id}")
    public List<Observation> getByCollection(
            @PathVariable UUID id){
        return service.getByCollection(id);
    }
}
