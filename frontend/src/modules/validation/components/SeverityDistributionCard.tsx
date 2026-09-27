import {
  LinearProgress,
  Paper,
  Stack,
  Typography,
} from "@mui/material";

import { useSeverityDistribution } from "../hooks/useSeverityDistribution";

interface Props {
  dataCollectionId: string;
}

const COLORS = {
  INFO: "#2563EB",
  WARNING: "#D97706",
  ERROR: "#DC2626",
};

export default function SeverityDistributionCard({
  dataCollectionId,
}: Props) {

  const { data = [] } =
    useSeverityDistribution(dataCollectionId);

  return (
    <Paper sx={{ p: 2 }}>
      <Stack spacing={2}>
        <Typography variant="h6">
          Error Severity Distribution
        </Typography>

        {data.map((item) => (
          <Stack key={item.severity} spacing={0.5}>
            <Stack
              direction="row"
              justifyContent="space-between"
            >
              <Typography variant="body2">
                {item.severity}
              </Typography>

              <Typography variant="body2">
                {item.percentage.toFixed(1)}%
              </Typography>
            </Stack>

            <LinearProgress
              variant="determinate"
              value={item.percentage}
              sx={{
                height: 8,
                borderRadius: 4,
                "& .MuiLinearProgress-bar": {
                  backgroundColor: COLORS[item.severity],
                },
              }}
            />

            <Typography
              variant="caption"
              color="text.secondary"
            >
              {`${item.count} observations`}
            </Typography>
          </Stack>
        ))}
      </Stack>
    </Paper>
  );
}
