import { Card, CardContent, Typography } from "@mui/material";
import type { ReactNode } from "react";

interface Props {
  title: string;
  value: string | number;
  color: string;
  icon?: ReactNode;
}

export default function KpiCard({
  title,
  value,
  color,
  icon,
}: Props) {
  return (
    <Card sx={{ borderTop: `4px solid ${color}` }}>
      <CardContent>
        {icon}
        <Typography variant="body2" color="text.secondary">
          {title}
        </Typography>

        <Typography variant="h4" sx={{ fontWeight: 700 }}>
          {value}
        </Typography>
      </CardContent>
    </Card>
  );
}
