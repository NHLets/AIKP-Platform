package org.afdb.aikp.modules.questionnaire.presentation.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.afdb.aikp.modules.questionnaire.application.dto.QuestionnaireVariableResponse;
import org.afdb.aikp.modules.questionnaire.application.service.QuestionnaireVariableApplicationService;
import org.afdb.aikp.modules.questionnaire.presentation.contract.QuestionnaireVariableApi;
import org.afdb.aikp.modules.questionnaire.presentation.mapper.QuestionnaireVariableRestMapper;
import org.afdb.aikp.modules.questionnaire.presentation.request.CreateQuestionnaireVariableRestRequest;
import org.afdb.aikp.modules.questionnaire.presentation.request.UpdateQuestionnaireVariableRestRequest;

@RestController
@RequestMapping("/api/v1/questionnaire-variables")
@Tag(
        name = "Questionnaire Variable",
        description = "Operations for managing questionnaire variables."
)
public class QuestionnaireVariableController
        implements QuestionnaireVariableApi {

    private final QuestionnaireVariableApplicationService
            applicationService;

    private final QuestionnaireVariableRestMapper restMapper;

    public QuestionnaireVariableController(
            QuestionnaireVariableApplicationService applicationService,
            QuestionnaireVariableRestMapper restMapper) {

        this.applicationService = applicationService;
        this.restMapper = restMapper;
    }

    @Override
    @PostMapping
    @Operation(summary = "Create a questionnaire variable")
    public ResponseEntity<QuestionnaireVariableResponse> create(

            @Valid
            @RequestBody
            CreateQuestionnaireVariableRestRequest request) {

        QuestionnaireVariableResponse response =
                applicationService.createVariable(
                        restMapper.toApplicationRequest(request));

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(response);
    }

    @Override
    @GetMapping("/{id}")
    @Operation(summary = "Retrieve a questionnaire variable")
    public ResponseEntity<QuestionnaireVariableResponse> get(

            @PathVariable("id")
            UUID id) {

        return ResponseEntity.ok(
                applicationService.getVariableById(id));
    }

    @Override
    @GetMapping("/questionnaire/{questionnaireId}")
    @Operation(summary = "Retrieve variables of a questionnaire")
    public ResponseEntity<List<QuestionnaireVariableResponse>>
            getByQuestionnaire(

                    @PathVariable("questionnaireId")
                    UUID questionnaireId) {

        return ResponseEntity.ok(
                applicationService
                        .getVariablesByQuestionnaire(
                                questionnaireId));
    }

    @Override
    @GetMapping("/group/{groupId}")
    @Operation(summary = "Retrieve variables of a questionnaire group")
    public ResponseEntity<List<QuestionnaireVariableResponse>>
            getByGroup(

                    @PathVariable("groupId")
                    UUID groupId) {

        return ResponseEntity.ok(
                applicationService
                        .getVariablesByGroup(groupId));
    }

    @Override
    @PutMapping("/{id}")
    @Operation(summary = "Update a questionnaire variable")
    public ResponseEntity<QuestionnaireVariableResponse> update(

            @PathVariable("id")
            UUID id,

            @Valid
            @RequestBody
            UpdateQuestionnaireVariableRestRequest request) {

        return ResponseEntity.ok(
                applicationService.updateVariable(
                        id,
                        restMapper.toApplicationRequest(request)));
    }

    @Override
    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate a questionnaire variable")
    public ResponseEntity<QuestionnaireVariableResponse> activate(

            @PathVariable("id")
            UUID id) {

        return ResponseEntity.ok(
                applicationService.activateVariable(id));
    }

    @Override
    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate a questionnaire variable")
    public ResponseEntity<QuestionnaireVariableResponse> deactivate(

            @PathVariable("id")
            UUID id) {

        return ResponseEntity.ok(
                applicationService.deactivateVariable(id));
    }

    @Override
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a questionnaire variable")
    public ResponseEntity<Void> delete(

            @PathVariable("id")
            UUID id) {

        applicationService.deleteVariable(id);

        return ResponseEntity.noContent().build();
    }
}
