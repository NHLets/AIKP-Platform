package org.afdb.aikp.modules.country.application.service;

import org.afdb.aikp.modules.country.application.command.ActivateCountryCommand;
import org.afdb.aikp.modules.country.application.command.CreateCountryCommand;
import org.afdb.aikp.modules.country.application.command.DeactivateCountryCommand;
import org.afdb.aikp.modules.country.application.command.DeleteCountryCommand;
import org.afdb.aikp.modules.country.application.command.UpdateCountryCommand;
import org.afdb.aikp.modules.country.application.response.CountryResponse;
import org.afdb.aikp.modules.country.application.response.CountrySummary;
import org.afdb.aikp.modules.country.application.mapper.CountryApplicationMapper;
import org.afdb.aikp.modules.country.application.query.GetActiveCountriesQuery;
import org.afdb.aikp.modules.country.application.query.GetCountriesQuery;
import org.afdb.aikp.modules.country.application.query.GetCountryQuery;
import org.afdb.aikp.modules.country.domain.exception.CountryNotFoundException;
import org.afdb.aikp.modules.country.domain.model.Country;
import org.afdb.aikp.modules.country.domain.repository.CountryRepository;
import org.afdb.aikp.modules.country.domain.service.CountryDomainService;
import org.afdb.aikp.modules.country.domain.valueobject.CountryId;
import org.afdb.aikp.modules.country.domain.valueobject.CountryName;
import org.afdb.aikp.modules.country.domain.valueobject.Iso2Code;
import org.afdb.aikp.modules.country.domain.valueobject.Iso3Code;
import org.afdb.aikp.modules.country.domain.valueobject.NumericCode;
import org.afdb.aikp.modules.country.domain.valueobject.OfficialCountryName;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CountryApplicationService {

    private final CountryRepository repository;

    private final CountryDomainService domainService;

    public CountryApplicationService(
            CountryRepository repository,
            CountryDomainService domainService) {

        this.repository = repository;
        this.domainService = domainService;
    }

    @Transactional(readOnly = true)
    public CountryResponse get(GetCountryQuery query) {

        CountryId id = CountryId.of(query.id());

        Country country = repository.findById(id)
                .orElseThrow(() -> new CountryNotFoundException(id));

        return CountryApplicationMapper.toResponse(country);
    }

    @Transactional(readOnly = true)
    public List<CountrySummary> getAll(GetCountriesQuery query) {

        return repository.findAll()
                .stream()
                .map(CountryApplicationMapper::toSummary)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CountrySummary> getActive(GetActiveCountriesQuery query) {

        return repository.findActive()
                .stream()
                .map(CountryApplicationMapper::toSummary)
                .toList();
    }

    public CountryResponse create(CreateCountryCommand command) {

        Iso2Code iso2Code = Iso2Code.of(command.iso2Code());
        Iso3Code iso3Code = Iso3Code.of(command.iso3Code());
        NumericCode numericCode = NumericCode.of(command.numericCode());

        domainService.validateCreation(
                iso2Code,
                iso3Code,
                numericCode);

        Country country = Country.create(
                CountryId.generate(),
                iso2Code,
                iso3Code,
                numericCode,
                CountryName.of(command.name()),
                OfficialCountryName.of(command.officialName()));

        Country saved = repository.save(country);

        return CountryApplicationMapper.toResponse(saved);
    }

    public CountryResponse update(UpdateCountryCommand command) {

        CountryId id = CountryId.of(command.id());

        Country country = repository.findById(id)
                .orElseThrow(() -> new CountryNotFoundException(id));

        Iso2Code iso2Code = Iso2Code.of(command.iso2Code());
        Iso3Code iso3Code = Iso3Code.of(command.iso3Code());
        NumericCode numericCode = NumericCode.of(command.numericCode());

        domainService.validateUpdate(
                id,
                iso2Code,
                iso3Code,
                numericCode);

        country.changeCodes(
                iso2Code,
                iso3Code,
                numericCode);

        country.rename(
                CountryName.of(command.name()),
                OfficialCountryName.of(command.officialName()));

        Country saved = repository.save(country);

        return CountryApplicationMapper.toResponse(saved);
    }

    public CountryResponse activate(ActivateCountryCommand command) {

        CountryId id = CountryId.of(command.id());

        Country country = repository.findById(id)
                .orElseThrow(() -> new CountryNotFoundException(id));

        country.activate();

        Country saved = repository.save(country);

        return CountryApplicationMapper.toResponse(saved);
    }

    public CountryResponse deactivate(DeactivateCountryCommand command) {

        CountryId id = CountryId.of(command.id());

        Country country = repository.findById(id)
                .orElseThrow(() -> new CountryNotFoundException(id));

        country.deactivate();

        Country saved = repository.save(country);

        return CountryApplicationMapper.toResponse(saved);
    }

    public void delete(DeleteCountryCommand command) {

        CountryId id = CountryId.of(command.id());

        Country country = repository.findById(id)
                .orElseThrow(() -> new CountryNotFoundException(id));

        repository.delete(country);
    }

}