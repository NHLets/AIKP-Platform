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
    >("/questionnaires");

    return response.data;
}

export async function getQuestionnaire(
    id: string,
): Promise<Questionnaire> {
    const response = await axiosClient.get<
        Questionnaire
    >(`/questionnaires/${id}`);

    return response.data;
}

export async function createQuestionnaire(
    request: CreateQuestionnaireRequest,
): Promise<Questionnaire> {
    const response = await axiosClient.post<
        Questionnaire
    >(
        "/questionnaires",
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
        `/questionnaires/${id}`,
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
        `/questionnaires/${id}/submit-for-review`,
    );

    return response.data;
}

export async function approveQuestionnaire(
    id: string,
): Promise<Questionnaire> {
    const response = await axiosClient.patch<
        Questionnaire
    >(
        `/questionnaires/${id}/approve`,
    );

    return response.data;
}

export async function activateQuestionnaire(
    id: string,
): Promise<Questionnaire> {
    const response = await axiosClient.patch<
        Questionnaire
    >(
        `/questionnaires/${id}/activate`,
    );

    return response.data;
}

export async function deactivateQuestionnaire(
    id: string,
): Promise<Questionnaire> {
    const response = await axiosClient.patch<
        Questionnaire
    >(
        `/questionnaires/${id}/deactivate`,
    );

    return response.data;
}

export async function publishQuestionnaire(
    id: string,
): Promise<Questionnaire> {
    const response = await axiosClient.patch<
        Questionnaire
    >(
        `/questionnaires/${id}/publish`,
    );

    return response.data;
}

export async function archiveQuestionnaire(
    id: string,
): Promise<Questionnaire> {
    const response = await axiosClient.patch<
        Questionnaire
    >(
        `/questionnaires/${id}/archive`,
    );

    return response.data;
}

export async function deleteQuestionnaire(
    id: string,
): Promise<void> {
    await axiosClient.delete(
        `/questionnaires/${id}`,
    );
}
