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


/* ============================================================
   Legacy UI types (M16–M18 compatibility)
   ============================================================ */

export interface CampaignOption {
  id: string;
  name: string;
}

export interface CountryOption {
  id: string;
  name: string;
  iso3?: string;
}

export interface QuestionnaireOption {
  id: string;
  code: string;
  name: string;
}

export interface OrganizationOption {
  id: string;
  name: string;
}

export interface PersonOption {
  id: string;
  fullName: string;
}

export type PendingAction =
  | "SUBMIT"
  | "APPROVE"
  | "REJECT"
  | null;
