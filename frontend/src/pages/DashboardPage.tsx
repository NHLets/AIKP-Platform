import {
  Container,
  Box,
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
    <Container maxWidth="xl" sx={{ mt: 4, mb: 4 }}>      <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "center", mb: 2 }}>

        <Box>
          <Typography variant="h4" sx={{ fontWeight: "bold" }}>
            AIKP Analytics Dashboard
          </Typography>
        </Box>

        <Box>
          <Box sx={{ display: "grid", gridTemplateColumns: "repeat(2, minmax(0, 1fr))", gap: 2 }}>
            <Box>
              <CampaignSelector />
            </Box>
            <Box>
              <ReferenceYearSelector />
            </Box>
          </Box>
        </Box>

      </Box>


      <KpiCards data={data.kpiSummary} />

      <Box sx={{ display: "grid", gridTemplateColumns: { xs: "1fr", md: "repeat(2, minmax(0, 1fr))" }, gap: 3, mt: 1 }}>

        <Box sx={{ minWidth: 0 }}>
          <SeverityChart data={data.severityDistribution} />
        </Box>

        <Box sx={{ minWidth: 0 }}>
          <TrendChart data={data.validationTrend} />
        </Box>

        <Box sx={{ minWidth: 0, gridColumn: "1 / -1" }}>
          <Heatmap data={data.validationHeatmap} />
        </Box>

      </Box>

    </Container>
  );
}
