import { useEffect, useMemo, useState } from "react";

import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import { useNavigate, useParams } from "react-router-dom";

import {
  Accordion,
  AccordionDetails,
  AccordionSummary,
  Alert,
  Box,
  Button,
  Card,
  CardContent,
  Chip,
  CircularProgress,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
  Grid,
  Stack,
  Typography,
} from "@mui/material";

import ValidationDashboard from "@/modules/validation/components/ValidationDashboard";
import ReviewPanel from "@/modules/validation/components/ReviewPanel";

import SpreadsheetDataEntryForm from "../components/SpreadsheetDataEntryForm";

import {
  getDataCollectionById,
  submitDataCollection,
  validateDataCollection,
  rejectDataCollection,
} from "../api/dataCollectionApi";

import { startDataCollection } from "../api/workflowApi";

import { getQuestionnaire } from "@/modules/questionnaire/api/questionnaireApi";

import {
  getCampaigns,
  getCountries,
  getQuestionnaires,
  getOrganizations,
} from "../api/dataCollectionOptionsApi";

import type {
  CampaignOption,
  CountryOption,
  QuestionnaireOption,
  OrganizationOption,
} from "../api/dataCollectionOptionsApi";
import { getQuestionnaireGroups } from "@/modules/questionnaire/api/questionnaireGroupApi";
import { getQuestionnaireVariables } from "@/modules/questionnaire/api/questionnaireVariableApi";

import type { DataCollection } from "../types/dataCollection.types";
import type { QuestionnaireGroup } from "@/modules/questionnaire/types/questionnaireGroup.types";
import type { QuestionnaireVariable } from "@/modules/questionnaire/types/questionnaireVariable.types";

type PendingAction =
  | "START"
  | "SUBMIT"
  | "APPROVE"
  | "REJECT"
  | null;

export default function DataCollectionDetailPage() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [dataCollection, setDataCollection] =
    useState<DataCollection | null>(null);

  const [groups, setGroups] = useState<QuestionnaireGroup[]>([]);
  const [variables, setVariables] = useState<QuestionnaireVariable[]>([]);

  const [campaigns, setCampaigns] =
    useState<CampaignOption[]>([]);

  const [countries, setCountries] =
    useState<CountryOption[]>([]);

  const [questionnaires, setQuestionnaires] =
    useState<QuestionnaireOption[]>([]);

  const [organizations, setOrganizations] =
    useState<OrganizationOption[]>([]);

  const [loading, setLoading] = useState(true);
  const [structureLoading, setStructureLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const [pendingAction, setPendingAction] =
    useState<PendingAction>(null);

  const [selectedObservationId, setSelectedObservationId] =
    useState<string | undefined>();

  const [reviewOpen, setReviewOpen] = useState(false);

  useEffect(() => {
    async function load() {
      if (!id) return;

      try {
        setLoading(true);

        const dc = await getDataCollectionById(id);
        setDataCollection(dc);

        const [
          campaignData,
          countryData,
          questionnaireData,
          organizationData,
        ] = await Promise.all([
          getCampaigns(),
          getCountries(),
          getQuestionnaires(),
          getOrganizations(),
        ]);

        setCampaigns(campaignData);
        setCountries(countryData);
        setQuestionnaires(questionnaireData);
        setOrganizations(organizationData);

        setStructureLoading(true);

        await getQuestionnaire(dc.questionnaireId);

        const [g, v] = await Promise.all([
          getQuestionnaireGroups(dc.questionnaireId),
          getQuestionnaireVariables(dc.questionnaireId),
        ]);

        setGroups(g);
        setVariables(v);
      } catch {
        setError("Unable to load Data Collection.");
      } finally {
        setStructureLoading(false);
        setLoading(false);
      }
    }

    void load();
  }, [id]);

  const statusColor = useMemo(() => {
    switch (dataCollection?.status) {
      case "VALIDATED":
        return "success";
      case "REJECTED":
        return "error";
      case "SUBMITTED":
        return "warning";
      default:
        return "default";
    }
  }, [dataCollection]);

  async function handleConfirm() {
    if (!dataCollection || !pendingAction) return;

    switch (pendingAction) {
      case "START":
        await startDataCollection(dataCollection.id);
        break;

      case "SUBMIT":
        await submitDataCollection(dataCollection.id);
        break;

      case "APPROVE":
        await validateDataCollection(dataCollection.id);
        break;

      case "REJECT":
        await rejectDataCollection(dataCollection.id);
        break;
    }

    const refreshed =
      await getDataCollectionById(dataCollection.id);

    setDataCollection(refreshed);
    setPendingAction(null);
  }

  if (loading) {
    return (
      <Box
        sx={{
          display: "flex",
          justifyContent: "center",
          py: 8,
        }}
      >
        <CircularProgress />
      </Box>
    );
  }

  if (error || !dataCollection) {
    return (
      <Alert severity="error">
        {error ?? "Data Collection not found."}
      </Alert>
    );
  }


  return (
    <>
      <Box>
        <Button
          onClick={() => navigate(-1)}
          sx={{ mb: 3 }}
        >
          Back
        </Button>

        <Typography
          variant="h4"
          sx={{ fontWeight: 700 }}
        >
          Data Collection Details
        </Typography>

        <Typography
          color="text.secondary"
          sx={{ mb: 1 }}
        >
          {questionnaires.find(
            (item) => item.id === dataCollection.questionnaireId,
          )?.name ?? dataCollection.questionnaireId}
        </Typography>

        <Card sx={{ mb: 3 }}>
          <CardContent>
            <Stack spacing={2}>
              <Grid container spacing={2}>
                <Grid size={{ xs: 12, md: 6 }}>
                  <Typography variant="caption">
                    Campaign
                  </Typography>
                  <Typography>
                    {campaigns.find(
                      (item) => item.id === dataCollection.campaignId,
                    )?.code
                      ? `${campaigns.find(
                          (item) => item.id === dataCollection.campaignId,
                        )?.code} — ${campaigns.find(
                          (item) => item.id === dataCollection.campaignId,
                        )?.name}`
                      : dataCollection.campaignId}
                  </Typography>
                </Grid>

                <Grid size={{ xs: 12, md: 6 }}>
                  <Typography variant="caption">
                    Country
                  </Typography>
                  <Typography>
                    {countries.find(
                      (item) => item.id === dataCollection.countryId,
                    )?.name ?? dataCollection.countryId}
                  </Typography>
                </Grid>

                <Grid size={{ xs: 12, md: 6 }}>
                  <Typography variant="caption">
                    Operator Data Provider
                  </Typography>
                  <Typography>
                    {organizations.find(
                      (item) =>
                        item.id === dataCollection.operatorOrganizationId,
                    )?.name ?? dataCollection.operatorOrganizationId}
                  </Typography>
                </Grid>

                <Grid size={{ xs: 12, md: 6 }}>
                  <Typography variant="caption">
                    Status
                  </Typography>
                  <Box sx={{ mt: 0.5 }}>
                    <Chip
                      label={dataCollection.status}
                      color={statusColor}
                    />
                  </Box>
                </Grid>
              </Grid>

              <Stack
                direction="row"
                spacing={1}
              >
                {(dataCollection.status === "DRAFT" ||
                  dataCollection.status === "REJECTED") && (
                  <Button
                    variant="contained"
                    onClick={() =>
                      setPendingAction("START")
                    }
                  >
                    Start
                  </Button>
                )}

                {dataCollection.status === "IN_PROGRESS" && (
                  <Button
                    variant="contained"
                    onClick={() =>
                      setPendingAction("SUBMIT")
                    }
                  >
                    Submit
                  </Button>
                )}

                {dataCollection.status === "SUBMITTED" && (
                  <>
                    <Button
                      color="success"
                      variant="contained"
                      onClick={() =>
                        setPendingAction("APPROVE")
                      }
                    >
                      Validate
                    </Button>

                    <Button
                      color="error"
                      variant="contained"
                      onClick={() =>
                        setPendingAction("REJECT")
                      }
                    >
                      Reject
                    </Button>
                  </>
                )}
              </Stack>
            </Stack>
          </CardContent>
        </Card>

        <ValidationDashboard
          dataCollectionId={dataCollection.id}
        />

        <Box sx={{ mt: 2 }}>
          {structureLoading ? (
            <CircularProgress />
          ) : (
            <SpreadsheetDataEntryForm
              dataCollectionId={dataCollection.id}
              groups={groups}
              variables={variables}
              status={dataCollection.status}
              onObservationSelect={(observationId) => {
                setSelectedObservationId(observationId);
                setReviewOpen(Boolean(observationId));
              }}
            />
          )}

          <Accordion
            expanded={reviewOpen}
            onChange={(_, expanded) => setReviewOpen(expanded)}
            sx={{ mt: 3 }}
          >
            <AccordionSummary
              expandIcon={<ExpandMoreIcon />}
            >
              <Typography variant="h6">
                Validation Review
              </Typography>
            </AccordionSummary>

            <AccordionDetails>
              <ReviewPanel
                observationId={selectedObservationId}
              />
            </AccordionDetails>
          </Accordion>
        </Box>
      </Box>

      <Dialog
        open={pendingAction !== null}
        onClose={() => setPendingAction(null)}
      >
        <DialogTitle>
          Confirm action
        </DialogTitle>

        <DialogContent>
          <DialogContentText>
            Are you sure you want to continue?
          </DialogContentText>
        </DialogContent>

        <DialogActions>
          <Button
            onClick={() =>
              setPendingAction(null)
            }
          >
            Cancel
          </Button>

          <Button
            variant="contained"
            onClick={() => void handleConfirm()}
          >
            Confirm
          </Button>
        </DialogActions>
      </Dialog>
    </>
  );
}
