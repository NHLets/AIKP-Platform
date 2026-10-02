import {
  Card,
  CardContent,
  Typography
} from '@mui/material';

import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer
} from 'recharts';

import type { ValidationTrend } from "../../types/dashboard";

interface Props {
  data: ValidationTrend[];
}

export default function TrendChart({ data }: Props) {
  return (
    <Card elevation={3}>
      <CardContent>
        <Typography variant="h6" gutterBottom>
          Validation Trend
        </Typography>

        <ResponsiveContainer width="100%" height={320}>
          <LineChart data={data}>
            <CartesianGrid strokeDasharray="3 3" />

            <XAxis dataKey="monthName" />

            <YAxis allowDecimals={false} />

            <Tooltip />

            <Line
              type="monotone"
              dataKey="count"
              stroke="#1976D2"
              strokeWidth={3}
              dot={{ r: 5 }}
              activeDot={{ r: 7 }}
            />
          </LineChart>
        </ResponsiveContainer>
      </CardContent>
    </Card>
  );
}
