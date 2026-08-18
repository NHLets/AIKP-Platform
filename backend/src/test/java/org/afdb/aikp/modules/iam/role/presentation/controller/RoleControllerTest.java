package org.afdb.aikp.modules.iam.role.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.afdb.aikp.modules.iam.role.application.response.RoleResponse;
import org.afdb.aikp.modules.iam.role.application.response.RoleSummary;
import org.afdb.aikp.modules.iam.role.application.service.RoleApplicationService;
import org.afdb.aikp.modules.iam.role.domain.enums.RoleStatus;
import org.afdb.aikp.modules.iam.role.domain.exception.RoleAlreadyExistsException;
import org.afdb.aikp.modules.iam.role.domain.exception.RoleNotFoundException;
import org.afdb.aikp.modules.iam.role.presentation.request.CreateRoleRequest;
import org.afdb.aikp.modules.iam.role.presentation.request.UpdateRoleRequest;
import org.afdb.aikp.shared.web.ApiExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RoleController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(ApiExceptionHandler.class)
class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RoleApplicationService applicationService;

    @Test
    void shouldCreateRole() throws Exception {

        UUID id = UUID.randomUUID();
        Instant now = Instant.now();

        CreateRoleRequest request =
                new CreateRoleRequest(
                        "DATA_COLLECTOR",
                        "Data collection role",
                        false);

        RoleResponse response =
                new RoleResponse(
                        id,
                        "DATA_COLLECTOR",
                        "Data collection role",
                        RoleStatus.ACTIVE,
                        false,
                        now,
                        now);

        when(applicationService.create(any()))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/v1/roles")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id")
                        .value(id.toString()))
                .andExpect(jsonPath("$.name")
                        .value("DATA_COLLECTOR"))
                .andExpect(jsonPath("$.description")
                        .value("Data collection role"))
                .andExpect(jsonPath("$.status")
                        .value("ACTIVE"))
                .andExpect(jsonPath("$.system")
                        .value(false));
    }

    @Test
    void shouldGetRole() throws Exception {

        UUID id = UUID.randomUUID();
        Instant now = Instant.now();

        RoleResponse response =
                new RoleResponse(
                        id,
                        "DATA_COLLECTOR",
                        "Data collection role",
                        RoleStatus.ACTIVE,
                        false,
                        now,
                        now);

        when(applicationService.get(any()))
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/v1/roles/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id")
                        .value(id.toString()))
                .andExpect(jsonPath("$.name")
                        .value("DATA_COLLECTOR"))
                .andExpect(jsonPath("$.description")
                        .value("Data collection role"))
                .andExpect(jsonPath("$.status")
                        .value("ACTIVE"))
                .andExpect(jsonPath("$.system")
                        .value(false));
    }

    @Test
    void shouldGetAllRoles() throws Exception {

        UUID id = UUID.randomUUID();

        RoleSummary summary =
                new RoleSummary(
                        id,
                        "DATA_COLLECTOR",
                        RoleStatus.ACTIVE,
                        false);

        when(applicationService.getAll(any()))
                .thenReturn(List.of(summary));

        mockMvc.perform(
                        get("/api/v1/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id")
                        .value(id.toString()))
                .andExpect(jsonPath("$[0].name")
                        .value("DATA_COLLECTOR"))
                .andExpect(jsonPath("$[0].status")
                        .value("ACTIVE"))
                .andExpect(jsonPath("$[0].system")
                        .value(false));
    }

    @Test
    void shouldGetActiveRoles() throws Exception {

        UUID id = UUID.randomUUID();

        RoleSummary summary =
                new RoleSummary(
                        id,
                        "DATA_COLLECTOR",
                        RoleStatus.ACTIVE,
                        false);

        when(applicationService.getActive(any()))
                .thenReturn(List.of(summary));

        mockMvc.perform(
                        get("/api/v1/roles/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id")
                        .value(id.toString()))
                .andExpect(jsonPath("$[0].name")
                        .value("DATA_COLLECTOR"))
                .andExpect(jsonPath("$[0].status")
                        .value("ACTIVE"))
                .andExpect(jsonPath("$[0].system")
                        .value(false));
    }

    @Test
    void shouldUpdateRole() throws Exception {

        UUID id = UUID.randomUUID();
        Instant now = Instant.now();

        UpdateRoleRequest request =
                new UpdateRoleRequest(
                        "SENIOR_DATA_COLLECTOR",
                        "Senior data collection role");

        RoleResponse response =
                new RoleResponse(
                        id,
                        "SENIOR_DATA_COLLECTOR",
                        "Senior data collection role",
                        RoleStatus.ACTIVE,
                        false,
                        now,
                        now);

        when(applicationService.update(any()))
                .thenReturn(response);

        mockMvc.perform(
                        put("/api/v1/roles/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id")
                        .value(id.toString()))
                .andExpect(jsonPath("$.name")
                        .value("SENIOR_DATA_COLLECTOR"))
                .andExpect(jsonPath("$.description")
                        .value("Senior data collection role"));
    }

    @Test
    void shouldActivateRole() throws Exception {

        UUID id = UUID.randomUUID();
        Instant now = Instant.now();

        RoleResponse response =
                new RoleResponse(
                        id,
                        "DATA_COLLECTOR",
                        "Data collection role",
                        RoleStatus.ACTIVE,
                        false,
                        now,
                        now);

        when(applicationService.activate(any()))
                .thenReturn(response);

        mockMvc.perform(
                        patch(
                                "/api/v1/roles/{id}/activate",
                                id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id")
                        .value(id.toString()))
                .andExpect(jsonPath("$.status")
                        .value("ACTIVE"));
    }

    @Test
    void shouldDeactivateRole() throws Exception {

        UUID id = UUID.randomUUID();
        Instant now = Instant.now();

        RoleResponse response =
                new RoleResponse(
                        id,
                        "DATA_COLLECTOR",
                        "Data collection role",
                        RoleStatus.INACTIVE,
                        false,
                        now,
                        now);

        when(applicationService.deactivate(any()))
                .thenReturn(response);

        mockMvc.perform(
                        patch(
                                "/api/v1/roles/{id}/deactivate",
                                id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id")
                        .value(id.toString()))
                .andExpect(jsonPath("$.status")
                        .value("INACTIVE"));
    }

    @Test
    void shouldDeleteRole() throws Exception {

        UUID id = UUID.randomUUID();

        doNothing()
                .when(applicationService)
                .delete(any());

        mockMvc.perform(
                        delete("/api/v1/roles/{id}", id))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }

    @Test
    void shouldRejectInvalidCreateRequest() throws Exception {

        CreateRoleRequest request =
                new CreateRoleRequest(
                        "",
                        "",
                        null);

        mockMvc.perform(
                        post("/api/v1/roles")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title")
                        .value("Validation failed"))
                .andExpect(jsonPath("$.errors.name")
                        .exists())
                .andExpect(jsonPath("$.errors.description")
                        .exists())
                .andExpect(jsonPath("$.errors.system")
                        .exists());
    }

    @Test
    void shouldRejectInvalidUpdateRequest() throws Exception {

        UUID id = UUID.randomUUID();

        UpdateRoleRequest request =
                new UpdateRoleRequest(
                        "",
                        "");

        mockMvc.perform(
                        put("/api/v1/roles/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title")
                        .value("Validation failed"))
                .andExpect(jsonPath("$.errors.name")
                        .exists())
                .andExpect(jsonPath("$.errors.description")
                        .exists());
    }

    @Test
    void shouldReturnNotFoundWhenRoleDoesNotExist()
            throws Exception {

        UUID id = UUID.randomUUID();

        when(applicationService.get(any()))
                .thenThrow(
                        RoleNotFoundException.withId(id));

        mockMvc.perform(
                        get("/api/v1/roles/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status")
                        .value(404))
                .andExpect(jsonPath("$.instance")
                        .value("/api/v1/roles/" + id));
    }

    @Test
    void shouldReturnConflictWhenRoleAlreadyExists()
            throws Exception {

        CreateRoleRequest request =
                new CreateRoleRequest(
                        "DATA_COLLECTOR",
                        "Data collection role",
                        false);

        when(applicationService.create(any()))
                .thenThrow(
                        RoleAlreadyExistsException.withName(
                                "DATA_COLLECTOR"));

        mockMvc.perform(
                        post("/api/v1/roles")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status")
                        .value(409));
    }
}
