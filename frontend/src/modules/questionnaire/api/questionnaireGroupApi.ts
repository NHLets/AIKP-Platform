import { axiosClient } from "@/shared/api/axiosClient";

import type { QuestionnaireGroup } from "../types/questionnaireGroup.types";

export async function getQuestionnaireGroups(
    questionnaireId: string,
): Promise<QuestionnaireGroup[]> {
    const response = await axiosClient.get<QuestionnaireGroup[]>(
        `/questionnaires/${questionnaireId}/groups`,
    );

    return response.data;
}
