import { axiosClient } from "@/shared/api/axiosClient";

import type { QuestionnaireVariable } from "../types/questionnaireVariable.types";

export async function getQuestionnaireVariables(
    questionnaireId: string,
): Promise<QuestionnaireVariable[]> {
    const response =
        await axiosClient.get<QuestionnaireVariable[]>(
            `/v1/questionnaire-variables/questionnaire/${questionnaireId}`,
        );

    return response.data;
}
