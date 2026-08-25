package org.afdb.aikp.modules.collection.infrastructure.persistence.mapper;

import org.afdb.aikp.modules.campaign.domain.valueobject.CampaignId;
import org.afdb.aikp.modules.collection.domain.model.DataCollection;
import org.afdb.aikp.modules.collection.domain.valueobject.DataCollectionId;
import org.afdb.aikp.modules.collection.infrastructure.persistence.entity.DataCollectionEntity;
import org.afdb.aikp.modules.country.domain.valueobject.CountryId;
import org.afdb.aikp.modules.organization.domain.valueobject.OrganizationId;
import org.afdb.aikp.modules.person.domain.valueobject.PersonId;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireId;

import org.springframework.stereotype.Component;

/**
 * Maps DataCollection domain objects to persistence entities
 * and persistence entities back to domain objects.
 */
@Component
public class DataCollectionPersistenceMapper {

    public DataCollectionEntity toEntity(
            DataCollection dataCollection) {

        return new DataCollectionEntity(
                dataCollection.getDataCollectionId()
                        .getValue(),
                dataCollection.getCampaignId()
                        .getValue(),
                dataCollection.getCountryId()
                        .getValue(),
                dataCollection.getQuestionnaireId()
                        .getValue(),
                dataCollection.getResponsibleOrganizationId()
                        .getValue(),
                dataCollection.getDataCollectorId()
                        .getValue(),
                dataCollection.getStatus());
    }

    public DataCollection toDomain(
            DataCollectionEntity entity) {

        return DataCollection.restore(
                DataCollectionId.of(entity.getId()),
                CampaignId.of(entity.getCampaignId()),
                CountryId.of(entity.getCountryId()),
                QuestionnaireId.of(
                        entity.getQuestionnaireId()),
                OrganizationId.of(
                        entity.getResponsibleOrganizationId()),
                PersonId.of(
                        entity.getDataCollectorId()),
                entity.getStatus());
    }
}
