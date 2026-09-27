import { api } from "@/lib/api";
import type { ValidationSeverity } from "../types/validationSeverity";

export async function getValidationSeverity(
  dataCollectionId: string,
): Promise<ValidationSeverity[]> {

  const { data } = await api.get(
    `/validation/statistics/${dataCollectionId}/severity`,
  );

  return data;
}
