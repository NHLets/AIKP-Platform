package org.afdb.aikp.modules.dashboard.application.response;

public record DashboardResponse(
        long totalCampaigns,
        long activeCampaigns,
        long totalCountries,
        long totalDataCollections,
        long draftDataCollections,
        long inProgressDataCollections,
        long submittedDataCollections,
        long validatedDataCollections,
        long rejectedDataCollections,
        long totalPersons,
        long activePersons) {}
