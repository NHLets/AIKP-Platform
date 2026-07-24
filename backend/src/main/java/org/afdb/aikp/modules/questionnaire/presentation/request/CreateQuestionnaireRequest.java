package org.afdb.aikp.modules.questionnaire.presentation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.afdb.aikp.modules.questionnaire.domain.enums.RenderType;

/**
 * HTTP request used to create a Questionnaire.
 */
public record CreateQuestionnaireRequest(

        @NotBlank
        @Pattern(regexp = "[A-Z0-9_]+")
        @Size(max = 50)
        String code,

        @NotBlank
        @Size(max = 150)
        String name,

        @NotBlank
        @Size(max = 1000)
        String description,

        @NotBlank
        @Pattern(regexp = "\\d+(\\.\\d+)*")
        @Size(max = 20)
        String version,

        @NotBlank
        @Pattern(regexp = "[a-z]{2}")
        String defaultLanguage,

        @NotNull
        RenderType renderType

) {
}