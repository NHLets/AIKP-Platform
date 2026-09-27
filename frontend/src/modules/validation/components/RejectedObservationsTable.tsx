import {
  Chip,
  Paper,
  Stack,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
  Typography,
} from "@mui/material";

import { useRejectedObservations } from "../hooks/useRejectedObservations";

interface Props {
  dataCollectionId: string;
}

export default function RejectedObservationsTable({
  dataCollectionId,
}: Props) {

  const { data = [] } =
    useRejectedObservations(dataCollectionId);

  return (
    <Paper sx={{ p: 2 }}>
      <Stack spacing={2}>
        <Typography variant="h6">
          Rejected Observations
        </Typography>

        <Table size="small">
          <TableHead>
            <TableRow>
              <TableCell>Variable</TableCell>
              <TableCell>Year</TableCell>
              <TableCell>Questionnaire</TableCell>
              <TableCell>Severity</TableCell>
            </TableRow>
          </TableHead>

          <TableBody>
            {data.map((r) => (
              <TableRow key={r.observationId}>
                <TableCell>
                  {r.variableCode}
                </TableCell>

                <TableCell>
                  {r.referenceYear}
                </TableCell>

                <TableCell>
                  {r.questionnaireCode}
                </TableCell>

                <TableCell>
                  <Chip
                    size="small"
                    label={r.severity}
                    color="error"
                  />
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </Stack>
    </Paper>
  );
}
