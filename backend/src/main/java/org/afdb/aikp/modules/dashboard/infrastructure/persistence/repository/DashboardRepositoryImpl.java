package org.afdb.aikp.modules.dashboard.infrastructure.persistence.repository;

import org.afdb.aikp.modules.campaign.domain.enums.CampaignStatus;
import org.afdb.aikp.modules.campaign.infrastructure.persistence.repository.CampaignJpaRepository;
import org.afdb.aikp.modules.collection.domain.enums.DataCollectionStatus;
import org.afdb.aikp.modules.collection.infrastructure.persistence.repository.DataCollectionJpaRepository;
import org.afdb.aikp.modules.country.infrastructure.persistence.repository.CountryJpaRepository;
import org.afdb.aikp.modules.dashboard.application.response.DashboardResponse;
import org.afdb.aikp.modules.dashboard.domain.repository.DashboardRepository;
import org.afdb.aikp.modules.person.infrastructure.persistence.repository.PersonJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class DashboardRepositoryImpl
        implements DashboardRepository {

    private final CampaignJpaRepository campaignJpaRepository;

    private final CountryJpaRepository countryJpaRepository;

    private final DataCollectionJpaRepository
            dataCollectionJpaRepository;

    private final PersonJpaRepository personJpaRepository;

    public DashboardRepositoryImpl(
            CampaignJpaRepository campaignJpaRepository,
            CountryJpaRepository countryJpaRepository,
            DataCollectionJpaRepository
                    dataCollectionJpaRepository,
            PersonJpaRepository personJpaRepository) {

        this.campaignJpaRepository =
                campaignJpaRepository;

        this.countryJpaRepository =
                countryJpaRepository;

        this.dataCollectionJpaRepository =
                dataCollectionJpaRepository;

        this.personJpaRepository =
                personJpaRepository;
    }

    @Override
    public DashboardResponse getDashboard() {

        return new DashboardResponse(
                campaignJpaRepository.count(),
                campaignJpaRepository.countByStatus(
                        CampaignStatus.ACTIVE),
                countryJpaRepository.count(),
                dataCollectionJpaRepository.count(),
                dataCollectionJpaRepository.countByStatus(
                        DataCollectionStatus.DRAFT),
                dataCollectionJpaRepository.countByStatus(
                        DataCollectionStatus.IN_PROGRESS),
                dataCollectionJpaRepository.countByStatus(
                        DataCollectionStatus.SUBMITTED),
                dataCollectionJpaRepository.countByStatus(
                        DataCollectionStatus.VALIDATED),
                dataCollectionJpaRepository.countByStatus(
                        DataCollectionStatus.REJECTED),
                personJpaRepository.count(),
                personJpaRepository.countByActiveTrue());
    }
}
