import apiClient from "@/shared/api/apiClient";
import type {
  ValidationComment,
  CreateValidationCommentRequest,
} from "../types/validation";

const BASE_URL = "/validation-comments";

export const validationCommentsApi = {
  async getByObservation(
    observationId: string,
  ): Promise<ValidationComment[]> {
    const { data } = await apiClient.get<ValidationComment[]>(
      `${BASE_URL}/observation/${observationId}`,
    );
    return data;
  },

  async create(
    request: CreateValidationCommentRequest,
  ): Promise<ValidationComment> {
    const { data } = await apiClient.post<ValidationComment>(
      BASE_URL,
      request,
    );
    return data;
  },
};

export default validationCommentsApi;
