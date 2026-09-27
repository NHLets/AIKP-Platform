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

function color(value: number) {
  if (value === 0) return "#F3F4F6";
  if (value === 1) return "#FEE2E2";
  if (value <= 3) return "#FCA5A5";
  return "#DC2626";
}

export default function ValidationHeatmap({
  dataCollectionId,
}: Props) {

  const { data = [] } =
    useValidationHeatmap(dataCollectionId);

  const variables = Array.from(
    new Map(
      data.map((d) => [d.variableCode, d]),
    ).values(),
  );

  const getValue = (
    code: string,
    year: number,
  ) =>
    data.find(
      (d) =>
        d.variableCode === code &&
        d.referenceYear === year,
    )?.rejectedCount ?? 0;

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
              {YEARS.map((y) => (
                <TableCell key={y} align="center">
                  {y}
                </TableCell>
              ))}
            </TableRow>
          </TableHead>

          <TableBody>
            {variables.map((v) => (
              <TableRow key={v.variableCode}>
                <TableCell>
                  {v.variableCode}
                </TableCell>

                {YEARS.map((year) => {
                  const value = getValue(
                    v.variableCode,
                    year,
                  );

                  return (
                    <TableCell
                      key={year}
                      align="center"
                      sx={{
                        bgcolor: color(value),
                        color:
                          value >= 4
                            ? "white"
                            : "inherit",
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
      </Stack>
    </Paper>
  );
}
