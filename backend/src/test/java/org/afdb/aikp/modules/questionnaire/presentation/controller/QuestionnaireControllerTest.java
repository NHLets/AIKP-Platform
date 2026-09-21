package org.afdb.aikp.modules.questionnaire.presentation.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.afdb.aikp.modules.questionnaire.application.dto.CreateQuestionnaireGroupRequest;
import org.afdb.aikp.modules.questionnaire.application.dto.QuestionnaireGroupResponse;
import org.afdb.aikp.modules.questionnaire.application.dto.UpdateQuestionnaireGroupRequest;
import org.afdb.aikp.modules.questionnaire.application.response.QuestionnaireResponse;
import org.afdb.aikp.modules.questionnaire.application.response.QuestionnaireSummary;
import org.afdb.aikp.modules.questionnaire.application.service.QuestionnaireApplicationService;
import org.afdb.aikp.modules.questionnaire.application.service.QuestionnaireGroupApplicationService;
import org.afdb.aikp.modules.questionnaire.domain.enums.QuestionnaireGroupType;
import org.afdb.aikp.modules.questionnaire.domain.enums.RenderType;
import org.afdb.aikp.modules.questionnaire.presentation.request.CreateQuestionnaireRequest;
import org.afdb.aikp.modules.questionnaire.presentation.request.UpdateQuestionnaireRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@ExtendWith(MockitoExtension.class)
class QuestionnaireControllerTest {

    @Mock
    private QuestionnaireApplicationService applicationService;

    @Mock
    private QuestionnaireGroupApplicationService
            questionnaireGroupApplicationService;

    @InjectMocks
    private QuestionnaireController controller;

    private UUID questionnaireId;

    private QuestionnaireResponse response;

    private QuestionnaireSummary summary;

    @BeforeEach
    void setUp() {

        questionnaireId = UUID.randomUUID();

        response = new QuestionnaireResponse(
                questionnaireId,
                "AIKP_2026",
                "AIKP Questionnaire",
                "AIKP questionnaire description",
                "1.0",
                "en",
                "DRAFT",
                "FORM",
                true,
                false);

        summary = new QuestionnaireSummary(
                questionnaireId,
                "AIKP_2026",
                "AIKP Questionnaire",
                "1.0",
                "DRAFT",
                true);
    }

    @Test
    void shouldCreateQuestionnaire() {

        CreateQuestionnaireRequest request =
                new CreateQuestionnaireRequest(
                        "AIKP_2026",
                        "AIKP Questionnaire",
                        "AIKP questionnaire description",
                        "1.0",
                        "en",
                        RenderType.FORM);

        when(applicationService.create(any()))
                .thenReturn(response);

        MockHttpServletRequest servletRequest =
                new MockHttpServletRequest();

        servletRequest.setRequestURI(
                "/api/v1/questionnaires");

        RequestContextHolder.setRequestAttributes(
                new ServletRequestAttributes(servletRequest));

        ResponseEntity<QuestionnaireResponse> result;

        try {
            result = controller.create(request);
        } finally {
            RequestContextHolder.resetRequestAttributes();
        }

        assertThat(result.getStatusCode())
                .isEqualTo(HttpStatus.CREATED);

        assertThat(result.getBody())
                .isEqualTo(response);

        assertThat(result.getHeaders().getLocation())
                .isNotNull();

        verify(applicationService)
                .create(any());
    }

    @Test
    void shouldGetQuestionnaireById() {

        when(applicationService.getById(any()))
                .thenReturn(response);

        ResponseEntity<QuestionnaireResponse> result =
                controller.get(questionnaireId);

        assertThat(result.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        assertThat(result.getBody())
                .isEqualTo(response);

        verify(applicationService)
                .getById(any());
    }

    @Test
    void shouldGetAllQuestionnaires() {

        when(applicationService.getAll(any()))
                .thenReturn(List.of(summary));

        ResponseEntity<List<QuestionnaireSummary>> result =
                controller.getAll();

        assertThat(result.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        assertThat(result.getBody())
                .containsExactly(summary);

        verify(applicationService)
                .getAll(any());
    }

    @Test
    void shouldGetActiveQuestionnaires() {

        when(applicationService.getActive(any()))
                .thenReturn(List.of(summary));

        ResponseEntity<List<QuestionnaireSummary>> result =
                controller.getActive();

        assertThat(result.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        assertThat(result.getBody())
                .containsExactly(summary);

        verify(applicationService)
                .getActive(any());
    }

    @Test
    void shouldGetPublishedQuestionnaires() {

        when(applicationService.getPublished(any()))
                .thenReturn(List.of(summary));

        ResponseEntity<List<QuestionnaireSummary>> result =
                controller.getPublished();

        assertThat(result.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        assertThat(result.getBody())
                .containsExactly(summary);

        verify(applicationService)
                .getPublished(any());
    }

    @Test
    void shouldUpdateQuestionnaire() {

        UpdateQuestionnaireRequest request =
                new UpdateQuestionnaireRequest(
                        "AIKP_2026",
                        "Updated AIKP Questionnaire",
                        "Updated description",
                        "1.1",
                        "en",
                        RenderType.FORM);

        when(applicationService.update(any()))
                .thenReturn(response);

        ResponseEntity<QuestionnaireResponse> result =
                controller.update(questionnaireId, request);

        assertThat(result.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        assertThat(result.getBody())
                .isEqualTo(response);

        verify(applicationService)
                .update(any());
    }

    @Test
    void shouldSubmitQuestionnaireForReview() {

        when(applicationService.submitForReview(any()))
                .thenReturn(response);

        ResponseEntity<QuestionnaireResponse> result =
                controller.submitForReview(questionnaireId);

        assertThat(result.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        assertThat(result.getBody())
                .isEqualTo(response);

        verify(applicationService)
                .submitForReview(any());
    }

    @Test
    void shouldRejectQuestionnaire() {

        when(applicationService.reject(any()))
                .thenReturn(response);

        ResponseEntity<QuestionnaireResponse> result =
                controller.reject(questionnaireId);

        assertThat(result.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        assertThat(result.getBody())
                .isEqualTo(response);

        verify(applicationService)
                .reject(any());
    }

    @Test
    void shouldApproveQuestionnaire() {

        when(applicationService.approve(any()))
                .thenReturn(response);

        ResponseEntity<QuestionnaireResponse> result =
                controller.approve(questionnaireId);

        assertThat(result.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        assertThat(result.getBody())
                .isEqualTo(response);

        verify(applicationService)
                .approve(any());
    }

    @Test
    void shouldActivateQuestionnaire() {

        when(applicationService.activate(any()))
                .thenReturn(response);

        ResponseEntity<QuestionnaireResponse> result =
                controller.activate(questionnaireId);

        assertThat(result.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        assertThat(result.getBody())
                .isEqualTo(response);

        verify(applicationService)
                .activate(any());
    }

    @Test
    void shouldDeactivateQuestionnaire() {

        when(applicationService.deactivate(any()))
                .thenReturn(response);

        ResponseEntity<QuestionnaireResponse> result =
                controller.deactivate(questionnaireId);

        assertThat(result.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        assertThat(result.getBody())
                .isEqualTo(response);

        verify(applicationService)
                .deactivate(any());
    }

    @Test
    void shouldPublishQuestionnaire() {

        when(applicationService.publish(any()))
                .thenReturn(response);

        ResponseEntity<QuestionnaireResponse> result =
                controller.publish(questionnaireId);

        assertThat(result.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        assertThat(result.getBody())
                .isEqualTo(response);

        verify(applicationService)
                .publish(any());
    }

    @Test
    void shouldArchiveQuestionnaire() {

        when(applicationService.archive(any()))
                .thenReturn(response);

        ResponseEntity<QuestionnaireResponse> result =
                controller.archive(questionnaireId);

        assertThat(result.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        assertThat(result.getBody())
                .isEqualTo(response);

        verify(applicationService)
                .archive(any());
    }


    @Test
    void shouldDeleteQuestionnaire() {

        ResponseEntity<Void> result =
                controller.delete(questionnaireId);

        assertThat(result.getStatusCode())
                .isEqualTo(HttpStatus.NO_CONTENT);

        assertThat(result.getBody())
                .isNull();

        verify(applicationService)
                .delete(any());
    }

    @Test
    void shouldCreateQuestionnaireGroup() {

        UUID groupId = UUID.randomUUID();

        CreateQuestionnaireGroupRequest request =
                new CreateQuestionnaireGroupRequest(
                        questionnaireId,
                        null,
                        "DIMENSION_01",
                        "Dimension 01",
                        "Dimension description",
                        QuestionnaireGroupType.DIMENSION,
                        1);

        QuestionnaireGroupResponse groupResponse =
                new QuestionnaireGroupResponse(
                        groupId,
                        questionnaireId,
                        null,
                        "DIMENSION_01",
                        "Dimension 01",
                        "Dimension description",
                        QuestionnaireGroupType.DIMENSION,
                        1,
                        true);

        when(questionnaireGroupApplicationService.createGroup(request))
                .thenReturn(groupResponse);

        MockHttpServletRequest servletRequest =
                new MockHttpServletRequest();

        servletRequest.setRequestURI(
                "/api/v1/questionnaires/" + questionnaireId + "/groups");

        RequestContextHolder.setRequestAttributes(
                new ServletRequestAttributes(servletRequest));

        ResponseEntity<QuestionnaireGroupResponse> result;

        try {
            result = controller.createGroup(
                    questionnaireId,
                    request);
        } finally {
            RequestContextHolder.resetRequestAttributes();
        }

        assertThat(result.getStatusCode())
                .isEqualTo(HttpStatus.CREATED);

        assertThat(result.getBody())
                .isEqualTo(groupResponse);

        assertThat(result.getHeaders().getLocation())
                .isNotNull();

        verify(questionnaireGroupApplicationService)
                .createGroup(request);
    }

    @Test
    void shouldRejectCreateGroupWhenPathAndRequestQuestionnaireIdsDiffer() {

        UUID requestQuestionnaireId = UUID.randomUUID();

        CreateQuestionnaireGroupRequest request =
                new CreateQuestionnaireGroupRequest(
                        requestQuestionnaireId,
                        null,
                        "DIMENSION_01",
                        "Dimension 01",
                        "Dimension description",
                        QuestionnaireGroupType.DIMENSION,
                        1);

        org.assertj.core.api.Assertions.assertThatThrownBy(
                () -> controller.createGroup(
                        questionnaireId,
                        request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(
                        "Questionnaire ID in path must match "
                                + "questionnaire ID in request.");

        verify(
                questionnaireGroupApplicationService,
                org.mockito.Mockito.never())
                .createGroup(any());
    }

    @Test
    void shouldGetGroupsByQuestionnaire() {

        UUID groupId = UUID.randomUUID();

        QuestionnaireGroupResponse groupResponse =
                new QuestionnaireGroupResponse(
                        groupId,
                        questionnaireId,
                        null,
                        "DIMENSION_01",
                        "Dimension 01",
                        "Dimension description",
                        QuestionnaireGroupType.DIMENSION,
                        1,
                        true);

        when(questionnaireGroupApplicationService
                .getGroupsByQuestionnaire(questionnaireId))
                .thenReturn(List.of(groupResponse));

        ResponseEntity<List<QuestionnaireGroupResponse>> result =
                controller.getGroupsByQuestionnaire(questionnaireId);

        assertThat(result.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        assertThat(result.getBody())
                .containsExactly(groupResponse);

        verify(questionnaireGroupApplicationService)
                .getGroupsByQuestionnaire(questionnaireId);
    }

    @Test
    void shouldGetGroupById() {

        UUID groupId = UUID.randomUUID();

        QuestionnaireGroupResponse groupResponse =
                new QuestionnaireGroupResponse(
                        groupId,
                        questionnaireId,
                        null,
                        "DIMENSION_01",
                        "Dimension 01",
                        "Dimension description",
                        QuestionnaireGroupType.DIMENSION,
                        1,
                        true);

        when(questionnaireGroupApplicationService
                .getGroupById(groupId))
                .thenReturn(groupResponse);

        ResponseEntity<QuestionnaireGroupResponse> result =
                controller.getGroupById(groupId);

        assertThat(result.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        assertThat(result.getBody())
                .isEqualTo(groupResponse);

        verify(questionnaireGroupApplicationService)
                .getGroupById(groupId);
    }

    @Test
    void shouldUpdateGroup() {

        UUID groupId = UUID.randomUUID();

        UpdateQuestionnaireGroupRequest request =
                new UpdateQuestionnaireGroupRequest(
                        "Updated Dimension",
                        "Updated description",
                        2);

        QuestionnaireGroupResponse groupResponse =
                new QuestionnaireGroupResponse(
                        groupId,
                        questionnaireId,
                        null,
                        "DIMENSION_01",
                        "Updated Dimension",
                        "Updated description",
                        QuestionnaireGroupType.DIMENSION,
                        2,
                        true);

        when(questionnaireGroupApplicationService
                .updateGroup(groupId, request))
                .thenReturn(groupResponse);

        ResponseEntity<QuestionnaireGroupResponse> result =
                controller.updateGroup(groupId, request);

        assertThat(result.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        assertThat(result.getBody())
                .isEqualTo(groupResponse);

        verify(questionnaireGroupApplicationService)
                .updateGroup(groupId, request);
    }

    @Test
    void shouldDeleteGroup() {

        UUID groupId = UUID.randomUUID();

        ResponseEntity<Void> result =
                controller.deleteGroup(groupId);

        assertThat(result.getStatusCode())
                .isEqualTo(HttpStatus.NO_CONTENT);

        assertThat(result.getBody())
                .isNull();

        verify(questionnaireGroupApplicationService)
                .deleteGroup(groupId);
    }

    @Test
    void shouldActivateGroup() {

        UUID groupId = UUID.randomUUID();

        QuestionnaireGroupResponse groupResponse =
                new QuestionnaireGroupResponse(
                        groupId,
                        questionnaireId,
                        null,
                        "DIMENSION_01",
                        "Dimension 01",
                        "Dimension description",
                        QuestionnaireGroupType.DIMENSION,
                        1,
                        true);

        when(questionnaireGroupApplicationService
                .activateGroup(groupId))
                .thenReturn(groupResponse);

        ResponseEntity<QuestionnaireGroupResponse> result =
                controller.activateGroup(groupId);

        assertThat(result.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        assertThat(result.getBody())
                .isEqualTo(groupResponse);

        verify(questionnaireGroupApplicationService)
                .activateGroup(groupId);
    }

    @Test
    void shouldDeactivateGroup() {

        UUID groupId = UUID.randomUUID();

        QuestionnaireGroupResponse groupResponse =
                new QuestionnaireGroupResponse(
                        groupId,
                        questionnaireId,
                        null,
                        "DIMENSION_01",
                        "Dimension 01",
                        "Dimension description",
                        QuestionnaireGroupType.DIMENSION,
                        1,
                        false);

        when(questionnaireGroupApplicationService
                .deactivateGroup(groupId))
                .thenReturn(groupResponse);

        ResponseEntity<QuestionnaireGroupResponse> result =
                controller.deactivateGroup(groupId);

        assertThat(result.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        assertThat(result.getBody())
                .isEqualTo(groupResponse);

        verify(questionnaireGroupApplicationService)
                .deactivateGroup(groupId);
    }

}
