import { useQuery } from "@tanstack/react-query";
import { getValidationStatistics } from "../api/statisticsApi";

export function useValidationStatistics(
  dataCollectionId?: string,
) {
  return useQuery({
    queryKey: ["validation-statistics", dataCollectionId],
    queryFn: () =>
      getValidationStatistics(dataCollectionId!),
    enabled: !!dataCollectionId,
  });
}
