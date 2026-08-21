import { axiosClient } from "@/shared/api/axiosClient";

import type {
    Campaign,
    CampaignSummary,
    CreateCampaignRequest,
    UpdateCampaignRequest,
} from "../types/campaign.types";


export async function getCampaigns(): Promise<CampaignSummary[]> {
    const response = await axiosClient.get<CampaignSummary[]>(
        "/campaigns",
    );

    return response.data;
}


export async function getCampaignById(
    id: string,
): Promise<Campaign> {
    const response = await axiosClient.get<Campaign>(
        `/campaigns/${id}`,
    );

    return response.data;
}


export async function createCampaign(
    request: CreateCampaignRequest,
): Promise<Campaign> {
    const response = await axiosClient.post<Campaign>(
        "/campaigns",
        request,
    );

    return response.data;
}


export async function updateCampaign(
    id: string,
    request: UpdateCampaignRequest,
): Promise<Campaign> {
    const response = await axiosClient.put<Campaign>(
        `/campaigns/${id}`,
        request,
    );

    return response.data;
}


export async function planCampaign(
    id: string,
): Promise<Campaign> {
    const response = await axiosClient.patch<Campaign>(
        `/campaigns/${id}/plan`,
    );

    return response.data;
}


export async function activateCampaign(
    id: string,
): Promise<Campaign> {
    const response = await axiosClient.patch<Campaign>(
        `/campaigns/${id}/activate`,
    );

    return response.data;
}


export async function completeCampaign(
    id: string,
): Promise<Campaign> {
    const response = await axiosClient.patch<Campaign>(
        `/campaigns/${id}/complete`,
    );

    return response.data;
}


export async function archiveCampaign(
    id: string,
): Promise<Campaign> {
    const response = await axiosClient.patch<Campaign>(
        `/campaigns/${id}/archive`,
    );

    return response.data;
}


export async function deleteCampaign(
    id: string,
): Promise<void> {
    await axiosClient.delete(
        `/campaigns/${id}`,
    );
}