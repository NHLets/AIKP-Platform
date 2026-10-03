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
  HIGH: '#F57C00',
  MEDIUM: '#FBC02D'
} as const;

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
            <Legend
              verticalAlign="bottom"
              height={36}
              wrapperStyle={{ paddingTop: 8 }}
            />
          </PieChart>
        </ResponsiveContainer>
      </CardContent>
    </Card>
  );
}
