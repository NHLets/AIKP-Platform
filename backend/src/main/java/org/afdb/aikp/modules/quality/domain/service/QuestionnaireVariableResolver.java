package org.afdb.aikp.modules.quality.domain.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import org.afdb.aikp.modules.questionnaire.domain.model.QuestionnaireVariable;

@Component
public final class QuestionnaireVariableResolver {

    public Map<String, QuestionnaireVariable> resolve(
            List<QuestionnaireVariable> variables) {

        Map<String, QuestionnaireVariable> result = new LinkedHashMap<>();

        for (QuestionnaireVariable variable : variables) {
            String code = variable.getSeriesCode().trim().toUpperCase();

            if (result.put(code, variable) != null) {
                throw new IllegalStateException(
                        "Duplicate questionnaire series code: " + code);
            }
        }

        return result;
    }
}
