package org.afdb.aikp.modules.quality.infrastructure.persistence.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.afdb.aikp.modules.quality.infrastructure.persistence.entity.QualityFindingEntity;

public interface QualityFindingJpaRepository
        extends JpaRepository<QualityFindingEntity, UUID> {

    List<QualityFindingEntity> findByQualityRunIdOrderBySeverityAsc(
            UUID qualityRunId);

    @Query("""
        select f
        from QualityFindingEntity f
        join QualityRunEntity r
          on r.id = f.qualityRunId
        where r.dataCollectionId = :dataCollectionId
        order by r.startedAt desc, f.severity asc
        """)
    List<QualityFindingEntity> findByDataCollectionId(
            @Param("dataCollectionId") UUID dataCollectionId);

    void deleteByQualityRunId(UUID qualityRunId);
}
