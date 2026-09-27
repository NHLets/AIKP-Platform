export interface KpiSummary {
  totalComments: number;
  criticalComments: number;
  affectedVariables: number;
  affectedQuestionnaires: number;
}

export interface SeverityDistribution {
  severity: string;
  count: number;
}

export interface ValidationTrend {
  month: number;
  monthName: string;
  count: number;
}

export interface ValidationHeatmap {
  questionnaire: string;
  variable: string;
  count: number;
}

export interface DashboardOverview {
  kpiSummary: KpiSummary;
  severityDistribution: SeverityDistribution[];
  validationTrend: ValidationTrend[];
  validationHeatmap: ValidationHeatmap[];
}
