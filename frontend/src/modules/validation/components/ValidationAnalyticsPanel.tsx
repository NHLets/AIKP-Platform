import { Stack, Typography } from "@mui/material";

import RejectedObservationsTable from "./RejectedObservationsTable";

interface Props {
  dataCollectionId: string;
}

export default function ValidationAnalyticsPanel({
  dataCollectionId,
}: Props) {

  return (
    <Stack spacing={2}>
      <Typography variant="h6">
        Validation Analytics
      </Typography>

      <RejectedObservationsTable
        dataCollectionId={dataCollectionId}
      />
    </Stack>
  );
}
