export type DataCollectionStatus =
    | "DRAFT"
    | "IN_PROGRESS"
    | "SUBMITTED"
    | "VALIDATED"
    | "REJECTED"
    | "CANCELLED";

export interface DataCollection {
    id: string;
    campaignId: string;
    countryId: string;
    questionnaireId: string;
    responsibleOrganizationId: string;
    operatorOrganizationId: string;
    dataCollectorId: string;
    status: DataCollectionStatus;
}

export interface DataCollectionSummary {
    id: string;
    campaignId: string;
    countryId: string;
    questionnaireId: string;
    responsibleOrganizationId: string;
    operatorOrganizationId: string;
    dataCollectorId: string;
    status: DataCollectionStatus;
}
