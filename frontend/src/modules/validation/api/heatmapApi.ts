import { api } from "@/lib/api";
import type { ValidationHeatmap } from "../types/heatmap";

export async function getValidationHeatmap(
  dataCollectionId: string,
): Promise<ValidationHeatmap[]> {
  const { data } = await api.get(
    `/validation/statistics/${dataCollectionId}/heatmap`,
  );
  return data;
}
