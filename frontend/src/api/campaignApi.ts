import axiosClient from "../shared/api/axiosClient";
import { CampaignSummary } from "../types/campaign";

export const campaignApi = {

  getAll: async (): Promise<CampaignSummary[]> => {
    const response = await axiosClient.get("/campaigns");
    return response.data as CampaignSummary[];
  }

};

export default campaignApi;
