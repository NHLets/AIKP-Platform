export type QuestionnaireVariableDataType =
    | "NUMBER"
    | "INTEGER"
    | "DECIMAL"
    | "PERCENTAGE"
    | "TEXT"
    | "BOOLEAN"
    | "DATE";

export interface QuestionnaireVariable {
    id: string;
    questionnaireId: string;
    questionnaireGroupId: string | null;
    seriesCode: string;
    name: string;
    definition: string | null;
    dataType: QuestionnaireVariableDataType;
    unit: string | null;
    required: boolean;
    displayOrder: number;
    active: boolean;
}
