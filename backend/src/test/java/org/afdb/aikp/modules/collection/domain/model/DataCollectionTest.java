package org.afdb.aikp.modules.collection.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.afdb.aikp.modules.campaign.domain.valueobject.CampaignId;
import org.afdb.aikp.modules.collection.domain.enums.DataCollectionStatus;
import org.afdb.aikp.modules.collection.domain.valueobject.DataCollectionId;
import org.afdb.aikp.modules.country.domain.valueobject.CountryId;
import org.afdb.aikp.modules.organization.domain.valueobject.OrganizationId;
import org.afdb.aikp.modules.person.domain.valueobject.PersonId;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireId;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for DataCollection aggregate.
 */
class DataCollectionTest {

    @Test
    void shouldCreateDataCollectionInDraftStatus() {

        DataCollection dataCollection =
                createDataCollection();

        assertEquals(
                DataCollectionStatus.DRAFT,
                dataCollection.getStatus());
    }

    @Test
    void shouldStartDraftDataCollection() {

        DataCollection dataCollection =
                createDataCollection();

        dataCollection.start();

        assertEquals(
                DataCollectionStatus.IN_PROGRESS,
                dataCollection.getStatus());
    }

    @Test
    void shouldNotStartNonDraftDataCollection() {

        DataCollection dataCollection =
                createDataCollection();

        dataCollection.start();

        assertThrows(
                IllegalStateException.class,
                dataCollection::start);
    }

    @Test
    void shouldSubmitInProgressDataCollection() {

        DataCollection dataCollection =
                createDataCollection();

        dataCollection.start();
        dataCollection.submit();

        assertEquals(
                DataCollectionStatus.SUBMITTED,
                dataCollection.getStatus());
    }

    @Test
    void shouldNotSubmitDraftDataCollection() {

        DataCollection dataCollection =
                createDataCollection();

        assertThrows(
                IllegalStateException.class,
                dataCollection::submit);
    }

    @Test
    void shouldValidateSubmittedDataCollection() {

        DataCollection dataCollection =
                createDataCollection();

        dataCollection.start();
        dataCollection.submit();
        dataCollection.validate();

        assertEquals(
                DataCollectionStatus.VALIDATED,
                dataCollection.getStatus());
    }

    @Test
    void shouldRejectSubmittedDataCollection() {

        DataCollection dataCollection =
                createDataCollection();

        dataCollection.start();
        dataCollection.submit();
        dataCollection.reject();

        assertEquals(
                DataCollectionStatus.REJECTED,
                dataCollection.getStatus());
    }

    @Test
    void shouldCancelDataCollection() {

        DataCollection dataCollection =
                createDataCollection();

        dataCollection.cancel();

        assertEquals(
                DataCollectionStatus.CANCELLED,
                dataCollection.getStatus());
    }

    @Test
    void shouldNotCancelValidatedDataCollection() {

        DataCollection dataCollection =
                createDataCollection();

        dataCollection.start();
        dataCollection.submit();
        dataCollection.validate();

        assertThrows(
                IllegalStateException.class,
                dataCollection::cancel);
    }

    @Test
    void shouldChangeResponsibleOrganization() {

        DataCollection dataCollection =
                createDataCollection();

        OrganizationId newOrganizationId =
                OrganizationId.generate();

        dataCollection.changeResponsibleOrganization(
                newOrganizationId);

        assertEquals(
                newOrganizationId,
                dataCollection
                        .getResponsibleOrganizationId());
    }

    @Test
    void shouldChangeDataCollector() {

        DataCollection dataCollection =
                createDataCollection();

        PersonId newPersonId =
                PersonId.generate();

        dataCollection.changeDataCollector(
                newPersonId);

        assertEquals(
                newPersonId,
                dataCollection.getDataCollectorId());
    }

    private DataCollection createDataCollection() {

        return DataCollection.create(
                DataCollectionId.generate(),
                CampaignId.generate(),
                CountryId.generate(),
                QuestionnaireId.generate(),
                OrganizationId.generate(),
                PersonId.generate());
    }
}
