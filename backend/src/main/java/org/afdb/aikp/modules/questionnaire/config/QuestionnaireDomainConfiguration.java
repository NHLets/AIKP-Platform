package org.afdb.aikp.modules.questionnaire.config;

import org.afdb.aikp.modules.questionnaire.domain.repository.QuestionnaireRepository;
import org.afdb.aikp.modules.questionnaire.domain.service.QuestionnaireDomainService;
import org.afdb.aikp.modules.questionnaire.domain.service.QuestionnaireDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuestionnaireDomainConfiguration {

    @Bean
    public QuestionnaireDomainService questionnaireDomainService(
            QuestionnaireRepository questionnaireRepository) {

        return new QuestionnaireDomainServiceImpl(
                questionnaireRepository);
    }
}
