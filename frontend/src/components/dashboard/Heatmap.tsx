import {
  Card,
  CardContent,
  Typography,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow
} from '@mui/material';

import type { ValidationHeatmap } from "../../types/dashboard";

interface Props {
  data: ValidationHeatmap[];
}

export default function Heatmap({ data }: Props) {

  const max = Math.max(...data.map(d => d.count), 1);

  const color = (value: number) => {
    const alpha = value / max;
    return `rgba(25,118,210,${alpha.toFixed(2)})`;
  };

  return (
    <Card elevation={3}>
      <CardContent>
        <Typography variant="h6" gutterBottom>
          Validation Heatmap
        </Typography>

        <Table size="small">
          <TableHead>
            <TableRow>
              <TableCell>Questionnaire</TableCell>
              <TableCell>Variable</TableCell>
              <TableCell align="center">Comments</TableCell>
            </TableRow>
          </TableHead>

          <TableBody>
            {data.map((row, index) => (
              <TableRow key={index}>
                <TableCell>{row.questionnaire}</TableCell>
                <TableCell>{row.variable}</TableCell>
                <TableCell
                  align="center"
                  sx={{
                    bgcolor: color(row.count),
                    color: row.count > max * 0.5 ? '#fff' : '#000',
                    fontWeight: 'bold'
                  }}
                >
                  {row.count}
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </CardContent>
    </Card>
  );
}
