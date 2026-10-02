import { Grid, LinearProgress, Stack, Typography } from "@mui/material";
import CheckCircleIcon from "@mui/icons-material/CheckCircle";
import PendingIcon from "@mui/icons-material/Pending";
import CancelIcon from "@mui/icons-material/Cancel";
import InsightsIcon from "@mui/icons-material/Insights";

import KpiCard from "./KpiCard";
import { useValidationStatistics } from "../hooks/useValidationStatistics";
import QuestionnaireProgressPanel from "./QuestionnaireProgressPanel";
import ValidationHeatmapPanel from "./ValidationHeatmapPanel";
import ValidationAnalyticsPanel from "./ValidationAnalyticsPanel";
import SeverityDistributionPanel from "./SeverityDistributionPanel";
import RejectedObservationsTable from "./RejectedObservationsTable";

interface Props {
  dataCollectionId: string;
}

export default function ValidationDashboard({ dataCollectionId }: Props) {
  const { data } = useValidationStatistics(dataCollectionId);

  if (!data) return null;

  return (
    <Stack spacing={3}>
      <Grid container spacing={2}>
        <Grid size={{ xs: 6, md: 3 }}>
          <KpiCard
            title="Validated"
            value={data.validated}
            color="#16A34A"
            icon={<CheckCircleIcon color="success" />}
          />
        </Grid>

        <Grid size={{ xs: 6, md: 3 }}>
          <KpiCard
            title="Pending"
            value={data.pending}
            color="#D97706"
            icon={<PendingIcon sx={{ color: "#D97706" }} />}
          />
        </Grid>

        <Grid size={{ xs: 6, md: 3 }}>
          <KpiCard
            title="Rejected"
            value={data.rejected}
            color="#DC2626"
            icon={<CancelIcon color="error" />}
          />
        </Grid>

        <Grid size={{ xs: 6, md: 3 }}>
          <KpiCard
            title="Completion"
            value={`${data.completionRate}%`}
            color="#2563EB"
            icon={<InsightsIcon color="primary" />}
          />
        </Grid>
      </Grid>

      <Stack spacing={1}>
        <Typography variant="body2">Overall completion</Typography>

        <LinearProgress
          variant="determinate"
          value={data.completionRate}
          sx={{ height: 8, borderRadius: 4 }}
        />

        <Typography variant="caption" color="text.secondary">
          {`${data.validated} validated • ${data.pending} pending • ${data.rejected} rejected`}
        </Typography>
      </Stack>

      <QuestionnaireProgressPanel dataCollectionId={dataCollectionId} />

      <ValidationHeatmapPanel dataCollectionId={dataCollectionId} />

      <Grid container spacing={2}>
        <Grid size={{ xs: 12, lg: 6 }}>
          <SeverityDistributionPanel dataCollectionId={dataCollectionId} />
        </Grid>

        <Grid size={{ xs: 12, lg: 6 }}>
          <ValidationAnalyticsPanel dataCollectionId={dataCollectionId} />
        </Grid>
      </Grid>

      <RejectedObservationsTable dataCollectionId={dataCollectionId} />
    </Stack>
  );
}
