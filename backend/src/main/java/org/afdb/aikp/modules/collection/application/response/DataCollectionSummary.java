package org.afdb.aikp.modules.collection.application.response;

import java.util.UUID;

import org.afdb.aikp.modules.collection.domain.enums.DataCollectionStatus;

/**
 * Lightweight representation of a DataCollection.
 */
public record DataCollectionSummary(
        UUID id,
        UUID campaignId,
        UUID countryId,
        UUID questionnaireId,
        UUID responsibleOrganizationId,
        UUID dataCollectorId,
        DataCollectionStatus status) {
}
