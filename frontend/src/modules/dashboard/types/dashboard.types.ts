export interface DashboardResponse {
    totalCampaigns: number;
    activeCampaigns: number;
    totalCountries: number;
    totalDataCollections: number;
    draftDataCollections: number;
    inProgressDataCollections: number;
    submittedDataCollections: number;
    validatedDataCollections: number;
    rejectedDataCollections: number;
    cancelledDataCollections: number;
    totalPersons: number;
    activePersons: number;
}
