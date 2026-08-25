package org.afdb.aikp.modules.person.presentation.contract;

import org.afdb.aikp.modules.person.application.response.PersonResponse;
import org.afdb.aikp.modules.person.application.response.PersonSummary;
import org.afdb.aikp.modules.person.presentation.request.CreatePersonRequest;
import org.afdb.aikp.modules.person.presentation.request.UpdatePersonRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

/**
 * Contract for Person REST operations.
 *
 * <p>
 * This interface defines the REST contract independently of the
 * Spring MVC/OpenAPI annotations, which are declared on the
 * implementing controller.
 * </p>
 */
public interface PersonApi {

    ResponseEntity<PersonResponse> create(
            CreatePersonRequest request);

    ResponseEntity<PersonResponse> get(
            UUID id);

    ResponseEntity<List<PersonSummary>> getAll();

    ResponseEntity<List<PersonSummary>> getActive();

    ResponseEntity<List<PersonSummary>> getByOrganization(
            UUID organizationId);

    ResponseEntity<PersonResponse> update(
            UUID id,
            UpdatePersonRequest request);

    ResponseEntity<PersonResponse> activate(
            UUID id);

    ResponseEntity<PersonResponse> deactivate(
            UUID id);

    ResponseEntity<Void> delete(
            UUID id);
}
