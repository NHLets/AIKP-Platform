import { useQuery } from "@tanstack/react-query";
import { getRejectedObservations } from "../api/rejectedObservationApi";

export function useRejectedObservations(
  dataCollectionId?: string,
) {

  return useQuery({
    queryKey: ["rejected-observations", dataCollectionId],
    queryFn: () => getRejectedObservations(dataCollectionId!),
    enabled: !!dataCollectionId,
  });

}
