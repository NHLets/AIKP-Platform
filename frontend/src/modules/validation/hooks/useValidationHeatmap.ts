import { useQuery } from "@tanstack/react-query";
import { getValidationHeatmap } from "../api/validationHeatmapApi";

export function useValidationHeatmap(
  dataCollectionId?: string,
) {

  return useQuery({
    queryKey: ["validation-heatmap", dataCollectionId],
    queryFn: () => getValidationHeatmap(dataCollectionId!),
    enabled: !!dataCollectionId,
  });

}
