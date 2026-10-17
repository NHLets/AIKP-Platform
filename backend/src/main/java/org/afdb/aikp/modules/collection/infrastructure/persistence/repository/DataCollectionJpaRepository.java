package org.afdb.aikp.modules.collection.infrastructure.persistence.repository;

import java.util.List;
import java.util.UUID;

import org.afdb.aikp.modules.collection.domain.enums.DataCollectionStatus;
import org.afdb.aikp.modules.collection.infrastructure.persistence.entity.DataCollectionEntity;
import org.afdb.aikp.modules.questionnaire.infrastructure.persistence.entity.QuestionnaireEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for DataCollectionEntity.
 */
@Repository
public interface DataCollectionJpaRepository
        extends JpaRepository<DataCollectionEntity, UUID> {

    List<DataCollectionEntity> findByCampaignId(
            UUID campaignId);

    List<DataCollectionEntity> findByCountryId(
            UUID countryId);

    long countByStatus(DataCollectionStatus status);

    @Query("select case when count(dc) > 0 then true else false end "
            + "from DataCollectionEntity dc "
            + "where dc.responsibleOrganizationId = :organizationId "
            + "or dc.operatorOrganizationId = :organizationId "
            + "or dc.dataCollectorId = :organizationId")
    boolean existsByOrganizationId(
            @Param("organizationId") UUID organizationId);

    @Query("select dc.countryId "
            + "from DataCollectionEntity dc "
            + "where dc.questionnaireId in ( "
            + "select q.id from QuestionnaireEntity q "
            + "where q.code in ('PW_A', 'PW_B', 'PW_C')) "
            + "and dc.status = :status "
            + "group by dc.countryId "
            + "having count(distinct dc.questionnaireId) = 3")
    List<UUID> findCountriesWithAllValidatedPw(
            @Param("status") DataCollectionStatus status);

    @Query("select count(distinct dc.countryId) "
            + "from DataCollectionEntity dc "
            + "where dc.questionnaireId = ( "
            + "select q.id from QuestionnaireEntity q "
            + "where q.code = :questionnaireCode) "
            + "and dc.status = :status")
    long countDistinctCountriesWithQuestionnaireStatus(
            @Param("questionnaireCode") String questionnaireCode,
            @Param("status") DataCollectionStatus status);
}
