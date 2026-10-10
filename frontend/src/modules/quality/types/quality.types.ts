export const QUALITY_COLLECTION_CODES = ["PW_A", "PW_B", "PW_C", "F_G"] as const;
export type QualityCollectionCode = (typeof QUALITY_COLLECTION_CODES)[number];
export type QualityLevel = "VERY_GOOD" | "GOOD" | "FAIR" | "WEAK" | "POOR";
export type QualityDimensionStatus = "EVALUATED" | "PARTIALLY_EVALUATED" | "NOT_EVALUABLE";

export interface QualityDimensionAssessment {
  dimension: string;
  level: QualityLevel | null;
  status: QualityDimensionStatus;
  rulesEvaluated: number;
  findingsCount: number;
  affectedRuleCodes: string[];
  explanation: string;
}
export interface QualityFinding {
  id: string;
  qualityRunId: string;
  ruleCode: string;
  ruleType: string;
  provenance: string;
  severity: string;
  status: string;
  title: string;
  message: string;
  explanation: string;
  recommendation: string;
  referenceYear: number | null;
  affectedVariableCodes: string[];
  evidence: Record<string, unknown>;
}
export interface QualityAssessment {
  dataCollectionId: string;
  qualityRunId: string;
  overallLevel: QualityLevel;
  analysisComplete: boolean;
  summary: string;
  generatedAt: string;
  dimensions: QualityDimensionAssessment[];
  significantFindings: QualityFinding[];
}
export type QualityAssessmentResult =
  | { state: "loaded"; assessment: QualityAssessment }
  | { state: "missing" };
