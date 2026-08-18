package org.afdb.aikp.modules.organization.application.service;

import org.afdb.aikp.modules.country.domain.valueobject.CountryId;
import org.afdb.aikp.modules.organization.application.command.ActivateOrganizationCommand;
import org.afdb.aikp.modules.organization.application.command.CreateOrganizationCommand;
import org.afdb.aikp.modules.organization.application.command.DeactivateOrganizationCommand;
import org.afdb.aikp.modules.organization.application.command.DeleteOrganizationCommand;
import org.afdb.aikp.modules.organization.application.command.UpdateOrganizationCommand;
import org.afdb.aikp.modules.organization.application.mapper.OrganizationApplicationMapper;
import org.afdb.aikp.modules.organization.application.query.GetActiveOrganizationsQuery;
import org.afdb.aikp.modules.organization.application.query.GetOrganizationQuery;
import org.afdb.aikp.modules.organization.application.query.GetOrganizationsByCountryQuery;
import org.afdb.aikp.modules.organization.application.query.GetOrganizationsByTypeQuery;
import org.afdb.aikp.modules.organization.application.query.GetOrganizationsQuery;
import org.afdb.aikp.modules.organization.application.response.OrganizationResponse;
import org.afdb.aikp.modules.organization.application.response.OrganizationSummary;
import org.afdb.aikp.modules.organization.domain.exception.OrganizationNotFoundException;
import org.afdb.aikp.modules.organization.domain.model.Organization;
import org.afdb.aikp.modules.organization.domain.repository.OrganizationRepository;
import org.afdb.aikp.modules.organization.domain.service.OrganizationDomainService;
import org.afdb.aikp.modules.organization.domain.valueobject.OrganizationCode;
import org.afdb.aikp.modules.organization.domain.valueobject.OrganizationId;
import org.afdb.aikp.modules.organization.domain.valueobject.OrganizationName;
import org.afdb.aikp.modules.organization.domain.valueobject.OrganizationType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OrganizationApplicationService {

    private final OrganizationRepository repository;

    private final OrganizationDomainService domainService;

    public OrganizationApplicationService(
            OrganizationRepository repository,
            OrganizationDomainService domainService) {

        this.repository = repository;
        this.domainService = domainService;
    }

    @Transactional(readOnly = true)
    public OrganizationResponse get(
            GetOrganizationQuery query) {

        OrganizationId id =
                OrganizationId.of(query.id());

        Organization organization =
                repository.findById(id)
                        .orElseThrow(
                                () -> new OrganizationNotFoundException(id));

        return OrganizationApplicationMapper.toResponse(
                organization);
    }

    @Transactional(readOnly = true)
    public List<OrganizationSummary> getAll(
            GetOrganizationsQuery query) {

        return repository.findAll()
                .stream()
                .map(OrganizationApplicationMapper::toSummary)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<OrganizationSummary> getActive(
            GetActiveOrganizationsQuery query) {

        return repository.findActive()
                .stream()
                .map(OrganizationApplicationMapper::toSummary)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<OrganizationSummary> getByCountry(
            GetOrganizationsByCountryQuery query) {

        CountryId countryId =
                CountryId.of(query.countryId());

        return repository.findByCountryId(countryId)
                .stream()
                .map(OrganizationApplicationMapper::toSummary)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<OrganizationSummary> getByType(
            GetOrganizationsByTypeQuery query) {

        OrganizationType type =
                OrganizationType.valueOf(query.type());

        return repository.findByType(type)
                .stream()
                .map(OrganizationApplicationMapper::toSummary)
                .toList();
    }

    public OrganizationResponse create(
            CreateOrganizationCommand command) {

        OrganizationCode code =
                OrganizationCode.of(command.code());

        OrganizationName name =
                OrganizationName.of(command.name());

        OrganizationType type =
                OrganizationType.valueOf(command.type());

        CountryId countryId =
                CountryId.of(command.countryId());

        domainService.validateCreation(code);

        Organization organization =
                Organization.create(
                        OrganizationId.generate(),
                        code,
                        name,
                        type,
                        countryId);

        Organization saved =
                repository.save(organization);

        return OrganizationApplicationMapper.toResponse(
                saved);
    }

    public OrganizationResponse update(
            UpdateOrganizationCommand command) {

        OrganizationId id =
                OrganizationId.of(command.id());

        Organization organization =
                repository.findById(id)
                        .orElseThrow(
                                () -> new OrganizationNotFoundException(id));

        OrganizationCode code =
                OrganizationCode.of(command.code());

        OrganizationName name =
                OrganizationName.of(command.name());

        OrganizationType type =
                OrganizationType.valueOf(command.type());

        CountryId countryId =
                CountryId.of(command.countryId());

        domainService.validateUpdate(id, code);

        organization.changeCode(code);
        organization.rename(name);
        organization.changeType(type);
        organization.changeCountry(countryId);

        Organization saved =
                repository.save(organization);

        return OrganizationApplicationMapper.toResponse(
                saved);
    }

    public OrganizationResponse activate(
            ActivateOrganizationCommand command) {

        OrganizationId id =
                OrganizationId.of(command.id());

        Organization organization =
                repository.findById(id)
                        .orElseThrow(
                                () -> new OrganizationNotFoundException(id));

        organization.activate();

        Organization saved =
                repository.save(organization);

        return OrganizationApplicationMapper.toResponse(
                saved);
    }

    public OrganizationResponse deactivate(
            DeactivateOrganizationCommand command) {

        OrganizationId id =
                OrganizationId.of(command.id());

        Organization organization =
                repository.findById(id)
                        .orElseThrow(
                                () -> new OrganizationNotFoundException(id));

        organization.deactivate();

        Organization saved =
                repository.save(organization);

        return OrganizationApplicationMapper.toResponse(
                saved);
    }

    public void delete(
            DeleteOrganizationCommand command) {

        OrganizationId id =
                OrganizationId.of(command.id());

        Organization organization =
                repository.findById(id)
                        .orElseThrow(
                                () -> new OrganizationNotFoundException(id));

        repository.delete(organization);
    }
}