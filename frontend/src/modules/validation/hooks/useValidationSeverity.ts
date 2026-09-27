import { useQuery } from "@tanstack/react-query";
import { getValidationSeverity } from "../api/validationSeverityApi";

export function useValidationSeverity(
  dataCollectionId?: string,
) {

  return useQuery({
    queryKey: ["validation-severity", dataCollectionId],
    queryFn: () =>
      getValidationSeverity(dataCollectionId!),
    enabled: !!dataCollectionId,
  });

}
