package org.afdb.aikp.modules.organization.application.mapper;

import org.afdb.aikp.modules.organization.application.response.OrganizationResponse;
import org.afdb.aikp.modules.organization.application.response.OrganizationSummary;
import org.afdb.aikp.modules.organization.domain.model.Organization;

/**
 * Maps Organization domain aggregates to application responses.
 */
public final class OrganizationApplicationMapper {

    private OrganizationApplicationMapper() {
        // Utility class
    }

    /**
     * Maps an Organization aggregate to a detailed response.
     */
    public static OrganizationResponse toResponse(
            Organization organization) {

        return new OrganizationResponse(
                organization.getOrganizationId().getValue(),
                organization.getCode().getValue(),
                organization.getName().getValue(),
                organization.getType().name(),
                organization.getCountryId().getValue(),
                organization.isActive()
        );
    }

    /**
     * Maps an Organization aggregate to a summary response.
     */
    public static OrganizationSummary toSummary(
            Organization organization) {

        return new OrganizationSummary(
                organization.getOrganizationId().getValue(),
                organization.getCode().getValue(),
                organization.getName().getValue(),
                organization.getType().name(),
                organization.getCountryId().getValue(),
                organization.isActive()
        );
    }
}
