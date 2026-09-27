import { useState } from "react";
import {
  Button,
  MenuItem,
  Stack,
  TextField,
} from "@mui/material";
import type {
  ValidationSeverity,
} from "../types/validation";

interface Props {
  observationId: string;
  validatorId: string;
  onSubmit: (
    comment: string,
    severity: ValidationSeverity,
  ) => Promise<void>;
}

export default function CommentComposer({
  onSubmit,
}: Props) {
  const [comment, setComment] = useState("");
  const [severity, setSeverity] =
    useState<ValidationSeverity>("WARNING");
  const [saving, setSaving] = useState(false);

  const handleSubmit = async () => {
    if (!comment.trim()) return;

    setSaving(true);
    try {
      await onSubmit(comment, severity);
      setComment("");
      setSeverity("WARNING");
    } finally {
      setSaving(false);
    }
  };

  return (
    <Stack spacing={2}>
      <TextField
        select
        label="Severity"
        value={severity}
        onChange={(e) =>
          setSeverity(
            e.target.value as ValidationSeverity,
          )
        }
        size="small"
      >
        <MenuItem value="INFO">INFO</MenuItem>
        <MenuItem value="WARNING">WARNING</MenuItem>
        <MenuItem value="ERROR">ERROR</MenuItem>
      </TextField>

      <TextField
        label="Validation comment"
        multiline
        minRows={3}
        value={comment}
        onChange={(e) =>
          setComment(e.target.value)
        }
      />

      <Button
        variant="contained"
        disabled={saving || !comment.trim()}
        onClick={() => void handleSubmit()}
      >
        Add Comment
      </Button>
    </Stack>
  );
}
