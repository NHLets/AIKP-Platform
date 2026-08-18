package org.afdb.aikp.modules.questionnaire.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.afdb.aikp.modules.questionnaire.application.command.ActivateQuestionnaireCommand;
import org.afdb.aikp.modules.questionnaire.application.command.ArchiveQuestionnaireCommand;
import org.afdb.aikp.modules.questionnaire.application.command.DeactivateQuestionnaireCommand;
import org.afdb.aikp.modules.questionnaire.application.command.DeleteQuestionnaireCommand;
import org.afdb.aikp.modules.questionnaire.application.command.PublishQuestionnaireCommand;
import org.afdb.aikp.modules.questionnaire.application.query.GetActiveQuestionnairesQuery;
import org.afdb.aikp.modules.questionnaire.application.query.GetPublishedQuestionnairesQuery;
import org.afdb.aikp.modules.questionnaire.application.query.GetQuestionnaireQuery;
import org.afdb.aikp.modules.questionnaire.application.query.GetQuestionnairesQuery;
import org.afdb.aikp.modules.questionnaire.application.response.QuestionnaireResponse;
import org.afdb.aikp.modules.questionnaire.application.response.QuestionnaireSummary;
import org.afdb.aikp.modules.questionnaire.application.service.QuestionnaireApplicationService;
import org.afdb.aikp.modules.questionnaire.presentation.contract.QuestionnaireApi;
import org.afdb.aikp.modules.questionnaire.presentation.mapper.QuestionnaireRestMapper;
import org.afdb.aikp.modules.questionnaire.presentation.request.CreateQuestionnaireRequest;
import org.afdb.aikp.modules.questionnaire.presentation.request.UpdateQuestionnaireRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.afdb.aikp.modules.questionnaire.application.command.SubmitQuestionnaireForReviewCommand;
import org.afdb.aikp.modules.questionnaire.application.command.ApproveQuestionnaireCommand;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/questionnaires")
@Tag(
        name = "Questionnaire",
        description = "Operations for managing AIKP questionnaires."
)
public class QuestionnaireController implements QuestionnaireApi {

    private final QuestionnaireApplicationService applicationService;

    public QuestionnaireController(
            QuestionnaireApplicationService applicationService) {

        this.applicationService = applicationService;
    }

    @Override
    @PostMapping
    @Operation(summary = "Create a new questionnaire")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Questionnaire created"),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "409", description = "Questionnaire already exists", content = @Content)
    })
    public ResponseEntity<QuestionnaireResponse> create(

            @Valid
            @RequestBody
            CreateQuestionnaireRequest request) {

        QuestionnaireResponse response =
                applicationService.create(
                        QuestionnaireRestMapper.toCommand(request));

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
    @Operation(summary = "Retrieve a questionnaire")
    public ResponseEntity<QuestionnaireResponse> get(

            @Parameter(
                    name = "id",
                    required = true)
            @PathVariable("id") UUID id) {

        return ResponseEntity.ok(
        applicationService.getById(
                new GetQuestionnaireQuery(id)));
    }

    @Override
    @GetMapping
    @Operation(summary = "Retrieve all questionnaires")
    public ResponseEntity<List<QuestionnaireSummary>> getAll() {

        return ResponseEntity.ok(
                applicationService.getAll(
                        new GetQuestionnairesQuery()));
    }

    @Override
    @GetMapping("/active")
    @Operation(summary = "Retrieve active questionnaires")
    public ResponseEntity<List<QuestionnaireSummary>> getActive() {

        return ResponseEntity.ok(
                applicationService.getActive(
                        new GetActiveQuestionnairesQuery()));
    }

    @Override
    @GetMapping("/published")
    @Operation(summary = "Retrieve published questionnaires")
    public ResponseEntity<List<QuestionnaireSummary>> getPublished() {

        return ResponseEntity.ok(
                applicationService.getPublished(
                        new GetPublishedQuestionnairesQuery()));
    }

    @Override
    @PutMapping("/{id}")
    @Operation(summary = "Update a questionnaire")
    public ResponseEntity<QuestionnaireResponse> update(

            @PathVariable("id") UUID id,

            @Valid
            @RequestBody
            UpdateQuestionnaireRequest request) {

        return ResponseEntity.ok(
                applicationService.update(
                        QuestionnaireRestMapper.toCommand(id, request)));
    }

    @Override
        @PatchMapping("/{id}/submit-for-review")
        @Operation(summary = "Submit a questionnaire for review")
        public ResponseEntity<QuestionnaireResponse> submitForReview(
        @PathVariable("id") UUID id) {

        return ResponseEntity.ok(
            applicationService.submitForReview(
                    new SubmitQuestionnaireForReviewCommand(id)));
        }

        @PatchMapping("/{id}/approve")
        @Operation(summary = "Approve a questionnaire")
        public ResponseEntity<QuestionnaireResponse> approve(
        @PathVariable("id") UUID id) {

        return ResponseEntity.ok(
            applicationService.approve(
                    new ApproveQuestionnaireCommand(id)));
}

    @Override
    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate a questionnaire")
    public ResponseEntity<QuestionnaireResponse> activate(

            @PathVariable("id") UUID id) {

        return ResponseEntity.ok(
                applicationService.activate(
                        new ActivateQuestionnaireCommand(id)));
    }

    @Override
    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate a questionnaire")
    public ResponseEntity<QuestionnaireResponse> deactivate(

            @PathVariable("id") UUID id) {

        return ResponseEntity.ok(
                applicationService.deactivate(
                        new DeactivateQuestionnaireCommand(id)));
    }

    @Override
    @PatchMapping("/{id}/publish")
    @Operation(summary = "Publish a questionnaire")
    public ResponseEntity<QuestionnaireResponse> publish(

            @PathVariable("id") UUID id) {

        return ResponseEntity.ok(
                applicationService.publish(
                        new PublishQuestionnaireCommand(id)));
    }

    @Override
    @PatchMapping("/{id}/archive")
    @Operation(summary = "Archive a questionnaire")
    public ResponseEntity<QuestionnaireResponse> archive(

            @PathVariable("id") UUID id) {

        return ResponseEntity.ok(
                applicationService.archive(
                        new ArchiveQuestionnaireCommand(id)));
    }

    @Override
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a questionnaire")
    public ResponseEntity<Void> delete(

            @PathVariable("id") UUID id) {

        applicationService.delete(
                new DeleteQuestionnaireCommand(id));

        return ResponseEntity.noContent().build();
    }

}
