import { axiosClient } from "@/shared/api/axiosClient";

export interface CampaignOption {
    id: string;
    code: string;
    name: string;
    active: boolean;
}

export interface CountryOption {
    id: string;
    iso2Code: string;
    iso3Code: string;
    name: string;
    active: boolean;
}

export interface QuestionnaireOption {
    id: string;
    code: string;
    name: string;
    version: string;
    status: string;
    active: boolean;
}

export interface OrganizationOption {
    id: string;
    code: string;
    name: string;
    countryId: string;
    active: boolean;
}

export interface PersonOption {
    id: string;
    fullName: string;
    organizationId: string;
    active: boolean;
}

export interface CampaignCountryOption {
    id: string;
    campaignId: string;
    countryId: string;
    createdAt: string;
    updatedAt: string;
}

export async function getCampaigns(): Promise<
    CampaignOption[]
> {
    const response = await axiosClient.get<
        CampaignOption[]
    >("/v1/campaigns");

    return response.data;
}

export async function getCountries(): Promise<
    CountryOption[]
> {
    const response = await axiosClient.get<
        CountryOption[]
    >("/v1/countries");

    return response.data;
}

export async function getQuestionnaires(): Promise<
    QuestionnaireOption[]
> {
    const response = await axiosClient.get<
        QuestionnaireOption[]
    >("/v1/questionnaires");

    return response.data;
}

export async function getOrganizations(): Promise<
    OrganizationOption[]
> {
    const response = await axiosClient.get<
        OrganizationOption[]
    >("/v1/organizations");

    return response.data;
}

export async function getPersons(): Promise<
    PersonOption[]
> {
    const response = await axiosClient.get<
        PersonOption[]
    >("/v1/persons");

    return response.data;
}

export async function getCountriesByCampaign(
    campaignId: string,
): Promise<CampaignCountryOption[]> {
    const response = await axiosClient.get<
        CampaignCountryOption[]
    >(
        `/v1/campaigns/${campaignId}/countries`,
    );

    return response.data;
}
