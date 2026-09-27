import { useQuery } from "@tanstack/react-query";
import { getQuestionnaireProgress } from "../api/questionnaireProgressApi";

export function useQuestionnaireProgress(
  dataCollectionId?: string,
) {

  return useQuery({
    queryKey: ["questionnaire-progress", dataCollectionId],
    queryFn: () => getQuestionnaireProgress(dataCollectionId!),
    enabled: !!dataCollectionId,
  });

}
