import { useEffect, useMemo, useState } from "react";
import {
  Alert, Box, Button, Card, CardContent, CircularProgress, Divider,
  FormControl, InputLabel, MenuItem, Select, Stack, Typography,
} from "@mui/material";
import type { SelectChangeEvent } from "@mui/material";
import { getQualityAssessment, runQualityAssessment } from "../api/qualityApi";
import { QUALITY_LEVEL_LABELS, summarizeCountryQuality, type CountryQualityInput } from "../domain/countryQuality";
import {
  QUALITY_COLLECTION_CODES, type QualityAssessment, type QualityCollectionCode,
  type QualityLevel, type QualityAssessmentResult,
} from "../types/quality.types";
import {
  getCampaigns, getCountries, getQuestionnaires, type CampaignOption,
  type CountryOption, type QuestionnaireOption,
} from "@/modules/data-collection/api/dataCollectionOptionsApi";
import { getDataCollections } from "@/modules/data-collection/api/dataCollectionApi";
import type { DataCollectionSummary } from "@/modules/data-collection/types/dataCollection.types";
import { useCampaign } from "@/shared/context/CampaignContext";

const levelColor: Record<QualityLevel, string> = {
  VERY_GOOD: "success.main", GOOD: "success.main", FAIR: "info.main",
  WEAK: "warning.main", POOR: "error.main",
};

function Rating({ level }: { level: QualityLevel }) {
  return <Typography component="span" sx={{ fontWeight: 700 }} color={levelColor[level]}>
    {QUALITY_LEVEL_LABELS[level]}
  </Typography>;
}

export default function CountryQualityDashboardPage() {
  const { campaignId: contextCampaignId } = useCampaign();
  const [campaigns, setCampaigns] = useState<CampaignOption[]>([]);
  const [countries, setCountries] = useState<CountryOption[]>([]);
  const [questionnaires, setQuestionnaires] = useState<QuestionnaireOption[]>([]);
  const [collections, setCollections] = useState<DataCollectionSummary[]>([]);
  const [campaignId, setCampaignId] = useState("");
  const [countryId, setCountryId] = useState("");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [results, setResults] = useState<Partial<Record<QualityCollectionCode, QualityAssessmentResult>>>({});
  const [assessmentsLoading, setAssessmentsLoading] = useState(false);
  const [runningAssessmentId, setRunningAssessmentId] = useState<string | null>(null);

  useEffect(() => {
    let active = true;
    Promise.all([getCampaigns(), getCountries(), getQuestionnaires(), getDataCollections()])
      .then(([cs, countriesResult, qs, ds]) => {
        if (!active) return;
        setCampaigns(cs); setCountries(countriesResult); setQuestionnaires(qs); setCollections(ds);
        setCampaignId((old) => old && cs.some((x) => x.id === old) ? old :
          contextCampaignId && cs.some((x) => x.id === contextCampaignId) ? contextCampaignId : cs[0]?.id ?? "");
        setCountryId((old) => old && countriesResult.some((x) => x.id === old) ? old : countriesResult[0]?.id ?? "");
      })
      .catch(() => { if (active) setError("Impossible de charger les campagnes, pays et collections."); })
      .finally(() => { if (active) setLoading(false); });
    return () => { active = false; };
  }, [contextCampaignId]);

  const questionnaireCodes = useMemo(
    () => new Map(questionnaires.map((q) => [q.id, q.code.toUpperCase()])),
    [questionnaires],
  );
  const matches = useMemo(() => {
    const result: Partial<Record<QualityCollectionCode, DataCollectionSummary[]>> = {};
    for (const code of QUALITY_COLLECTION_CODES) {
      const candidates = collections.filter((d) =>
        d.campaignId === campaignId &&
        d.countryId === countryId &&
        questionnaireCodes.get(d.questionnaireId)?.trim().toUpperCase() === code
      );
      const eligible = candidates.filter(
        (d) => d.status === "SUBMITTED" || d.status === "VALIDATED",
      );
      // Quality assessment can be run manually on SUBMITTED or VALIDATED collections.
      // Do not arbitrarily choose when several eligible collections exist.
      result[code] = eligible;
    }
    return result;
  }, [collections, questionnaireCodes, campaignId, countryId]);

  useEffect(() => {
    let active = true;
    setResults({});
    if (!campaignId || !countryId) { setAssessmentsLoading(false); return () => { active = false; }; }
    const targets = QUALITY_COLLECTION_CODES.flatMap((code) => {
      const list = matches[code] ?? [];
      return list.length === 1
        ? [{ code, id: list[0]!.id }]
        : [];
    });
    setAssessmentsLoading(true);
    Promise.all(targets.map(async ({ code, id }) => {
      try {
        const assessment = await getQualityAssessment(id);
        return [code, { state: "loaded", assessment } as const] as const;
      } catch {
        return [code, { state: "missing" } as const] as const;
      }
    })).then((entries) => {
      if (active) setResults(Object.fromEntries(entries));
    }).finally(() => { if (active) setAssessmentsLoading(false); });
    return () => { active = false; };
  }, [matches, campaignId, countryId]);

  const handleRunAssessment = async (
    code: QualityCollectionCode,
    id: string,
  ) => {
    setError("");
    setRunningAssessmentId(id);

    try {
      await runQualityAssessment(id);
      const assessment = await getQualityAssessment(id);

      setResults((current) => ({
        ...current,
        [code]: { state: "loaded", assessment },
      }));
    } catch {
      setResults((current) => ({
        ...current,
        [code]: { state: "missing" },
      }));
      setError(`Impossible d'exécuter l'évaluation qualité pour ${code}.`);
    } finally {
      setRunningAssessmentId(null);
    }
  };

  const summary = useMemo(() => {
    const inputs: CountryQualityInput[] = [];
    for (const code of QUALITY_COLLECTION_CODES) {
      const list = matches[code] ?? [];
      if (list.length > 1) {
        inputs.push({ code, ambiguous: true });
      } else if (list.length === 1) {
        const result = results[code];
        inputs.push({
          code,
          assessment: result?.state === "loaded" ? result.assessment : undefined,
        });
      }
    }
    return summarizeCountryQuality(inputs);
  }, [matches, results]);

  const country = countries.find((item) => item.id === countryId);
  const campaign = campaigns.find((item) => item.id === campaignId);

  if (loading) return <Box sx={{ display: "flex", justifyContent: "center", p: 6 }}><CircularProgress /></Box>;
  if (error) return <Alert severity="error">{error}</Alert>;

  return <Stack spacing={3}>
    <Box>
      <Typography variant="h4" sx={{ fontWeight: 700 }}>Country data quality</Typography>
      <Typography color="text.secondary" sx={{ mt: 1 }}>
        Assessment of PW_A, PW_B, PW_C and F_G for a selected country and campaign.
      </Typography>
    </Box>
    <Card><CardContent><Stack direction={{ xs: "column", md: "row" }} spacing={2}>
      <FormControl fullWidth>
        <InputLabel id="quality-campaign-label">Campaign</InputLabel>
        <Select labelId="quality-campaign-label" label="Campaign" value={campaignId}
          onChange={(event: SelectChangeEvent) => setCampaignId(event.target.value)}>
          {campaigns.map((item) => <MenuItem key={item.id} value={item.id}>{item.name} ({item.code})</MenuItem>)}
        </Select>
      </FormControl>
      <FormControl fullWidth>
        <InputLabel id="quality-country-label">Country</InputLabel>
        <Select labelId="quality-country-label" label="Country" value={countryId}
          onChange={(event: SelectChangeEvent) => setCountryId(event.target.value)}>
          {countries.map((item) => <MenuItem key={item.id} value={item.id}>{item.name} ({item.iso3Code})</MenuItem>)}
        </Select>
      </FormControl>
    </Stack></CardContent></Card>

    {!campaignId || !countryId ? <Alert severity="info">No campaign or country is available.</Alert> : <>
      <Card><CardContent>
        <Typography variant="overline" color="text.secondary">Country quality rating</Typography>
        <Typography variant="h5" sx={{ fontWeight: 700 }}>{country?.name} — {campaign?.name}</Typography>
        {assessmentsLoading ? <Stack direction="row" spacing={1.5} sx={{ mt: 2, alignItems: "center" }}>
          <CircularProgress size={20} /><Typography color="text.secondary">Loading assessments…</Typography>
        </Stack> : summary.complete && summary.level ? <Box sx={{ mt: 2 }}>
          <Typography variant="h3"><Rating level={summary.level} /></Typography>
          <Typography color="text.secondary">The country rating is the worst rating among the four collections.</Typography>
        </Box> : <Alert severity="warning" sx={{ mt: 2 }}>
          National rating unavailable: coverage is incomplete or one or more assessments are not fully evaluable.
          A definitive country rating requires four complete and usable assessments.
        </Alert>}
        {!assessmentsLoading && !summary.complete && <Stack spacing={0.5} sx={{ mt: 2 }}>
          {summary.missingCodes.length > 0 && <Typography variant="body2">Missing collection or assessment: {summary.missingCodes.join(", ")}.</Typography>}
          {summary.incompleteCodes.length > 0 && <Typography variant="body2">Incomplete analysis: {summary.incompleteCodes.join(", ")}.</Typography>}
          {summary.ambiguousCodes.length > 0 && <Typography variant="body2">Duplicate matching collections: {summary.ambiguousCodes.join(", ")}.</Typography>}
        </Stack>}
      </CardContent></Card>

      <Box><Typography variant="h6" sx={{ fontWeight: 700, mb: 1.5 }}>Collection assessments</Typography>
        <Stack spacing={2}>{QUALITY_COLLECTION_CODES.map((code) => {
          const list = matches[code] ?? [];
          const result = results[code];
          const assessment: QualityAssessment | undefined = result?.state === "loaded" ? result.assessment : undefined;
          return <Card key={code} variant="outlined"><CardContent>
            <Stack direction={{ xs: "column", sm: "row" }} spacing={1} sx={{ justifyContent: "space-between" }}>
              <Box><Typography variant="h6" sx={{ fontWeight: 700 }}>{code}</Typography>
                <Typography variant="body2" color="text.secondary">
                  {list.length === 0 ? "No collection found for this campaign and country." :
                    list.length > 1 ? `${list.length} eligible collections — ambiguous` :
                    `Collection status: ${list[0]!.status}`}
                </Typography></Box>
              <Stack
                direction={{ xs: "column", sm: "row" }}
                spacing={1}
                sx={{ alignItems: { sm: "center" } }}
              >
                {list.length === 1 && (
                  <Button
                    variant="contained"
                    size="small"
                    onClick={() => handleRunAssessment(code, list[0]!.id)}
                    disabled={runningAssessmentId === list[0]!.id}
                  >
                    {runningAssessmentId === list[0]!.id
                      ? "Running…"
                      : "Run Quality Assessment"}
                  </Button>
                )}

                {assessment ? (
                  <Rating level={assessment.overallLevel} />
                ) : list.length === 1 && result?.state === "missing" ? (
                  <Typography color="warning.main" sx={{ fontWeight: 700 }}>
                    Assessment unavailable
                  </Typography>
                ) : list.length === 1 && assessmentsLoading ? (
                  <CircularProgress size={20} />
                ) : null}
              </Stack>
            </Stack>
            {assessment && <><Divider sx={{ my: 1.5 }} />
              <Typography variant="body2">{assessment.summary}</Typography>
              <Typography variant="caption" color="text.secondary" sx={{ display: "block", mt: 1 }}>
                Evaluated: {new Date(assessment.generatedAt).toLocaleString()} · Analysis {assessment.analysisComplete ? "complete" : "incomplete"}
              </Typography>
              {assessment.dimensions.length > 0 && <Box sx={{ mt: 1.5 }}>
                <Typography variant="subtitle2">Dimensions</Typography>
                {assessment.dimensions.map((d) => <Typography key={d.dimension} variant="body2" sx={{ mt: 0.5 }}>
                  {d.dimension}: {d.level ? QUALITY_LEVEL_LABELS[d.level] : "Not rated"} · {d.status.replaceAll("_", " ").toLowerCase()} · {d.findingsCount} finding(s)
                  {d.explanation ? ` — ${d.explanation}` : ""}
                </Typography>)}
              </Box>}
              {assessment.significantFindings.length > 0 && <Box sx={{ mt: 1.5 }}>
                <Typography variant="subtitle2">Significant findings</Typography>
                {assessment.significantFindings.map((f) => <Box key={f.id} sx={{ mt: 1 }}>
                  <Typography variant="body2" sx={{ fontWeight: 700 }}>{f.title || f.ruleCode} ({f.severity})</Typography>
                  <Typography variant="body2">{f.message}</Typography>
                  {f.recommendation && <Typography variant="body2" color="text.secondary">Recommendation: {f.recommendation}</Typography>}
                </Box>)}
              </Box>}
            </>}
          </CardContent></Card>;
        })}</Stack>
      </Box>
    </>}
    <Typography variant="caption" color="text.secondary">
      Quality assessments are run manually. The country rating is only shown when all four assessments are complete and evaluable.
    </Typography>
  </Stack>;
}
