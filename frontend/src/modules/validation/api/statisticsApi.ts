import { api } from "@/lib/api";
import type { ValidationStatistics } from "../types/statistics";

export async function getValidationStatistics(
  dataCollectionId: string,
): Promise<ValidationStatistics> {
  const { data } = await api.get(
    `/validation/statistics/${dataCollectionId}`,
  );

  return data;
}
