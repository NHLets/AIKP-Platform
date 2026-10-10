package org.afdb.aikp.modules.quality.domain.repository;

import java.util.List;
import java.util.Optional;

import org.afdb.aikp.modules.collection.domain.valueobject.DataCollectionId;
import org.afdb.aikp.modules.quality.domain.model.QualityRun;
import org.afdb.aikp.modules.quality.domain.valueobject.QualityRunId;

public interface QualityRunRepository {

    QualityRun save(QualityRun run);

    Optional<QualityRun> findById(QualityRunId id);

    List<QualityRun> findByDataCollectionId(DataCollectionId dataCollectionId);

    Optional<QualityRun> findLatestByDataCollectionId(
            DataCollectionId dataCollectionId);
}
