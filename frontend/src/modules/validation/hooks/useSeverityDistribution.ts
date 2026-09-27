import { useQuery } from "@tanstack/react-query";
import { getSeverityDistribution } from "../api/severityApi";

export function useSeverityDistribution(
  dataCollectionId?: string,
) {
  return useQuery({
    queryKey: ["severity-distribution", dataCollectionId],
    queryFn: () => getSeverityDistribution(dataCollectionId!),
    enabled: !!dataCollectionId,
  });
}
