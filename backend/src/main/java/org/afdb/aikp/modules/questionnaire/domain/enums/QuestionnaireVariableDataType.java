package org.afdb.aikp.modules.questionnaire.domain.enums;

/**
 * Defines the expected type of data collected for a questionnaire variable.
 */
public enum QuestionnaireVariableDataType {

    /**
     * Generic numeric value.
     */
    NUMBER,

    /**
     * Whole number.
     */
    INTEGER,

    /**
     * Decimal numeric value.
     */
    DECIMAL,

    /**
     * Percentage value.
     */
    PERCENTAGE,

    /**
     * Free text value.
     */
    TEXT,

    /**
     * True or false value.
     */
    BOOLEAN,

    /**
     * Date value.
     */
    DATE

}
