package org.afdb.aikp.modules.collection.infrastructure.persistence.adapter;

import java.util.UUID;

import org.afdb.aikp.modules.collection.infrastructure.persistence.repository.DataCollectionJpaRepository;
import org.afdb.aikp.modules.organization.application.port.out.DataCollectionUsageChecker;
import org.springframework.stereotype.Component;

/**
 * Persistence implementation of the organization Data Collection usage check.
 */
@Component
public class DataCollectionUsageCheckerAdapter
        implements DataCollectionUsageChecker {

    private final DataCollectionJpaRepository repository;

    public DataCollectionUsageCheckerAdapter(
            DataCollectionJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean isUsedByAnyDataCollection(UUID organizationId) {
        return repository.existsByOrganizationId(organizationId);
    }
}
