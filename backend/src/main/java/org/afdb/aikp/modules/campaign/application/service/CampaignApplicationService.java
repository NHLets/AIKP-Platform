package org.afdb.aikp.modules.campaign.application.service;

import org.afdb.aikp.modules.campaign.application.command.ActivateCampaignCommand;
import org.afdb.aikp.modules.campaign.application.command.ArchiveCampaignCommand;
import org.afdb.aikp.modules.campaign.application.command.CompleteCampaignCommand;
import org.afdb.aikp.modules.campaign.application.command.CreateCampaignCommand;
import org.afdb.aikp.modules.campaign.application.command.DeleteCampaignCommand;
import org.afdb.aikp.modules.campaign.application.command.PlanCampaignCommand;
import org.afdb.aikp.modules.campaign.application.command.UpdateCampaignCommand;
import org.afdb.aikp.modules.campaign.application.mapper.CampaignApplicationMapper;
import org.afdb.aikp.modules.campaign.application.query.GetCampaignQuery;
import org.afdb.aikp.modules.campaign.application.response.CampaignResponse;
import org.afdb.aikp.modules.campaign.domain.exception.CampaignCodeAlreadyExistsException;
import org.afdb.aikp.modules.campaign.domain.exception.CampaignNotFoundException;
import org.afdb.aikp.modules.campaign.domain.model.Campaign;
import org.afdb.aikp.modules.campaign.domain.repository.CampaignRepository;
import org.afdb.aikp.modules.campaign.domain.valueobject.CampaignCode;
import org.afdb.aikp.modules.campaign.domain.valueobject.CampaignDescription;
import org.afdb.aikp.modules.campaign.domain.valueobject.CampaignId;
import org.afdb.aikp.modules.campaign.domain.valueobject.CampaignName;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.afdb.aikp.modules.campaign.application.query.GetCampaignsQuery;
import org.afdb.aikp.modules.campaign.application.response.CampaignSummary;

import java.util.List;
@Service
@Transactional
public class CampaignApplicationService {

    private final CampaignRepository repository;

    public CampaignApplicationService(
            CampaignRepository repository) {

        this.repository = repository;
    }

    public CampaignResponse create(
            CreateCampaignCommand command) {

        CampaignCode code =
                CampaignCode.of(command.code());

        if (repository.existsByCode(code)) {
            throw new CampaignCodeAlreadyExistsException(
                "Campaign code already exists: " + code);
        }

        Campaign campaign = Campaign.create(
                code,
                CampaignName.of(command.name()),
                CampaignDescription.of(command.description()),
                command.startDate(),
                command.endDate());

        return CampaignApplicationMapper.toResponse(
                repository.save(campaign));
    }

    @Transactional(readOnly = true)
    public CampaignResponse getById(
            GetCampaignQuery query) {

        return CampaignApplicationMapper.toResponse(
                findByIdOrThrow(query.id()));
    }

    @Transactional(readOnly = true)
    public CampaignResponse getByCode(
            String code) {

        Campaign campaign = repository.findByCode(
                        CampaignCode.of(code))
                .orElseThrow(() ->
                        new CampaignNotFoundException(
        "Campaign not found with code: " + code));

        return CampaignApplicationMapper.toResponse(campaign);
    }

    @Transactional(readOnly = true)
    public List<CampaignSummary> getAll(
        GetCampaignsQuery query) {

    return repository
            .findAll()
            .stream()
            .map(campaign -> new CampaignSummary(
                    campaign.getId().getValue(),
                    campaign.getCode().getValue(),
                    campaign.getName().getValue(),
                    campaign.getStartDate(),
                    campaign.getEndDate(),
                    campaign.getStatus(),
                    campaign.isActive()
            ))
            .toList();
        }
    public CampaignResponse update(
            UpdateCampaignCommand command) {

        Campaign campaign = findByIdOrThrow(command.id());

        campaign.rename(
                CampaignName.of(command.name()));

        campaign.changeDescription(
                CampaignDescription.of(command.description()));

        campaign.changePeriod(
                command.startDate(),
                command.endDate());

        return CampaignApplicationMapper.toResponse(
                repository.save(campaign));
    }

    public CampaignResponse plan(
            PlanCampaignCommand command) {

        Campaign campaign = findByIdOrThrow(command.id());

        campaign.plan();

        return CampaignApplicationMapper.toResponse(
                repository.save(campaign));
    }

    public CampaignResponse activate(
            ActivateCampaignCommand command) {

        Campaign campaign = findByIdOrThrow(command.id());

        campaign.activate();

        return CampaignApplicationMapper.toResponse(
                repository.save(campaign));
    }

    public CampaignResponse complete(
            CompleteCampaignCommand command) {

        Campaign campaign = findByIdOrThrow(command.id());

        campaign.complete();

        return CampaignApplicationMapper.toResponse(
                repository.save(campaign));
    }

    public CampaignResponse archive(
            ArchiveCampaignCommand command) {

        Campaign campaign = findByIdOrThrow(command.id());

        campaign.archive();

        return CampaignApplicationMapper.toResponse(
                repository.save(campaign));
    }

    public void delete(
            DeleteCampaignCommand command) {

        Campaign campaign = findByIdOrThrow(command.id());

        repository.delete(campaign);
    }

    private Campaign findByIdOrThrow(
            java.util.UUID id) {

        return repository.findById(
                        CampaignId.of(id))
                .orElseThrow(() ->
                        new CampaignNotFoundException(
        "Campaign not found with id: " + id));
    }
}
