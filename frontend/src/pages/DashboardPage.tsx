import {
  Container,
  Grid,
  Typography,
  CircularProgress,
  Alert
} from '@mui/material';

import KpiCards from '../components/dashboard/KpiCards';
import SeverityChart from '../components/dashboard/SeverityChart';
import TrendChart from '../components/dashboard/TrendChart';
import Heatmap from '../components/dashboard/Heatmap';
import CampaignSelector from "../shared/components/CampaignSelector";
import ReferenceYearSelector from "../shared/components/ReferenceYearSelector";
import { useCampaign } from "../shared/context/CampaignContext";

import { useDashboardOverview } from '../hooks/useDashboardOverview';

export default function DashboardPage() {

  const { campaignId, referenceYear } = useCampaign();
  const { data, isLoading, error } =
    useDashboardOverview(campaignId, referenceYear);

  if (isLoading) {
    return <CircularProgress />;
  }

  if (error || !data) {
    return <Alert severity="error">Unable to load dashboard.</Alert>;
  }

  return (
    <Container maxWidth="xl" sx={{ mt: 4, mb: 4 }}>      <Grid container justifyContent="space-between" alignItems="center" mb={2}>

        <Grid item>
          <Typography variant="h4" fontWeight="bold">
            AIKP Analytics Dashboard
          </Typography>
        </Grid>

        <Grid item>
          <Grid container spacing={2}>
            <Grid item>
              <CampaignSelector />
            </Grid>
            <Grid item>
              <ReferenceYearSelector />
            </Grid>
          </Grid>
        </Grid>

      </Grid>


      <KpiCards data={data.kpiSummary} />

      <Grid container spacing={3} mt={1}>

        <Grid item xs={12} md={6}>
          <SeverityChart data={data.severityDistribution} />
        </Grid>

        <Grid item xs={12} md={6}>
          <TrendChart data={data.validationTrend} />
        </Grid>

        <Grid item xs={12}>
          <Heatmap data={data.validationHeatmap} />
        </Grid>

      </Grid>

    </Container>
  );
}
