import { axiosClient } from "@/shared/api/axiosClient";

import type {
    Campaign,
    CampaignSummary,
    CreateCampaignRequest,
    UpdateCampaignRequest,
} from "../types/campaign.types";


export async function getCampaigns(): Promise<CampaignSummary[]> {
    const response = await axiosClient.get<CampaignSummary[]>(
        "/v1/campaigns",
    );

    return response.data;
}


export async function getCampaignById(
    id: string,
): Promise<Campaign> {
    const response = await axiosClient.get<Campaign>(
        `/v1/campaigns/${id}`,
    );

    return response.data;
}


export async function createCampaign(
    request: CreateCampaignRequest,
): Promise<Campaign> {
    const response = await axiosClient.post<Campaign>(
        "/v1/campaigns",
        request,
    );

    return response.data;
}


export async function updateCampaign(
    id: string,
    request: UpdateCampaignRequest,
): Promise<Campaign> {
    const response = await axiosClient.put<Campaign>(
        `/v1/campaigns/${id}`,
        request,
    );

    return response.data;
}


export async function planCampaign(
    id: string,
): Promise<Campaign> {
    const response = await axiosClient.patch<Campaign>(
        `/v1/campaigns/${id}/plan`,
    );

    return response.data;
}


export async function activateCampaign(
    id: string,
): Promise<Campaign> {
    const response = await axiosClient.patch<Campaign>(
        `/v1/campaigns/${id}/activate`,
    );

    return response.data;
}


export async function completeCampaign(
    id: string,
): Promise<Campaign> {
    const response = await axiosClient.patch<Campaign>(
        `/v1/campaigns/${id}/complete`,
    );

    return response.data;
}


export async function archiveCampaign(
    id: string,
): Promise<Campaign> {
    const response = await axiosClient.patch<Campaign>(
        `/v1/campaigns/${id}/archive`,
    );

    return response.data;
}


export async function deleteCampaign(
    id: string,
): Promise<void> {
    await axiosClient.delete(
        `/v1/campaigns/${id}`,
    );
}