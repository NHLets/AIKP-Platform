package org.afdb.aikp.modules.questionnaire.domain.enums;

/**
 * Defines how a Questionnaire is rendered by the user interface.
 *
 * <p>
 * The render type allows the frontend to dynamically select the
 * appropriate rendering engine.
 * </p>
 */
public enum RenderType {

    /**
     * Classical form composed of individual fields.
     */
    FORM,

    /**
     * Spreadsheet-like statistical data collection grid.
     */
    SPREADSHEET,

    /**
     * Combination of forms and spreadsheet grids.
     */
    HYBRID

}