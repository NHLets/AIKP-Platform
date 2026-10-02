import axiosClient from "../shared/api/axiosClient";
import type { CampaignSummary } from "../types/campaign";

interface PageResponse<T> {
  content: T[];
}

export const campaignApi = {
  getAll: async (): Promise<CampaignSummary[]> => {
    const response = await axiosClient.get<PageResponse<CampaignSummary>>("/v1/campaigns");
    return response.data.content ?? [];
  },
};

export default campaignApi;
