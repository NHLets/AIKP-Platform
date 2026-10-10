import { QUALITY_COLLECTION_CODES, type QualityAssessment, type QualityCollectionCode, type QualityLevel } from "../types/quality.types";

const rank: Record<QualityLevel, number> = {
  VERY_GOOD: 0, GOOD: 1, FAIR: 2, WEAK: 3, POOR: 4,
};
export const QUALITY_LEVEL_LABELS: Record<QualityLevel, string> = {
  VERY_GOOD: "Very good", GOOD: "Good", FAIR: "Fair", WEAK: "Weak", POOR: "Poor",
};
export interface CountryQualityInput {
  code: QualityCollectionCode;
  assessment?: QualityAssessment;
  ambiguous?: boolean;
}
export interface CountryQualitySummary {
  level: QualityLevel | null;
  complete: boolean;
  missingCodes: QualityCollectionCode[];
  incompleteCodes: QualityCollectionCode[];
  ambiguousCodes: QualityCollectionCode[];
}
export function summarizeCountryQuality(inputs: CountryQualityInput[]): CountryQualitySummary {
  const byCode = new Map(inputs.map((item) => [item.code, item]));
  const missingCodes: QualityCollectionCode[] = [];
  const incompleteCodes: QualityCollectionCode[] = [];
  const ambiguousCodes: QualityCollectionCode[] = [];
  const levels: QualityLevel[] = [];
  for (const code of QUALITY_COLLECTION_CODES) {
    const item = byCode.get(code);
    if (!item || (!item.assessment && !item.ambiguous)) {
      missingCodes.push(code);
      continue;
    }
    if (item.ambiguous) {
      ambiguousCodes.push(code);
      continue;
    }
    const assessment = item.assessment!;
    if (
      !assessment.analysisComplete ||
      !assessment.overallLevel
    ) {
      incompleteCodes.push(code);
      continue;
    }
    levels.push(assessment.overallLevel);
  }
  const complete = missingCodes.length === 0 && incompleteCodes.length === 0 &&
    ambiguousCodes.length === 0 && levels.length === QUALITY_COLLECTION_CODES.length;
  return {
    level: complete ? levels.reduce((worst, current) =>
      rank[current] > rank[worst] ? current : worst, levels[0]!) : null,
    complete, missingCodes, incompleteCodes, ambiguousCodes,
  };
}
