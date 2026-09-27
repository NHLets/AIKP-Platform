import { api } from "@/lib/api";
import type { SeverityDistribution } from "../types/severityDistribution";

export async function getSeverityDistribution(
  dataCollectionId: string,
): Promise<SeverityDistribution[]> {

  const { data } = await api.get(
    `/validation/statistics/${dataCollectionId}/severity`,
  );

  return data;
}
