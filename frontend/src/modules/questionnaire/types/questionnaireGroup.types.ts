export type QuestionnaireGroupType =
    | "DIMENSION"
    | "POLICY_GROUP"
    | "SECTION"
    | "CATEGORY"
    | "GROUP";

export interface QuestionnaireGroup {
    id: string;
    questionnaireId: string;
    parentGroupId: string | null;
    code: string;
    name: string;
    description: string | null;
    groupType: QuestionnaireGroupType;
    displayOrder: number;
    active: boolean;
}
