package org.afdb.aikp.modules.questionnaire.presentation.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.afdb.aikp.modules.questionnaire.application.dto.CreateQuestionnaireVariableRequest;
import org.afdb.aikp.modules.questionnaire.application.dto.QuestionnaireVariableResponse;
import org.afdb.aikp.modules.questionnaire.application.dto.UpdateQuestionnaireVariableRequest;
import org.afdb.aikp.modules.questionnaire.application.service.QuestionnaireVariableApplicationService;
import org.afdb.aikp.modules.questionnaire.domain.enums.QuestionnaireVariableDataType;
import org.afdb.aikp.modules.questionnaire.presentation.mapper.QuestionnaireVariableRestMapper;
import org.afdb.aikp.modules.questionnaire.presentation.request.CreateQuestionnaireVariableRestRequest;
import org.afdb.aikp.modules.questionnaire.presentation.request.UpdateQuestionnaireVariableRestRequest;

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
class QuestionnaireVariableControllerTest {

    @Mock
    private QuestionnaireVariableApplicationService applicationService;

    @Mock
    private QuestionnaireVariableRestMapper restMapper;

    @InjectMocks
    private QuestionnaireVariableController controller;

    private UUID variableId;
    private UUID questionnaireId;
    private UUID groupId;

    private QuestionnaireVariableResponse response;

    @BeforeEach
    void setUp() {

        variableId = UUID.randomUUID();
        questionnaireId = UUID.randomUUID();
        groupId = UUID.randomUUID();

        response = new QuestionnaireVariableResponse(
                variableId,
                questionnaireId,
                groupId,
                "GDP_01",
                "GDP",
                "Gross domestic product",
                QuestionnaireVariableDataType.DECIMAL,
                "USD",
                true,
                1,
                true);
    }

    @Test
    void shouldCreateVariable() {

        CreateQuestionnaireVariableRestRequest request =
                new CreateQuestionnaireVariableRestRequest(
                        questionnaireId,
                        groupId,
                        "GDP_01",
                        "GDP",
                        "Gross domestic product",
                        QuestionnaireVariableDataType.DECIMAL,
                        "USD",
                        true,
                        1);

        CreateQuestionnaireVariableRequest applicationRequest =
                new CreateQuestionnaireVariableRequest(
                        questionnaireId,
                        groupId,
                        "GDP_01",
                        "GDP",
                        "Gross domestic product",
                        QuestionnaireVariableDataType.DECIMAL,
                        "USD",
                        true,
                        1);

        when(restMapper.toApplicationRequest(request))
                .thenReturn(applicationRequest);

        when(applicationService.createVariable(applicationRequest))
                .thenReturn(response);

        MockHttpServletRequest servletRequest =
                new MockHttpServletRequest();

        servletRequest.setRequestURI(
                "/api/v1/questionnaire-variables");

        RequestContextHolder.setRequestAttributes(
                new ServletRequestAttributes(servletRequest));

        ResponseEntity<QuestionnaireVariableResponse> result;

        try {
            result = controller.create(request);
        } finally {
            RequestContextHolder.resetRequestAttributes();
        }

        assertThat(result.getStatusCode())
                .isEqualTo(HttpStatus.CREATED);

        assertThat(result.getBody())
                .isEqualTo(response);

        verify(applicationService)
                .createVariable(applicationRequest);
    }

    @Test
    void shouldGetVariableById() {

        when(applicationService.getVariableById(variableId))
                .thenReturn(response);

        ResponseEntity<QuestionnaireVariableResponse> result =
                controller.get(variableId);

        assertThat(result.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        assertThat(result.getBody())
                .isEqualTo(response);

        verify(applicationService)
                .getVariableById(variableId);
    }

    @Test
    void shouldGetVariablesByQuestionnaire() {

        when(applicationService
                .getVariablesByQuestionnaire(questionnaireId))
                .thenReturn(List.of(response));

        ResponseEntity<List<QuestionnaireVariableResponse>> result =
                controller.getByQuestionnaire(questionnaireId);

        assertThat(result.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        assertThat(result.getBody())
                .containsExactly(response);

        verify(applicationService)
                .getVariablesByQuestionnaire(questionnaireId);
    }

    @Test
    void shouldGetVariablesByGroup() {

        when(applicationService.getVariablesByGroup(groupId))
                .thenReturn(List.of(response));

        ResponseEntity<List<QuestionnaireVariableResponse>> result =
                controller.getByGroup(groupId);

        assertThat(result.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        assertThat(result.getBody())
                .containsExactly(response);

        verify(applicationService)
                .getVariablesByGroup(groupId);
    }

    @Test
    void shouldUpdateVariable() {

        UpdateQuestionnaireVariableRestRequest request =
                new UpdateQuestionnaireVariableRestRequest(
                        groupId,
                        "Updated GDP",
                        "Updated definition",
                        QuestionnaireVariableDataType.NUMBER,
                        "EUR",
                        false,
                        2);

        UpdateQuestionnaireVariableRequest applicationRequest =
                new UpdateQuestionnaireVariableRequest(
                        groupId,
                        "Updated GDP",
                        "Updated definition",
                        QuestionnaireVariableDataType.NUMBER,
                        "EUR",
                        false,
                        2);

        when(restMapper.toApplicationRequest(request))
                .thenReturn(applicationRequest);

        when(applicationService.updateVariable(
                variableId,
                applicationRequest))
                .thenReturn(response);

        ResponseEntity<QuestionnaireVariableResponse> result =
                controller.update(variableId, request);

        assertThat(result.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        assertThat(result.getBody())
                .isEqualTo(response);

        verify(applicationService)
                .updateVariable(variableId, applicationRequest);
    }

    @Test
    void shouldActivateVariable() {

        when(applicationService.activateVariable(variableId))
                .thenReturn(response);

        ResponseEntity<QuestionnaireVariableResponse> result =
                controller.activate(variableId);

        assertThat(result.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        assertThat(result.getBody())
                .isEqualTo(response);

        verify(applicationService)
                .activateVariable(variableId);
    }

    @Test
    void shouldDeactivateVariable() {

        when(applicationService.deactivateVariable(variableId))
                .thenReturn(response);

        ResponseEntity<QuestionnaireVariableResponse> result =
                controller.deactivate(variableId);

        assertThat(result.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        assertThat(result.getBody())
                .isEqualTo(response);

        verify(applicationService)
                .deactivateVariable(variableId);
    }

    @Test
    void shouldDeleteVariable() {

        ResponseEntity<Void> result =
                controller.delete(variableId);

        assertThat(result.getStatusCode())
                .isEqualTo(HttpStatus.NO_CONTENT);

        assertThat(result.getBody())
                .isNull();

        verify(applicationService)
                .deleteVariable(variableId);
    }
}
