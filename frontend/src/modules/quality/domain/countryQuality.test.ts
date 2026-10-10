import { describe, expect, it } from "vitest";
import { summarizeCountryQuality } from "./countryQuality";
import type { QualityAssessment, QualityCollectionCode } from "../types/quality.types";

function makeAssessment(level: QualityAssessment["overallLevel"], complete = true): QualityAssessment {
  return {
    dataCollectionId: "collection", qualityRunId: "run", overallLevel: level,
    analysisComplete: complete, summary: "Test", generatedAt: "2026-10-10T10:00:00Z",
    dimensions: [{ dimension: "COMPLETENESS", level, status: "EVALUATED",
      rulesEvaluated: 2, findingsCount: 0, affectedRuleCodes: [], explanation: "" }],
    significantFindings: [],
  };
}
function four() {
  const codes: QualityCollectionCode[] = ["PW_A", "PW_B", "PW_C", "F_G"];
  return codes.map((code, i) => ({
    code, assessment: makeAssessment((["GOOD", "VERY_GOOD", "FAIR", "POOR"] as const)[i]!),
  }));
}
describe("summarizeCountryQuality", () => {
  it("uses the worst of the four ratings", () => {
    expect(summarizeCountryQuality(four())).toMatchObject({ complete: true, level: "POOR" });
  });
  it("does not rate incomplete coverage", () => {
    expect(summarizeCountryQuality(four().filter((x) => x.code !== "F_G"))).toMatchObject({
      complete: false, level: null, missingCodes: ["F_G"],
    });
  });
  it("does not rate an incomplete analysis", () => {
    const input = four();
    input[2]!.assessment = makeAssessment("FAIR", false);
    expect(summarizeCountryQuality(input)).toMatchObject({
      complete: false, level: null, incompleteCodes: ["PW_C"],
    });
  });
  it("does not rate ambiguous matching collections", () => {
    const input = four().map((x) => x.code === "PW_B" ? { code: x.code, ambiguous: true } : x);
    expect(summarizeCountryQuality(input)).toMatchObject({
      complete: false, level: null, ambiguousCodes: ["PW_B"],
    });
  });
});
