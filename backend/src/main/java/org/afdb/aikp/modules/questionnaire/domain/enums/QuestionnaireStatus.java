package org.afdb.aikp.modules.questionnaire.domain.enums;

/**
 * Represents the lifecycle status of a Questionnaire.
 *
 * <p>
 * A questionnaire evolves through a controlled lifecycle from its
 * initial creation to publication and retirement.
 * </p>
 */
public enum QuestionnaireStatus {

    /**
     * Questionnaire is being designed.
     */
    DRAFT,

    /**
     * Questionnaire has been submitted for review.
     */
    UNDER_REVIEW,

    /**
     * Questionnaire has been validated and approved.
     */
    APPROVED,

    /**
     * Questionnaire is available for data collection.
     */
    PUBLISHED,

    /**
     * Questionnaire is no longer available for use.
     */
    ARCHIVED

}