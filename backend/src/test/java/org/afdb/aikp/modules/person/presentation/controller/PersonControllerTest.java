package org.afdb.aikp.modules.person.presentation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.afdb.aikp.modules.person.application.response.PersonResponse;
import org.afdb.aikp.modules.person.application.response.PersonSummary;
import org.afdb.aikp.modules.person.application.service.PersonApplicationService;
import org.afdb.aikp.shared.web.ApiExceptionHandler;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PersonController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(ApiExceptionHandler.class)
class PersonControllerTest {

    private static final String BASE_URL =
            "/api/v1/persons";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PersonApplicationService applicationService;

    @Test
    void shouldCreatePerson() throws Exception {

        UUID personId =
                UUID.randomUUID();

        UUID organizationId =
                UUID.randomUUID();

        PersonResponse response =
                new PersonResponse(
                        personId,
                        "John Doe",
                        organizationId,
                        true);

        when(applicationService.createPerson(any()))
                .thenReturn(response);

        String requestBody =
                """
                {
                  "fullName": "John Doe",
                  "organizationId": "%s"
                }
                """.formatted(organizationId);

        mockMvc.perform(
                        post(BASE_URL)
                                .contentType(
                                        MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(
                        status().isCreated())
                .andExpect(
                        header().string(
                                "Location",
                                "http://localhost"
                                        + BASE_URL
                                        + "/"
                                        + personId))
                .andExpect(
                        jsonPath("$.id")
                                .value(
                                        personId.toString()))
                .andExpect(
                        jsonPath("$.fullName")
                                .value("John Doe"))
                .andExpect(
                        jsonPath("$.organizationId")
                                .value(
                                        organizationId.toString()))
                .andExpect(
                        jsonPath("$.active")
                                .value(true));

        verify(applicationService)
                .createPerson(any());
    }

    @Test
    void shouldGetPersonById() throws Exception {

        UUID personId =
                UUID.randomUUID();

        UUID organizationId =
                UUID.randomUUID();

        PersonResponse response =
                new PersonResponse(
                        personId,
                        "Jane Doe",
                        organizationId,
                        true);

        when(applicationService.getPerson(any()))
                .thenReturn(response);

        mockMvc.perform(
                        get(
                                BASE_URL
                                        + "/"
                                        + personId))
                .andExpect(
                        status().isOk())
                .andExpect(
                        jsonPath("$.id")
                                .value(
                                        personId.toString()))
                .andExpect(
                        jsonPath("$.fullName")
                                .value("Jane Doe"))
                .andExpect(
                        jsonPath("$.organizationId")
                                .value(
                                        organizationId.toString()))
                .andExpect(
                        jsonPath("$.active")
                                .value(true));

        verify(applicationService)
                .getPerson(any());
    }

    @Test
    void shouldGetAllPersons() throws Exception {

        UUID organizationId =
                UUID.randomUUID();

        PersonSummary firstPerson =
                new PersonSummary(
                        UUID.randomUUID(),
                        "Alice Martin",
                        organizationId,
                        true);

        PersonSummary secondPerson =
                new PersonSummary(
                        UUID.randomUUID(),
                        "Bob Smith",
                        organizationId,
                        false);

        when(applicationService.getPersons(any()))
                .thenReturn(
                        List.of(
                                firstPerson,
                                secondPerson));

        mockMvc.perform(
                        get(BASE_URL))
                .andExpect(
                        status().isOk())
                .andExpect(
                        jsonPath("$.length()")
                                .value(2))
                .andExpect(
                        jsonPath("$[0].fullName")
                                .value(
                                        "Alice Martin"))
                .andExpect(
                        jsonPath("$[1].fullName")
                                .value(
                                        "Bob Smith"));

        verify(applicationService)
                .getPersons(any());
    }

    @Test
    void shouldGetActivePersons() throws Exception {

        UUID organizationId =
                UUID.randomUUID();

        PersonSummary person =
                new PersonSummary(
                        UUID.randomUUID(),
                        "Active Person",
                        organizationId,
                        true);

        when(applicationService.getActivePersons(any()))
                .thenReturn(
                        List.of(person));

        mockMvc.perform(
                        get(
                                BASE_URL
                                        + "/active"))
                .andExpect(
                        status().isOk())
                .andExpect(
                        jsonPath("$.length()")
                                .value(1))
                .andExpect(
                        jsonPath("$[0].fullName")
                                .value(
                                        "Active Person"))
                .andExpect(
                        jsonPath("$[0].active")
                                .value(true));

        verify(applicationService)
                .getActivePersons(any());
    }

    @Test
    void shouldGetPersonsByOrganization() throws Exception {

        UUID organizationId =
                UUID.randomUUID();

        PersonSummary person =
                new PersonSummary(
                        UUID.randomUUID(),
                        "Organization Person",
                        organizationId,
                        true);

        when(
                applicationService
                        .getPersonsByOrganization(any()))
                .thenReturn(
                        List.of(person));

        mockMvc.perform(
                        get(
                                BASE_URL
                                        + "/by-organization/"
                                        + organizationId))
                .andExpect(
                        status().isOk())
                .andExpect(
                        jsonPath("$.length()")
                                .value(1))
                .andExpect(
                        jsonPath("$[0].organizationId")
                                .value(
                                        organizationId.toString()));

        verify(applicationService)
                .getPersonsByOrganization(any());
    }

    @Test
    void shouldUpdatePerson() throws Exception {

        UUID personId =
                UUID.randomUUID();

        UUID organizationId =
                UUID.randomUUID();

        PersonResponse response =
                new PersonResponse(
                        personId,
                        "Updated Person",
                        organizationId,
                        true);

        when(applicationService.updatePerson(any()))
                .thenReturn(response);

        String requestBody =
                """
                {
                  "fullName": "Updated Person",
                  "organizationId": "%s"
                }
                """.formatted(organizationId);

        mockMvc.perform(
                        put(
                                BASE_URL
                                        + "/"
                                        + personId)
                                .contentType(
                                        MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(
                        status().isOk())
                .andExpect(
                        jsonPath("$.id")
                                .value(
                                        personId.toString()))
                .andExpect(
                        jsonPath("$.fullName")
                                .value(
                                        "Updated Person"))
                .andExpect(
                        jsonPath("$.organizationId")
                                .value(
                                        organizationId.toString()));

        verify(applicationService)
                .updatePerson(any());
    }

    @Test
    void shouldActivatePerson() throws Exception {

        UUID personId =
                UUID.randomUUID();

        UUID organizationId =
                UUID.randomUUID();

        PersonResponse response =
                new PersonResponse(
                        personId,
                        "John Doe",
                        organizationId,
                        true);

        when(applicationService.activatePerson(any()))
                .thenReturn(response);

        mockMvc.perform(
                        patch(
                                BASE_URL
                                        + "/"
                                        + personId
                                        + "/activate"))
                .andExpect(
                        status().isOk())
                .andExpect(
                        jsonPath("$.active")
                                .value(true));

        verify(applicationService)
                .activatePerson(any());
    }

    @Test
    void shouldDeactivatePerson() throws Exception {

        UUID personId =
                UUID.randomUUID();

        UUID organizationId =
                UUID.randomUUID();

        PersonResponse response =
                new PersonResponse(
                        personId,
                        "John Doe",
                        organizationId,
                        false);

        when(applicationService.deactivatePerson(any()))
                .thenReturn(response);

        mockMvc.perform(
                        patch(
                                BASE_URL
                                        + "/"
                                        + personId
                                        + "/deactivate"))
                .andExpect(
                        status().isOk())
                .andExpect(
                        jsonPath("$.active")
                                .value(false));

        verify(applicationService)
                .deactivatePerson(any());
    }

    @Test
    void shouldDeletePerson() throws Exception {

        UUID personId =
                UUID.randomUUID();

        doNothing()
                .when(applicationService)
                .deletePerson(any());

        mockMvc.perform(
                        delete(
                                BASE_URL
                                        + "/"
                                        + personId))
                .andExpect(
                        status().isNoContent());

        verify(applicationService)
                .deletePerson(any());
    }

    @Test
    void shouldRejectCreateRequestWhenFullNameIsBlank()
            throws Exception {

        UUID organizationId =
                UUID.randomUUID();

        String requestBody =
                """
                {
                  "fullName": "",
                  "organizationId": "%s"
                }
                """.formatted(organizationId);

        mockMvc.perform(
                        post(BASE_URL)
                                .contentType(
                                        MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(
                        status().isBadRequest());
    }

    @Test
    void shouldRejectCreateRequestWhenOrganizationIdIsMissing()
            throws Exception {

        String requestBody =
                """
                {
                  "fullName": "John Doe"
                }
                """;

        mockMvc.perform(
                        post(BASE_URL)
                                .contentType(
                                        MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(
                        status().isBadRequest());
    }

    @Test
    void shouldRejectUpdateRequestWhenFullNameIsBlank()
            throws Exception {

        UUID personId =
                UUID.randomUUID();

        UUID organizationId =
                UUID.randomUUID();

        String requestBody =
                """
                {
                  "fullName": "",
                  "organizationId": "%s"
                }
                """.formatted(organizationId);

        mockMvc.perform(
                        put(
                                BASE_URL
                                        + "/"
                                        + personId)
                                .contentType(
                                        MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(
                        status().isBadRequest());
    }
}
