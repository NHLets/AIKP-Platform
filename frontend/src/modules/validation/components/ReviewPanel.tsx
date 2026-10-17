import {
  CircularProgress,
  Paper,
  Stack,
  Typography,
} from "@mui/material";
import { useValidationComments } from "../hooks/useValidationComments";
import CommentList from "./CommentList";
import CommentComposer from "./CommentComposer";

interface Props {
  observationId?: string;
}

export default function ReviewPanel({ observationId }: Props) {
  const {
    comments,
    loading,
    error,
    createComment,
  } = useValidationComments(observationId);

  return (
    <Paper elevation={2} sx={{ p: 2, height: "100%" }}>
      <Stack spacing={2}>
        <Typography variant="h6">
          Validation Review
        </Typography>

        {!observationId && (
          <Typography color="text.secondary">
            Select a spreadsheet cell to review its validation history.
          </Typography>
        )}

        {loading && (
          <Stack sx={{ alignItems: "center" }}>
            <CircularProgress size={28} />
          </Stack>
        )}

        {error && (
          <Typography color="error.main">
            {error}
          </Typography>
        )}

        {observationId && !loading && !error && (
          <Stack spacing={2}>
            <CommentList comments={comments} />

            <CommentComposer
              observationId={observationId}
              onSubmit={async (comment, severity) => {
                await createComment({ observationId, comment, severity });
              }}
            />
          </Stack>
        )}
      </Stack>
    </Paper>
  );
}
