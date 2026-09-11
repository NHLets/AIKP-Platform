import { axiosClient } from "@/shared/api/axiosClient";

import type { QuestionnaireVariable } from "../types/questionnaireVariable.types";

export async function getQuestionnaireVariables(
    questionnaireId: string,
): Promise<QuestionnaireVariable[]> {
    const response =
        await axiosClient.get<QuestionnaireVariable[]>(
            `/questionnaire-variables/questionnaire/${questionnaireId}`,
        );

    return response.data;
}
