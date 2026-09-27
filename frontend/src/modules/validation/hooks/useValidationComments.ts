import { useCallback, useEffect, useState } from "react";
import validationCommentsApi from "../api/validationCommentsApi";
import type {
  ValidationComment,
  CreateValidationCommentRequest,
} from "../types/validation";

export function useValidationComments(observationId?: string) {
  const [comments, setComments] = useState<ValidationComment[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const refresh = useCallback(async () => {
    if (!observationId) {
      setComments([]);
      return;
    }

    try {
      setLoading(true);
      setError(null);

      const data =
        await validationCommentsApi.getByObservation(observationId);

      setComments(data);
    } catch (err) {
      setError(err instanceof Error ? err.message : "Unknown error");
    } finally {
      setLoading(false);
    }
  }, [observationId]);

  const createComment = useCallback(
    async (request: CreateValidationCommentRequest) => {
      const created = await validationCommentsApi.create(request);
      await refresh();
      return created;
    },
    [refresh],
  );

  useEffect(() => {
    void refresh();
  }, [refresh]);

  return {
    comments,
    loading,
    error,
    refresh,
    createComment,
  };
}
