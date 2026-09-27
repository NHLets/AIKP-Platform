import { api } from "@/lib/api";
import type { QuestionnaireProgress } from "../types/questionnaireProgress";

export async function getQuestionnaireProgress(
  dataCollectionId: string,
): Promise<QuestionnaireProgress[]> {

  const { data } = await api.get(
    `/validation/statistics/${dataCollectionId}/questionnaires`,
  );

  return data;
}
