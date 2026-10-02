import { useEffect, useMemo, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";

import {
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
  Tab,
  Tabs,
  Typography,
} from "@mui/material";

import ValidationDashboard from "@/modules/validation/components/ValidationDashboard";
import ReviewPanel from "@/modules/validation/components/ReviewPanel";

import DataEntryForm from "../components/DataEntryForm";
import SpreadsheetDataEntryForm from "../components/SpreadsheetDataEntryForm";

import {
  getDataCollectionById,
  submitDataCollection,
  validateDataCollection,
  rejectDataCollection,
} from "../api/dataCollectionApi";

import { getQuestionnaire } from "@/modules/questionnaire/api/questionnaireApi";
import { getQuestionnaireGroups } from "@/modules/questionnaire/api/questionnaireGroupApi";
import { getQuestionnaireVariables } from "@/modules/questionnaire/api/questionnaireVariableApi";

import type { DataCollection } from "../types/dataCollection.types";
import type { QuestionnaireGroup } from "@/modules/questionnaire/types/questionnaireGroup.types";
import type { QuestionnaireVariable } from "@/modules/questionnaire/types/questionnaireVariable.types";

type PendingAction = "SUBMIT" | "APPROVE" | "REJECT" | null;

export default function DataCollectionDetailPage() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [tab, setTab] = useState(0);

  const [dataCollection, setDataCollection] =
    useState<DataCollection | null>(null);

  const [groups, setGroups] = useState<QuestionnaireGroup[]>([]);
  const [variables, setVariables] = useState<QuestionnaireVariable[]>([]);

  const [loading, setLoading] = useState(true);
  const [structureLoading, setStructureLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const [pendingAction, setPendingAction] =
    useState<PendingAction>(null);

  const [selectedObservationId] = useState<string>();

  useEffect(() => {
    async function load() {
      if (!id) return;

      try {
        setLoading(true);

        const dc = await getDataCollectionById(id);
        setDataCollection(dc);

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
          sx={{ mb: 3 }}
        >
          {dataCollection.questionnaireId}
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
                    {dataCollection.campaignId}
                  </Typography>
                </Grid>

                <Grid size={{ xs: 12, md: 6 }}>
                  <Typography variant="caption">
                    Country
                  </Typography>
                  <Typography>
                    {dataCollection.countryId}
                  </Typography>
                </Grid>

                <Grid size={{ xs: 12, md: 6 }}>
                  <Typography variant="caption">
                    Questionnaire
                  </Typography>
                  <Typography>
                    {dataCollection.questionnaireId}
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
                <Button
                  variant="contained"
                  onClick={() =>
                    setPendingAction("SUBMIT")
                  }
                >
                  Submit
                </Button>

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
              </Stack>
            </Stack>
          </CardContent>
        </Card>

        <ValidationDashboard
          dataCollectionId={dataCollection.id}
        />

        <Grid
          container
          spacing={3}
          sx={{ mt: 2 }}
        >
          <Grid size={{ xs: 12, lg: 8 }}>
            <Tabs
              value={tab}
              onChange={(_, value) => setTab(value)}
              sx={{ mb: 2 }}
            >
              <Tab label="Questionnaire" />
              <Tab label="Spreadsheet" />
            </Tabs>

            {structureLoading ? (
              <CircularProgress />
            ) : tab === 0 ? (
              <DataEntryForm
                dataCollectionId={dataCollection.id}
                groups={groups}
                variables={variables}
              />
            ) : (
              <SpreadsheetDataEntryForm
                dataCollectionId={dataCollection.id}
                groups={groups}
                variables={variables}
                status={dataCollection.status}
              />
            )}
          </Grid>

          <Grid size={{ xs: 12, lg: 4 }}>
            <ReviewPanel
              observationId={selectedObservationId}
            />
          </Grid>
        </Grid>
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
