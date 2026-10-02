import {
  Paper,
  Stack,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
  Typography,
} from "@mui/material";

import { useValidationHeatmap } from "../hooks/useValidationHeatmap";

interface Props {
  dataCollectionId: string;
}

const YEARS = [2022, 2023, 2024, 2025];

function cellColor(value: number): string {
  if (value === 0) return "#F9FAFB";
  if (value === 1) return "#FEE2E2";
  if (value <= 3) return "#FCA5A5";
  if (value <= 5) return "#EF4444";
  return "#B91C1C";
}

export default function ValidationHeatmapPanel({
  dataCollectionId,
}: Props) {
  const { data = [] } = useValidationHeatmap(dataCollectionId);

  const variables = Array.from(
    new Map(data.map((d) => [d.variableCode, d.variableLabel])).entries(),
  );

  const lookup = new Map(
    data.map((d) => [
      `${d.variableCode}-${d.referenceYear}`,
      d.rejectedCount,
    ]),
  );

  return (
    <Paper sx={{ p: 2 }}>
      <Stack spacing={2}>
        <Typography variant="h6">
          Validation Heatmap
        </Typography>

        <Table size="small">
          <TableHead>
            <TableRow>
              <TableCell>Variable</TableCell>
              {YEARS.map((year) => (
                <TableCell key={year} align="center">
                  {year}
                </TableCell>
              ))}
            </TableRow>
          </TableHead>

          <TableBody>
            {variables.map(([code, label]) => (
              <TableRow key={code}>
                <TableCell>
                  <Stack spacing={0}>
                    <Typography variant="body2">
                      {code}
                    </Typography>
                    <Typography variant="caption" color="text.secondary">
                      {label}
                    </Typography>
                  </Stack>
                </TableCell>

                {YEARS.map((year) => {
                  const value =
                    lookup.get(`${code}-${year}`) ?? 0;

                  return (
                    <TableCell
                      key={year}
                      align="center"
                      sx={{
                        backgroundColor: cellColor(value),
                        color: value >= 4 ? "#FFFFFF" : "inherit",
                        fontWeight: 600,
                      }}
                    >
                      {value}
                    </TableCell>
                  );
                })}
              </TableRow>
            ))}
          </TableBody>
        </Table>

        <Stack direction="row" spacing={1} sx={{ alignItems: "center" }}>
          <Typography variant="caption">
            Low
          </Typography>

          {["#F9FAFB","#FEE2E2","#FCA5A5","#EF4444","#B91C1C"].map((c) => (
            <div
              key={c}
              style={{
                width: 20,
                height: 10,
                borderRadius: 2,
                background: c,
                border: "1px solid #E5E7EB",
              }}
            />
          ))}

          <Typography variant="caption">
            High
          </Typography>
        </Stack>
      </Stack>
    </Paper>
  );
}
