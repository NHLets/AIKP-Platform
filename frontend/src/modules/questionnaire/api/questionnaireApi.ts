import { axiosClient } from
    "@/shared/api/axiosClient";

import type {
    CreateQuestionnaireRequest,
    Questionnaire,
    QuestionnaireSummary,
    UpdateQuestionnaireRequest,
} from
    "../types/questionnaire.types";

export async function getQuestionnaires(): Promise<
    QuestionnaireSummary[]
> {
    const response = await axiosClient.get<
        QuestionnaireSummary[]
    >("/v1/questionnaires");

    return response.data;
}

export async function getQuestionnaire(
    id: string,
): Promise<Questionnaire> {
    const response = await axiosClient.get<
        Questionnaire
    >(`/v1/questionnaires/${id}`);

    return response.data;
}

export async function createQuestionnaire(
    request: CreateQuestionnaireRequest,
): Promise<Questionnaire> {
    const response = await axiosClient.post<
        Questionnaire
    >(
        "/v1/questionnaires",
        request,
    );

    return response.data;
}

export async function updateQuestionnaire(
    id: string,
    request: UpdateQuestionnaireRequest,
): Promise<Questionnaire> {
    const response = await axiosClient.put<
        Questionnaire
    >(
        `/v1/questionnaires/${id}`,
        request,
    );

    return response.data;
}

export async function submitQuestionnaireForReview(
    id: string,
): Promise<Questionnaire> {
    const response = await axiosClient.patch<
        Questionnaire
    >(
        `/v1/questionnaires/${id}/submit-for-review`,
    );

    return response.data;
}

export async function approveQuestionnaire(
    id: string,
): Promise<Questionnaire> {
    const response = await axiosClient.patch<
        Questionnaire
    >(
        `/v1/questionnaires/${id}/approve`,
    );

    return response.data;
}

export async function activateQuestionnaire(
    id: string,
): Promise<Questionnaire> {
    const response = await axiosClient.patch<
        Questionnaire
    >(
        `/v1/questionnaires/${id}/activate`,
    );

    return response.data;
}

export async function deactivateQuestionnaire(
    id: string,
): Promise<Questionnaire> {
    const response = await axiosClient.patch<
        Questionnaire
    >(
        `/v1/questionnaires/${id}/deactivate`,
    );

    return response.data;
}

export async function publishQuestionnaire(
    id: string,
): Promise<Questionnaire> {
    const response = await axiosClient.patch<
        Questionnaire
    >(
        `/v1/questionnaires/${id}/publish`,
    );

    return response.data;
}

export async function archiveQuestionnaire(
    id: string,
): Promise<Questionnaire> {
    const response = await axiosClient.patch<
        Questionnaire
    >(
        `/v1/questionnaires/${id}/archive`,
    );

    return response.data;
}

export async function deleteQuestionnaire(
    id: string,
): Promise<void> {
    await axiosClient.delete(
        `/v1/questionnaires/${id}`,
    );
}
