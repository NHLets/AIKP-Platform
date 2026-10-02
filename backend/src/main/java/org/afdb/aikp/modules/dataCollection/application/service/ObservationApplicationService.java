package org.afdb.aikp.modules.dataCollection.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

import org.afdb.aikp.modules.dataCollection.domain.model.*;
import org.afdb.aikp.modules.dataCollection.domain.repository.ObservationRepository;
import org.afdb.aikp.modules.dataCollection.application.dto.*;

@Service
public class ObservationApplicationService {

    private final ObservationRepository repository;

    public ObservationApplicationService(
            ObservationRepository repository) {
        this.repository = repository;
    }

    public ObservationResponseDto create(CreateObservationRequestDto dto){

        Observation observation = new Observation(
            ObservationId.generate(),
            dto.dataCollectionId(),
            dto.variableId(),
            dto.organizationId(),
            dto.value()
        );

        Observation saved = repository.save(observation);

        return new ObservationResponseDto(
            saved.getId().value(),
            saved.getVariableId(),
            saved.getOrganizationId(),
            saved.getValue(),
            saved.getStatus().name()
        );
    }



    @Transactional
    public void submitCollection(UUID dataCollectionId){

        List<Observation> observations =
            repository.findByDataCollection(dataCollectionId);

        observations.forEach(observation -> {

            observation.submit();

            repository.save(observation);

        });

    }

    public List<Observation> getByCollection(UUID id){
        return repository.findByDataCollection(id);
    }
}
