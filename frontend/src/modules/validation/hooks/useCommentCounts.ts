import { useQuery } from "@tanstack/react-query";
import { getCommentCounts } from "../api/commentCountApi";

export function useCommentCounts(
  dataCollectionId?: string,
) {
  return useQuery({
    queryKey: ["comment-counts", dataCollectionId],
    queryFn: () => getCommentCounts(dataCollectionId!),
    enabled: !!dataCollectionId,
  });
}
