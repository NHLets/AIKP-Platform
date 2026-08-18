package org.afdb.aikp.modules.iam.role.presentation.contract;

import org.afdb.aikp.modules.iam.role.application.response.RoleResponse;
import org.afdb.aikp.modules.iam.role.application.response.RoleSummary;
import org.afdb.aikp.modules.iam.role.presentation.request.CreateRoleRequest;
import org.afdb.aikp.modules.iam.role.presentation.request.UpdateRoleRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

/**
 * Contract for IAM Role REST operations.
 *
 * <p>
 * This interface defines the REST contract independently of the
 * Spring MVC/OpenAPI annotations, which are declared on the
 * implementing controller.
 * </p>
 */
public interface RoleApi {

    ResponseEntity<RoleResponse> create(
            CreateRoleRequest request);

    ResponseEntity<RoleResponse> get(
            UUID id);

    ResponseEntity<List<RoleSummary>> getAll();

    ResponseEntity<List<RoleSummary>> getActive();

    ResponseEntity<RoleResponse> update(
            UUID id,
            UpdateRoleRequest request);

    ResponseEntity<RoleResponse> activate(
            UUID id);

    ResponseEntity<RoleResponse> deactivate(
            UUID id);

    ResponseEntity<Void> delete(
            UUID id);
}
