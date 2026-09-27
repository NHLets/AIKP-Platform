export interface CampaignSummary {
  id: number;
  code: string;
  name: string;
  referenceYear: number;
  status: "DRAFT" | "PLANNED" | "ACTIVE" | "COMPLETED" | "ARCHIVED";
}
