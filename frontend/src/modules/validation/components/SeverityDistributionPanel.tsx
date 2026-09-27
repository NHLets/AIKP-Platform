import {
  Chip,
  LinearProgress,
  Paper,
  Stack,
  Typography,
} from "@mui/material";

import { useValidationSeverity } from "../hooks/useValidationSeverity";

interface Props {
  dataCollectionId: string;
}

const COLORS = {
  INFO: "#2563EB",
  WARNING: "#D97706",
  ERROR: "#DC2626",
};

export default function SeverityDistributionPanel({
  dataCollectionId,
}: Props) {

  const { data = [] } =
    useValidationSeverity(dataCollectionId);

  const total = data.reduce((s, i) => s + i.count, 0);

  return (
    <Paper sx={{ p: 2 }}>
      <Stack spacing={2}>
        <Typography variant="h6">
          Validation Severity
        </Typography>

        {data.map((item) => {
          const pct = total === 0
            ? 0
            : (item.count * 100) / total;

          return (
            <Stack
              key={item.severity}
              spacing={0.5}
            >
              <Stack
                direction="row"
                justifyContent="space-between"
                alignItems="center"
              >
                <Chip
                  label={item.severity}
                  size="small"
                  sx={{
                    backgroundColor:
                      COLORS[item.severity],
                    color: "white",
                  }}
                />

                <Typography variant="body2">
                  {item.count}
                </Typography>
              </Stack>

              <LinearProgress
                variant="determinate"
                value={pct}
                sx={{
                  height: 8,
                  borderRadius: 4,
                  "& .MuiLinearProgress-bar": {
                    backgroundColor:
                      COLORS[item.severity],
                  },
                }}
              />

              <Typography
                variant="caption"
                color="text.secondary"
              >
                {pct.toFixed(1)}%
              </Typography>
            </Stack>
          );
        })}
      </Stack>
    </Paper>
  );
}
