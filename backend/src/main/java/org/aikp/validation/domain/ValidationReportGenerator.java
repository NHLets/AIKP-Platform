package org.aikp.validation.domain;

import java.util.List;

public class ValidationReportGenerator {

    private final SeverityClassifier classifier =
            new SeverityClassifier();

    public ValidationReport generate(

            String questionnaire,

            List<ValidationIssue> issues){

        ValidationSeverity overallSeverity =
                classifier.classify(

                    issues.stream()

                        .map(i->new ValidationResult(

                                false,

                                i.message(),

                                i.severity()))

                        .toList()

                );

        return new ValidationReport(

                questionnaire,

                issues,

                severity);

    }

}