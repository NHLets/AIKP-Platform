import { Stack, Typography, Divider } from "@mui/material";
import type { ValidationComment } from "../types/validation";
import SeverityBadge from "./SeverityBadge";

interface Props {
  comments: ValidationComment[];
}

export default function CommentList({ comments }: Props) {
  if (comments.length === 0) {
    return (
      <Typography variant="body2" color="text.secondary">
        No validation comments.
      </Typography>
    );
  }

  return (
    <Stack spacing={2}>
      {comments.map((comment) => (
        <Stack key={comment.id} spacing={1}>
          <Stack direction="row" spacing={1} sx={{ alignItems: "center" }}>
            <SeverityBadge severity={comment.severity} />
            <Typography variant="caption" color="text.secondary">
              {new Date(comment.createdAt).toLocaleString()}
            </Typography>
          </Stack>

          <Typography variant="body2">
            {comment.comment}
          </Typography>

          <Divider />
        </Stack>
      ))}
    </Stack>
  );
}
