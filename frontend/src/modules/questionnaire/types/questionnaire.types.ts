export type QuestionnaireStatus =
    | "DRAFT"
    | "UNDER_REVIEW"
    | "APPROVED"
    | "PUBLISHED"
    | "ARCHIVED";

export type RenderType =
    | "FORM"
    | "SPREADSHEET"
    | "HYBRID";

export interface QuestionnaireSummary {
    id: string;
    code: string;
    name: string;
    version: string;
    status: QuestionnaireStatus;
    active: boolean;
}

export interface Questionnaire {
    id: string;
    code: string;
    name: string;
    description: string;
    version: string;
    defaultLanguage: string;
    status: QuestionnaireStatus;
    renderType: RenderType;
    active: boolean;
}

export interface CreateQuestionnaireRequest {
    code: string;
    name: string;
    description: string;
    version: string;
    defaultLanguage: string;
    renderType: RenderType;
}

export type UpdateQuestionnaireRequest =
    CreateQuestionnaireRequest;
