import axiosClient from "../shared/api/axiosClient";
import type { CampaignSummary } from "../types/campaign";



export const campaignApi = {
  getAll: async (): Promise<CampaignSummary[]> => {
    const response = await axiosClient.get<CampaignSummary[]>("/v1/campaigns");
    return response.data;
  },
};

export default campaignApi;
