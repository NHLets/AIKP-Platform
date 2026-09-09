package org.afdb.aikp.modules.questionnaire.domain.enums;

/**
 * Defines the structural role of a questionnaire group.
 *
 * <p>
 * Groups form a hierarchy within a questionnaire. The same model can
 * represent structures such as Dimension -> Policy Group -> Variable,
 * or simpler structures used by other questionnaires.
 * </p>
 */
public enum QuestionnaireGroupType {

    /**
     * High-level thematic dimension.
     */
    DIMENSION,

    /**
     * Group of variables related to a policy area.
     */
    POLICY_GROUP,

    /**
     * Generic questionnaire section.
     */
    SECTION,

    /**
     * Generic category.
     */
    CATEGORY,

    /**
     * Generic grouping level.
     */
    GROUP

}
