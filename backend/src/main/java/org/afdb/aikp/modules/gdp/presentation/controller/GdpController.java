package org.afdb.aikp.modules.gdp.presentation.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.afdb.aikp.modules.gdp.application.service.GdpApplicationService;
import org.afdb.aikp.modules.gdp.domain.repository.GdpReferenceRepository.GdpReferenceRow;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Validated
@RequestMapping("/api/v1/gdp")
public class GdpController {

    private final GdpApplicationService service;

    public GdpController(GdpApplicationService service) {
        this.service = service;
    }

    @PreAuthorize("hasAnyRole('ADMIN','COORDINATOR','VALIDATOR','DATA_PROVIDER')")
    @GetMapping
    public List<GdpReferenceRow> getGdp(
            @RequestParam(name = "iso3", required = false)
            @Pattern(regexp = "[A-Za-z]{3}") String iso3,
            @RequestParam(name = "year", required = false)
            @Min(1950) @Max(2100) Integer year) {
        return service.getGdp(iso3, year);
    }
}
