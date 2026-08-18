package org.afdb.aikp.modules.organization.presentation.contract;

import org.afdb.aikp.modules.organization.application.response.OrganizationResponse;
import org.afdb.aikp.modules.organization.application.response.OrganizationSummary;
import org.afdb.aikp.modules.organization.presentation.request.CreateOrganizationRequest;
import org.afdb.aikp.modules.organization.presentation.request.UpdateOrganizationRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

/**
 * Contract for Organization REST operations.
 *
 * <p>
 * This interface defines the REST contract independently of the
 * Spring MVC/OpenAPI annotations, which are declared on the
 * implementing controller.
 * </p>
 */
public interface OrganizationApi {

    ResponseEntity<OrganizationResponse> create(
            CreateOrganizationRequest request);

    ResponseEntity<OrganizationResponse> get(
            UUID id);

    ResponseEntity<List<OrganizationSummary>> getAll();

    ResponseEntity<List<OrganizationSummary>> getActive();

    ResponseEntity<List<OrganizationSummary>> getByCountry(
            UUID countryId);

    ResponseEntity<List<OrganizationSummary>> getByType(
            String type);

    ResponseEntity<OrganizationResponse> update(
            UUID id,
            UpdateOrganizationRequest request);

    ResponseEntity<OrganizationResponse> activate(
            UUID id);

    ResponseEntity<OrganizationResponse> deactivate(
            UUID id);

    ResponseEntity<Void> delete(
            UUID id);
}
