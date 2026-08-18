package org.afdb.aikp.modules.country.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.afdb.aikp.modules.country.application.response.CountryResponse;
import org.afdb.aikp.modules.country.application.response.CountrySummary;
import org.afdb.aikp.modules.country.application.service.CountryApplicationService;
import org.afdb.aikp.modules.country.domain.exception.CountryNotFoundException;
import org.afdb.aikp.modules.country.domain.exception.DuplicateCountryException;
import org.afdb.aikp.modules.country.presentation.request.CreateCountryRequest;
import org.afdb.aikp.modules.country.presentation.request.UpdateCountryRequest;
import org.afdb.aikp.shared.web.ApiExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
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

@WebMvcTest(CountryController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(ApiExceptionHandler.class)
class CountryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CountryApplicationService applicationService;

    @Test
    void shouldCreateCountry() throws Exception {

        UUID id = UUID.randomUUID();

        CreateCountryRequest request =
                new CreateCountryRequest(
                        "MG",
                        "MDG",
                        "450",
                        "Madagascar",
                        "Republic of Madagascar");

        CountryResponse response =
                new CountryResponse(
                        id,
                        "MG",
                        "MDG",
                        "450",
                        "Madagascar",
                        "Republic of Madagascar",
                        true);

        when(applicationService.create(any()))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/v1/countries")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.iso2Code").value("MG"))
                .andExpect(jsonPath("$.iso3Code").value("MDG"))
                .andExpect(jsonPath("$.numericCode").value("450"))
                .andExpect(jsonPath("$.name").value("Madagascar"))
                .andExpect(jsonPath("$.officialName")
                        .value("Republic of Madagascar"))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void shouldGetCountry() throws Exception {

        UUID id = UUID.randomUUID();

        CountryResponse response =
                new CountryResponse(
                        id,
                        "MG",
                        "MDG",
                        "450",
                        "Madagascar",
                        "Republic of Madagascar",
                        true);

        when(applicationService.get(any()))
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/v1/countries/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.iso2Code").value("MG"))
                .andExpect(jsonPath("$.iso3Code").value("MDG"));
    }

    @Test
    void shouldGetAllCountries() throws Exception {

        UUID id = UUID.randomUUID();

        CountrySummary summary =
                new CountrySummary(
                        id,
                        "MG",
                        "MDG",
                        "450",
                        "Madagascar",
                        "Republic of Madagascar",
                        true);

        when(applicationService.getAll(any()))
                .thenReturn(List.of(summary));

        mockMvc.perform(
                        get("/api/v1/countries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id.toString()))
                .andExpect(jsonPath("$[0].iso2Code").value("MG"))
                .andExpect(jsonPath("$[0].iso3Code").value("MDG"));
    }

    @Test
    void shouldGetActiveCountries() throws Exception {

        UUID id = UUID.randomUUID();

        CountrySummary summary =
                new CountrySummary(
                        id,
                        "MG",
                        "MDG",
                        "450",
                        "Madagascar",
                        "Republic of Madagascar",
                        true);

        when(applicationService.getActive(any()))
                .thenReturn(List.of(summary));

        mockMvc.perform(
                        get("/api/v1/countries/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id.toString()))
                .andExpect(jsonPath("$[0].active").value(true));
    }

    @Test
    void shouldUpdateCountry() throws Exception {

        UUID id = UUID.randomUUID();

        UpdateCountryRequest request =
                new UpdateCountryRequest(
                        "MG",
                        "MDG",
                        "450",
                        "Madagascar",
                        "Republic of Madagascar");

        CountryResponse response =
                new CountryResponse(
                        id,
                        "MG",
                        "MDG",
                        "450",
                        "Madagascar",
                        "Republic of Madagascar",
                        true);

        when(applicationService.update(any()))
                .thenReturn(response);

        mockMvc.perform(
                        put("/api/v1/countries/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.iso2Code").value("MG"));
    }

    @Test
    void shouldActivateCountry() throws Exception {

        UUID id = UUID.randomUUID();

        CountryResponse response =
                new CountryResponse(
                        id,
                        "MG",
                        "MDG",
                        "450",
                        "Madagascar",
                        "Republic of Madagascar",
                        true);

        when(applicationService.activate(any()))
                .thenReturn(response);

        mockMvc.perform(
                        patch("/api/v1/countries/{id}/activate", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void shouldDeactivateCountry() throws Exception {

        UUID id = UUID.randomUUID();

        CountryResponse response =
                new CountryResponse(
                        id,
                        "MG",
                        "MDG",
                        "450",
                        "Madagascar",
                        "Republic of Madagascar",
                        false);

        when(applicationService.deactivate(any()))
                .thenReturn(response);

        mockMvc.perform(
                        patch("/api/v1/countries/{id}/deactivate", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.active").value(false));
    }

    @Test
    void shouldDeleteCountry() throws Exception {

        UUID id = UUID.randomUUID();

        doNothing()
                .when(applicationService)
                .delete(any());

        mockMvc.perform(
                        delete("/api/v1/countries/{id}", id))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }

    @Test
    void shouldRejectInvalidCreateRequest() throws Exception {

        CreateCountryRequest request =
                new CreateCountryRequest(
                        "m",
                        "MD",
                        "45",
                        "",
                        "");

        mockMvc.perform(
                        post("/api/v1/countries")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title")
                        .value("Validation failed"))
                .andExpect(jsonPath("$.errors.iso2Code").exists())
                .andExpect(jsonPath("$.errors.iso3Code").exists())
                .andExpect(jsonPath("$.errors.numericCode").exists())
                .andExpect(jsonPath("$.errors.name").exists())
                .andExpect(jsonPath("$.errors.officialName").exists());
    }

    @Test
    void shouldReturnNotFoundWhenCountryDoesNotExist() throws Exception {

        UUID id = UUID.randomUUID();

        when(applicationService.get(any()))
                .thenThrow(new CountryNotFoundException(
        org.afdb.aikp.modules.country.domain.valueobject.CountryId.of(id)));

        mockMvc.perform(
                        get("/api/v1/countries/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.instance")
                        .value("/api/v1/countries/" + id));
    }

    @Test
    void shouldReturnConflictWhenCountryAlreadyExists() throws Exception {

        CreateCountryRequest request =
                new CreateCountryRequest(
                        "MG",
                        "MDG",
                        "450",
                        "Madagascar",
                        "Republic of Madagascar");

        when(applicationService.create(any()))
                .thenThrow(
                        new DuplicateCountryException(
                                "A country already exists with ISO2 code MG"));

        mockMvc.perform(
                        post("/api/v1/countries")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.errorCode")
                        .value("CONFLICT"));
    }
}
