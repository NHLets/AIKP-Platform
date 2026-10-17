package org.afdb.aikp.modules.organization.application.port.out;

import java.util.UUID;

/**
 * Checks whether an organization is referenced by any Data Collection.
 */
public interface DataCollectionUsageChecker {

    boolean isUsedByAnyDataCollection(UUID organizationId);
}
