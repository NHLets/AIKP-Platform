package org.afdb.aikp.modules.quality.domain.repository;

import java.util.List;

import org.afdb.aikp.modules.collection.domain.valueobject.DataCollectionId;
import org.afdb.aikp.modules.quality.domain.model.QualityFinding;
import org.afdb.aikp.modules.quality.domain.valueobject.QualityRunId;

public interface QualityFindingRepository {

    QualityFinding save(QualityFinding finding);

    List<QualityFinding> findByQualityRunId(QualityRunId qualityRunId);

    List<QualityFinding> findByDataCollectionId(
            DataCollectionId dataCollectionId);

    void deleteByQualityRunId(QualityRunId qualityRunId);
}
