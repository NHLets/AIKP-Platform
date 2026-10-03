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
  ResponsiveContainer,
  Legend
} from 'recharts';

import type { ValidationTrend } from "../../types/dashboard";

interface Props {
  data: ValidationTrend[];
}

export default function TrendChart({ data }: Props) {
  const formatPeriod = (period: string) => {
    const [year, month] = period.split('-');
    const date = new Date(Number(year), Number(month) - 1, 1);

    return new Intl.DateTimeFormat('en-US', {
      month: 'short',
      year: 'numeric'
    }).format(date);
  };

  const chartData = data.map(item => ({
    ...item,
    periodLabel: formatPeriod(item.period)
  }));

  return (
    <Card elevation={3}>
      <CardContent>
        <Typography variant="h6" gutterBottom>
          Validation Trend
        </Typography>

        <ResponsiveContainer width="100%" height={320}>
          <LineChart data={chartData}>
            <CartesianGrid strokeDasharray="3 3" />

            <XAxis dataKey="periodLabel" />

            <YAxis allowDecimals={false} />

            <Tooltip />

            <Legend
              verticalAlign="bottom"
              height={36}
              wrapperStyle={{ paddingTop: 8 }}
            />

            <Line
              type="monotone"
              dataKey="validated"
              name="Validated"
              stroke="#2E7D32"
              strokeWidth={3}
              dot={{ r: 5 }}
              activeDot={{ r: 7 }}
            />

            <Line
              type="monotone"
              dataKey="rejected"
              name="Rejected"
              stroke="#D32F2F"
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
