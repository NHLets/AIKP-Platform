package org.afdb.aikp.modules.country.presentation.contract;

import org.afdb.aikp.modules.country.application.response.CountryResponse;
import org.afdb.aikp.modules.country.application.response.CountrySummary;
import org.afdb.aikp.modules.country.presentation.request.CreateCountryRequest;
import org.afdb.aikp.modules.country.presentation.request.UpdateCountryRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

/**
 * Contract for Country REST operations.
 *
 * <p>
 * This interface defines the REST contract independently of the
 * Spring MVC/OpenAPI annotations, which are declared on the
 * implementing controller.
 * </p>
 */
public interface CountryApi {

    ResponseEntity<CountryResponse> create(
            CreateCountryRequest request);

    ResponseEntity<CountryResponse> get(
            UUID id);

    ResponseEntity<List<CountrySummary>> getAll();

    ResponseEntity<List<CountrySummary>> getActive();

    ResponseEntity<CountryResponse> update(
            UUID id,
            UpdateCountryRequest request);

    ResponseEntity<CountryResponse> activate(
            UUID id);

    ResponseEntity<CountryResponse> deactivate(
            UUID id);

    ResponseEntity<Void> delete(
            UUID id);

}