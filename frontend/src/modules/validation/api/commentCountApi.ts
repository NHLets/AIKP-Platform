import { api } from "@/lib/api";
import type { ObservationCommentCount } from "../types/commentCount";

export async function getCommentCounts(
  dataCollectionId: string,
): Promise<ObservationCommentCount[]> {
  const { data } = await api.get(
    `/validation-comments/counts/${dataCollectionId}`,
  );

  return data;
}
