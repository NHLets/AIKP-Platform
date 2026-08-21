export type CampaignStatus =
    | "DRAFT"
    | "PLANNED"
    | "ACTIVE"
    | "COMPLETED"
    | "ARCHIVED";

export interface Campaign {
    id: string;
    code: string;
    name: string;
    description: string;
    startDate: string;
    endDate: string;
    status: CampaignStatus;
    active: boolean;
}

export interface CampaignSummary {
    id: string;
    code: string;
    name: string;
    startDate: string;
    endDate: string;
    status: CampaignStatus;
    active: boolean;
}

export interface CreateCampaignRequest {
    code: string;
    name: string;
    description: string;
    startDate: string;
    endDate: string;
}

export interface UpdateCampaignRequest {
    name: string;
    description: string;
    startDate: string;
    endDate: string;
}