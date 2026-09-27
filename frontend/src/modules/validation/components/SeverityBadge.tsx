import Chip from "@mui/material/Chip";
import type { ValidationSeverity } from "../types/validation";

interface Props {
  severity: ValidationSeverity;
}

const colors = {
  INFO: "info",
  WARNING: "warning",
  ERROR: "error",
} as const;

export default function SeverityBadge({ severity }: Props) {
  return (
    <Chip
      size="small"
      label={severity}
      color={colors[severity]}
      variant="filled"
    />
  );
}
