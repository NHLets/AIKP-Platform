import { api } from "@/lib/api";
import type { RejectedObservation } from "../types/rejectedObservation";

export async function getRejectedObservations(
  dataCollectionId: string,
): Promise<RejectedObservation[]> {

  const { data } = await api.get(
    `/validation/statistics/${dataCollectionId}/rejected`,
  );

  return data;
}
