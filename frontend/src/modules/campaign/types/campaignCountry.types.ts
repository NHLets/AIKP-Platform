export interface CampaignCountry {
    id: string;
    campaignId: string;
    countryId: string;
    createdAt: string;
    updatedAt: string;
}

export interface AddCountryToCampaignRequest {
    countryId: string;
}
