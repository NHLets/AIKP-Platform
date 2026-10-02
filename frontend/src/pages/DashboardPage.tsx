import { Container, Typography, Stack } from "@mui/material";

import CampaignSelector from "../shared/components/CampaignSelector";
import ReferenceYearSelector from "../shared/components/ReferenceYearSelector";
import { useCampaign } from "../shared/context/CampaignContext";

export default function DashboardPage() {
  const { campaignId, referenceYear } = useCampaign();

  return (
    <Container maxWidth="xl" sx={{ mt: 4 }}>
      <Typography variant="h4" sx={{ fontWeight: "bold" }} gutterBottom>
        AIKP Analytics Dashboard
      </Typography>

      <Stack direction="row" spacing={2} sx={{ mb: 4 }}>
        <CampaignSelector />
        <ReferenceYearSelector />
      </Stack>

      <Typography variant="body1">
        Campaign ID: {campaignId ?? "NULL"}
      </Typography>

      <Typography variant="body1">
        Reference Year: {referenceYear ?? "NULL"}
      </Typography>
    </Container>
  );
}
