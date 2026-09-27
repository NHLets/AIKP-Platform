import {
  LinearProgress,
  Paper,
  Stack,
  Typography,
} from "@mui/material";

import { useQuestionnaireProgress } from "../hooks/useQuestionnaireProgress";

interface Props {
  dataCollectionId: string;
}

export default function QuestionnaireProgressPanel({
  dataCollectionId,
}: Props) {
  const { data = [] } =
    useQuestionnaireProgress(dataCollectionId);

  return (
    <Paper sx={{ p: 2 }}>
      <Stack spacing={2}>
        <Typography variant="h6">
          Progress by Questionnaire
        </Typography>

        {data.map((q) => (
          <Stack
            key={q.questionnaireCode}
            spacing={0.5}
          >
            <Stack
              direction="row"
              justifyContent="space-between"
            >
              <Typography variant="body2">
                {q.questionnaireCode}
              </Typography>

              <Typography variant="body2">
                {q.completionRate.toFixed(1)}%
              </Typography>
            </Stack>

            <LinearProgress
              variant="determinate"
              value={q.completionRate}
              sx={{
                height: 8,
                borderRadius: 4,
              }}
            />

            <Typography
              variant="caption"
              color="text.secondary"
            >
              {`${q.completed} / ${q.total} completed`}
            </Typography>
          </Stack>
        ))}
      </Stack>
    </Paper>
  );
}
