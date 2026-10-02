import {
  Card,
  CardContent,
  Typography
} from '@mui/material';

import {
  PieChart,
  Pie,
  Cell,
  Tooltip,
  ResponsiveContainer,
  Legend
} from 'recharts';

import type { SeverityDistribution } from "../../types/dashboard";

interface Props {
  data: SeverityDistribution[];
}

const COLORS = {
  CRITICAL: '#D32F2F',
  ERROR: '#F57C00',
  WARNING: '#FBC02D',
  INFO: '#1976D2'
};

export default function SeverityChart({ data }: Props) {
  return (
    <Card elevation={3}>
      <CardContent>
        <Typography variant="h6" gutterBottom>
          Validation Severity
        </Typography>

        <ResponsiveContainer width="100%" height={320}>
          <PieChart>
            <Pie
              data={data}
              dataKey="count"
              nameKey="severity"
              innerRadius={70}
              outerRadius={110}
              paddingAngle={2}
            >
              {data.map(item => (
                <Cell
                  key={item.severity}
                  fill={COLORS[item.severity as keyof typeof COLORS] ?? '#9E9E9E'}
                />
              ))}
            </Pie>

            <Tooltip />
            <Legend />
          </PieChart>
        </ResponsiveContainer>
      </CardContent>
    </Card>
  );
}
