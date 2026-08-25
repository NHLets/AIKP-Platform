package org.afdb.aikp.modules.collection.infrastructure.persistence.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.UUID;

import org.afdb.aikp.modules.campaign.domain.valueobject.CampaignId;
import org.afdb.aikp.modules.collection.domain.enums.DataCollectionStatus;
import org.afdb.aikp.modules.collection.domain.model.DataCollection;
import org.afdb.aikp.modules.collection.domain.valueobject.DataCollectionId;
import org.afdb.aikp.modules.collection.infrastructure.persistence.entity.DataCollectionEntity;
import org.afdb.aikp.modules.country.domain.valueobject.CountryId;
import org.afdb.aikp.modules.organization.domain.valueobject.OrganizationId;
import org.afdb.aikp.modules.person.domain.valueobject.PersonId;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireId;
import org.junit.jupiter.api.Test;

class DataCollectionPersistenceMapperTest {

    private final DataCollectionPersistenceMapper mapper =
            new DataCollectionPersistenceMapper();

    @Test
    void shouldMapDomainToEntity() {

        DataCollectionId dataCollectionId =
                DataCollectionId.generate();

        CampaignId campaignId =
                CampaignId.generate();

        CountryId countryId =
                CountryId.generate();

        QuestionnaireId questionnaireId =
                QuestionnaireId.generate();

        OrganizationId organizationId =
                OrganizationId.generate();

        PersonId personId =
                PersonId.generate();

        DataCollection dataCollection =
                DataCollection.create(
                        dataCollectionId,
                        campaignId,
                        countryId,
                        questionnaireId,
                        organizationId,
                        personId);

        DataCollectionEntity entity =
                mapper.toEntity(dataCollection);

        assertNotNull(entity);

        assertEquals(
                dataCollectionId.getValue(),
                entity.getId());

        assertEquals(
                campaignId.getValue(),
                entity.getCampaignId());

        assertEquals(
                countryId.getValue(),
                entity.getCountryId());

        assertEquals(
                questionnaireId.getValue(),
                entity.getQuestionnaireId());

        assertEquals(
                organizationId.getValue(),
                entity.getResponsibleOrganizationId());

        assertEquals(
                personId.getValue(),
                entity.getDataCollectorId());

        assertEquals(
                DataCollectionStatus.DRAFT,
                entity.getStatus());
    }

    @Test
    void shouldMapEntityToDomain() {

        UUID dataCollectionId =
                UUID.randomUUID();

        UUID campaignId =
                UUID.randomUUID();

        UUID countryId =
                UUID.randomUUID();

        UUID questionnaireId =
                UUID.randomUUID();

        UUID organizationId =
                UUID.randomUUID();

        UUID personId =
                UUID.randomUUID();

        DataCollectionEntity entity =
                new DataCollectionEntity(
                        dataCollectionId,
                        campaignId,
                        countryId,
                        questionnaireId,
                        organizationId,
                        personId,
                        DataCollectionStatus.SUBMITTED);

        DataCollection dataCollection =
                mapper.toDomain(entity);

        assertNotNull(dataCollection);

        assertEquals(
                dataCollectionId,
                dataCollection
                        .getDataCollectionId()
                        .getValue());

        assertEquals(
                campaignId,
                dataCollection
                        .getCampaignId()
                        .getValue());

        assertEquals(
                countryId,
                dataCollection
                        .getCountryId()
                        .getValue());

        assertEquals(
                questionnaireId,
                dataCollection
                        .getQuestionnaireId()
                        .getValue());

        assertEquals(
                organizationId,
                dataCollection
                        .getResponsibleOrganizationId()
                        .getValue());

        assertEquals(
                personId,
                dataCollection
                        .getDataCollectorId()
                        .getValue());

        assertEquals(
                DataCollectionStatus.SUBMITTED,
                dataCollection.getStatus());
    }

    @Test
    void shouldPreserveAllFieldsDuringRoundTrip() {

        DataCollection original =
                DataCollection.create(
                        DataCollectionId.generate(),
                        CampaignId.generate(),
                        CountryId.generate(),
                        QuestionnaireId.generate(),
                        OrganizationId.generate(),
                        PersonId.generate());

        DataCollectionEntity entity =
                mapper.toEntity(original);

        DataCollection restored =
                mapper.toDomain(entity);

        assertEquals(
                original.getDataCollectionId(),
                restored.getDataCollectionId());

        assertEquals(
                original.getCampaignId(),
                restored.getCampaignId());

        assertEquals(
                original.getCountryId(),
                restored.getCountryId());

        assertEquals(
                original.getQuestionnaireId(),
                restored.getQuestionnaireId());

        assertEquals(
                original.getResponsibleOrganizationId(),
                restored.getResponsibleOrganizationId());

        assertEquals(
                original.getDataCollectorId(),
                restored.getDataCollectorId());

        assertEquals(
                original.getStatus(),
                restored.getStatus());
    }
}
