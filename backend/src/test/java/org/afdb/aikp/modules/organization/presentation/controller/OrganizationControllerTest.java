package org.afdb.aikp.modules.organization.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.afdb.aikp.modules.organization.application.response.OrganizationResponse;
import org.afdb.aikp.modules.organization.application.response.OrganizationSummary;
import org.afdb.aikp.modules.organization.application.service.OrganizationApplicationService;
import org.afdb.aikp.modules.organization.domain.exception.OrganizationCodeAlreadyExistsException;
import org.afdb.aikp.modules.organization.domain.exception.OrganizationNotFoundException;
import org.afdb.aikp.modules.organization.domain.valueobject.OrganizationId;
import org.afdb.aikp.modules.organization.presentation.request.CreateOrganizationRequest;
import org.afdb.aikp.modules.organization.presentation.request.UpdateOrganizationRequest;
import org.afdb.aikp.shared.web.ApiExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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

@WebMvcTest(OrganizationController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(ApiExceptionHandler.class)
class OrganizationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrganizationApplicationService applicationService;

    @Test
    void shouldCreateOrganization() throws Exception {

        UUID id = UUID.randomUUID();
        UUID countryId = UUID.randomUUID();

        CreateOrganizationRequest request =
                new CreateOrganizationRequest(
                        "AFDB",
                        "African Development Bank",
                        "MINISTRY",
                        countryId);

        OrganizationResponse response =
                new OrganizationResponse(
                        id,
                        "AFDB",
                        "African Development Bank",
                        "MINISTRY",
                        countryId,
                        true);

        when(applicationService.create(any()))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/v1/organizations")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.code").value("AFDB"))
                .andExpect(jsonPath("$.name")
                        .value("African Development Bank"))
                .andExpect(jsonPath("$.type")
                        .value("MINISTRY"))
                .andExpect(jsonPath("$.countryId")
                        .value(countryId.toString()))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void shouldGetOrganization() throws Exception {

        UUID id = UUID.randomUUID();
        UUID countryId = UUID.randomUUID();

        OrganizationResponse response =
                new OrganizationResponse(
                        id,
                        "AFDB",
                        "African Development Bank",
                        "MINISTRY",
                        countryId,
                        true);

        when(applicationService.get(any()))
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/v1/organizations/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.code").value("AFDB"))
                .andExpect(jsonPath("$.name")
                        .value("African Development Bank"))
                .andExpect(jsonPath("$.type")
                        .value("MINISTRY"))
                .andExpect(jsonPath("$.countryId")
                        .value(countryId.toString()));
    }

    @Test
    void shouldGetAllOrganizations() throws Exception {

        UUID id = UUID.randomUUID();
        UUID countryId = UUID.randomUUID();

        OrganizationSummary summary =
                new OrganizationSummary(
                        id,
                        "AFDB",
                        "African Development Bank",
                        "MINISTRY",
                        countryId,
                        true);

        when(applicationService.getAll(any()))
                .thenReturn(List.of(summary));

        mockMvc.perform(
                        get("/api/v1/organizations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id")
                        .value(id.toString()))
                .andExpect(jsonPath("$[0].code")
                        .value("AFDB"))
                .andExpect(jsonPath("$[0].type")
                        .value("MINISTRY"))
                .andExpect(jsonPath("$[0].countryId")
                        .value(countryId.toString()));
    }

    @Test
    void shouldGetActiveOrganizations() throws Exception {

        UUID id = UUID.randomUUID();
        UUID countryId = UUID.randomUUID();

        OrganizationSummary summary =
                new OrganizationSummary(
                        id,
                        "AFDB",
                        "African Development Bank",
                        "MINISTRY",
                        countryId,
                        true);

        when(applicationService.getActive(any()))
                .thenReturn(List.of(summary));

        mockMvc.perform(
                        get("/api/v1/organizations/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id")
                        .value(id.toString()))
                .andExpect(jsonPath("$[0].code")
                        .value("AFDB"))
                .andExpect(jsonPath("$[0].active")
                        .value(true));
    }

    @Test
    void shouldGetOrganizationsByCountry() throws Exception {

        UUID id = UUID.randomUUID();
        UUID countryId = UUID.randomUUID();

        OrganizationSummary summary =
                new OrganizationSummary(
                        id,
                        "AFDB",
                        "African Development Bank",
                        "MINISTRY",
                        countryId,
                        true);

        when(applicationService.getByCountry(any()))
                .thenReturn(List.of(summary));

        mockMvc.perform(
                        get(
                                "/api/v1/organizations/by-country/{countryId}",
                                countryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id")
                        .value(id.toString()))
                .andExpect(jsonPath("$[0].code")
                        .value("AFDB"))
                .andExpect(jsonPath("$[0].countryId")
                        .value(countryId.toString()));
    }

    @Test
    void shouldGetOrganizationsByType() throws Exception {

        UUID id = UUID.randomUUID();
        UUID countryId = UUID.randomUUID();

        OrganizationSummary summary =
                new OrganizationSummary(
                        id,
                        "AFDB",
                        "African Development Bank",
                        "MINISTRY",
                        countryId,
                        true);

        when(applicationService.getByType(any()))
                .thenReturn(List.of(summary));

        mockMvc.perform(
                        get(
                                "/api/v1/organizations/by-type/{type}",
                                "MINISTRY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id")
                        .value(id.toString()))
                .andExpect(jsonPath("$[0].code")
                        .value("AFDB"))
                .andExpect(jsonPath("$[0].type")
                        .value("MINISTRY"));
    }

    @Test
    void shouldUpdateOrganization() throws Exception {

        UUID id = UUID.randomUUID();
        UUID countryId = UUID.randomUUID();

        UpdateOrganizationRequest request =
                new UpdateOrganizationRequest(
                        "AFDB",
                        "African Development Bank",
                        "MINISTRY",
                        countryId);

        OrganizationResponse response =
                new OrganizationResponse(
                        id,
                        "AFDB",
                        "African Development Bank",
                        "MINISTRY",
                        countryId,
                        true);

        when(applicationService.update(any()))
                .thenReturn(response);

        mockMvc.perform(
                        put("/api/v1/organizations/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id")
                        .value(id.toString()))
                .andExpect(jsonPath("$.code")
                        .value("AFDB"))
                .andExpect(jsonPath("$.countryId")
                        .value(countryId.toString()));
    }

    @Test
    void shouldActivateOrganization() throws Exception {

        UUID id = UUID.randomUUID();
        UUID countryId = UUID.randomUUID();

        OrganizationResponse response =
                new OrganizationResponse(
                        id,
                        "AFDB",
                        "African Development Bank",
                        "MINISTRY",
                        countryId,
                        true);

        when(applicationService.activate(any()))
                .thenReturn(response);

        mockMvc.perform(
                        patch(
                                "/api/v1/organizations/{id}/activate",
                                id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id")
                        .value(id.toString()))
                .andExpect(jsonPath("$.active")
                        .value(true));
    }

    @Test
    void shouldDeactivateOrganization() throws Exception {

        UUID id = UUID.randomUUID();
        UUID countryId = UUID.randomUUID();

        OrganizationResponse response =
                new OrganizationResponse(
                        id,
                        "AFDB",
                        "African Development Bank",
                        "MINISTRY",
                        countryId,
                        false);

        when(applicationService.deactivate(any()))
                .thenReturn(response);

        mockMvc.perform(
                        patch(
                                "/api/v1/organizations/{id}/deactivate",
                                id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id")
                        .value(id.toString()))
                .andExpect(jsonPath("$.active")
                        .value(false));
    }

    @Test
    void shouldDeleteOrganization() throws Exception {

        UUID id = UUID.randomUUID();

        doNothing()
                .when(applicationService)
                .delete(any());

        mockMvc.perform(
                        delete("/api/v1/organizations/{id}", id))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }

    @Test
    void shouldRejectInvalidCreateRequest() throws Exception {

        CreateOrganizationRequest request =
                new CreateOrganizationRequest(
                        "",
                        "",
                        "",
                        null);

        mockMvc.perform(
                        post("/api/v1/organizations")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title")
                        .value("Validation failed"))
                .andExpect(jsonPath("$.errors.code").exists())
                .andExpect(jsonPath("$.errors.name").exists())
                .andExpect(jsonPath("$.errors.type").exists())
                .andExpect(jsonPath("$.errors.countryId").exists());
    }

    @Test
    void shouldReturnNotFoundWhenOrganizationDoesNotExist()
            throws Exception {

        UUID id = UUID.randomUUID();

        when(applicationService.get(any()))
                .thenThrow(
                        new OrganizationNotFoundException(
                                OrganizationId.of(id)));

        mockMvc.perform(
                        get("/api/v1/organizations/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.instance")
                        .value("/api/v1/organizations/" + id));
    }

    @Test
    void shouldReturnConflictWhenOrganizationAlreadyExists()
            throws Exception {

        UUID countryId = UUID.randomUUID();

        CreateOrganizationRequest request =
                new CreateOrganizationRequest(
                        "AFDB",
                        "African Development Bank",
                        "MINISTRY",
                        countryId);

        when(applicationService.create(any()))
                .thenThrow(
                        new OrganizationCodeAlreadyExistsException(
                                "AFDB"));

        mockMvc.perform(
                        post("/api/v1/organizations")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.errorCode")
                        .value("CONFLICT"));
    }
}
