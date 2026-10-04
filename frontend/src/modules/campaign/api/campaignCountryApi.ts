import { axiosClient } from "../../../shared/api/axiosClient";

import type {
    AddCountryToCampaignRequest,
    CampaignCountry,
} from "../types/campaignCountry.types";


const BASE_PATH = "/v1/campaigns";


export async function getCampaignCountries(
    campaignId: string,
): Promise<CampaignCountry[]> {
    const response =
        await axiosClient.get<CampaignCountry[]>(
            `${BASE_PATH}/${campaignId}/countries`,
        );

    return response.data;
}


export async function getCampaignCountry(
    campaignId: string,
    countryId: string,
): Promise<CampaignCountry> {
    const response =
        await axiosClient.get<CampaignCountry>(
            `${BASE_PATH}/${campaignId}/countries/${countryId}`,
        );

    return response.data;
}


export async function addCountryToCampaign(
    campaignId: string,
    request: AddCountryToCampaignRequest,
): Promise<CampaignCountry> {
    const response =
        await axiosClient.post<CampaignCountry>(
            `${BASE_PATH}/${campaignId}/countries`,
            request,
        );

    return response.data;
}


export async function removeCountryFromCampaign(
    campaignId: string,
    countryId: string,
): Promise<void> {
    await axiosClient.delete(
        `${BASE_PATH}/${campaignId}/countries/${countryId}`,
    );
}
