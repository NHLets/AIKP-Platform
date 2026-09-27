import { Box, Popover, Typography } from "@mui/material";
import type { ValidationComment } from "../types/validation";

interface Props {
  anchorEl: HTMLElement | null;
  comment?: ValidationComment;
  onClose: () => void;
}

export default function CommentPreview({
  anchorEl,
  comment,
  onClose,
}: Props) {
  const open = Boolean(anchorEl && comment);

  return (
    <Popover
      open={open}
      anchorEl={anchorEl}
      onClose={onClose}
      anchorOrigin={{
        vertical: "bottom",
        horizontal: "left",
      }}
      transformOrigin={{
        vertical: "top",
        horizontal: "left",
      }}
      disableRestoreFocus
    >
      <Box sx={{ p: 2, maxWidth: 280 }}>
        <Typography variant="caption" color="text.secondary">
          {comment?.severity}
        </Typography>

        <Typography variant="body2" sx={{ mt: 0.5 }}>
          {comment?.comment}
        </Typography>

        <Typography
          variant="caption"
          color="text.secondary"
          sx={{ display: "block", mt: 1 }}
        >
          {comment?.createdAt}
        </Typography>
      </Box>
    </Popover>
  );
}
