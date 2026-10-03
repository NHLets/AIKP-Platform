export interface CampaignSummary {
  id: string;
  code: string;
  name: string;
  referenceYear: number;
  status: "DRAFT" | "PLANNED" | "ACTIVE" | "COMPLETED" | "ARCHIVED";
}
