import {
  Card,
  CardContent,
  Grid,
  Typography,
  Box
} from '@mui/material';

import {
  Message,
  Warning,
  CheckCircle,
  Description
} from '@mui/icons-material';

import { KpiSummary } from '../../types/dashboard';

interface Props {
  data: KpiSummary;
}

export default function KpiCards({ data }: Props) {
  const cards = [
    {
      title: 'Total Comments',
      value: data.totalComments,
      icon: <Message color="primary" />
    },
    {
      title: 'Critical',
      value: data.criticalComments,
      icon: <Warning color="warning" />
    },
    {
      title: 'Affected Variables',
      value: data.affectedVariables,
      icon: <CheckCircle color="success" />
    },
    {
      title: 'Questionnaires',
      value: data.affectedQuestionnaires,
      icon: <Description color="secondary" />
    }
  ];

  return (
    <Grid container spacing={3}>
      {cards.map(card => (
        <Grid item xs={12} sm={6} md={3} key={card.title}>
          <Card elevation={3}>
            <CardContent>
              <Box display="flex" alignItems="center" gap={1}>
                {card.icon}
                <Typography variant="body2" color="text.secondary">
                  {card.title}
                </Typography>
              </Box>

              <Typography variant="h4" fontWeight="bold" mt={2}>
                {card.value.toLocaleString()}
              </Typography>
            </CardContent>
          </Card>
        </Grid>
      ))}
    </Grid>
  );
}
