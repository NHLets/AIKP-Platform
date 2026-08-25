package org.afdb.aikp.modules.collection.application.mapper;

import org.afdb.aikp.modules.collection.application.response.DataCollectionResponse;
import org.afdb.aikp.modules.collection.application.response.DataCollectionSummary;
import org.afdb.aikp.modules.collection.domain.model.DataCollection;

import org.springframework.stereotype.Component;

/**
 * Maps DataCollection domain objects to application responses.
 */
@Component
public class DataCollectionApplicationMapper {

    public DataCollectionResponse toResponse(
            DataCollection dataCollection) {

        return new DataCollectionResponse(
                dataCollection.getDataCollectionId().getValue(),
                dataCollection.getCampaignId().getValue(),
                dataCollection.getCountryId().getValue(),
                dataCollection.getQuestionnaireId().getValue(),
                dataCollection
                        .getResponsibleOrganizationId()
                        .getValue(),
                dataCollection
                        .getDataCollectorId()
                        .getValue(),
                dataCollection.getStatus());
    }

    public DataCollectionSummary toSummary(
            DataCollection dataCollection) {

        return new DataCollectionSummary(
                dataCollection.getDataCollectionId().getValue(),
                dataCollection.getCampaignId().getValue(),
                dataCollection.getCountryId().getValue(),
                dataCollection.getQuestionnaireId().getValue(),
                dataCollection
                        .getResponsibleOrganizationId()
                        .getValue(),
                dataCollection
                        .getDataCollectorId()
                        .getValue(),
                dataCollection.getStatus());
    }
}
