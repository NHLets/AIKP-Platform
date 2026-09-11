export type ObservationStatus =
    | "PROVIDED"
    | "NOT_AVAILABLE"
    | "NOT_APPLICABLE";

export interface DataCollectionObservation {
    id: string;
    dataCollectionId: string;
    questionnaireVariableId: string;
    referenceYear: number;
    status: ObservationStatus;
    numericValue: string | null;
    textValue: string | null;
    booleanValue: boolean | null;
    dateValue: string | null;
    selectedUnit: string | null;
    comment: string | null;
}

export interface CreateDataCollectionObservationRequest {
    dataCollectionId: string;
    questionnaireVariableId: string;
    referenceYear: number;
    numericValue?: string | null;
    textValue?: string | null;
    booleanValue?: boolean | null;
    dateValue?: string | null;
    selectedUnit?: string | null;
    comment?: string | null;
}

export interface UpdateDataCollectionObservationRequest {
    numericValue?: string | null;
    textValue?: string | null;
    booleanValue?: boolean | null;
    dateValue?: string | null;
    selectedUnit?: string | null;
    comment?: string | null;
}
