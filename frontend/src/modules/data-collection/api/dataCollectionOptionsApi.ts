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
    >("/campaigns");

    return response.data;
}

export async function getCountries(): Promise<
    CountryOption[]
> {
    const response = await axiosClient.get<
        CountryOption[]
    >("/countries");

    return response.data;
}

export async function getQuestionnaires(): Promise<
    QuestionnaireOption[]
> {
    const response = await axiosClient.get<
        QuestionnaireOption[]
    >("/questionnaires");

    return response.data;
}

export async function getOrganizations(): Promise<
    OrganizationOption[]
> {
    const response = await axiosClient.get<
        OrganizationOption[]
    >("/organizations");

    return response.data;
}

export async function getPersons(): Promise<
    PersonOption[]
> {
    const response = await axiosClient.get<
        PersonOption[]
    >("/persons");

    return response.data;
}

export async function getCountriesByCampaign(
    campaignId: string,
): Promise<CampaignCountryOption[]> {
    const response = await axiosClient.get<
        CampaignCountryOption[]
    >(
        `/campaigns/${campaignId}/countries`,
    );

    return response.data;
}
