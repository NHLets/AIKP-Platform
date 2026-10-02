import type { ObservationStatus } from "../types/observation.types";

export interface CellStatusStyle {
  background: string;
  border: string;
  text: string;
}

export function getCellStatusStyle(
  status: ObservationStatus | null,
): CellStatusStyle {

  switch (status) {

    case "PROVIDED":
      return {
        background: "#E8F5E9",
        border: "#81C784",
        text: "#2E7D32",
      };

    case "NOT_AVAILABLE":
      return {
        background: "#FFF3E0",
        border: "#FFB74D",
        text: "#E65100",
      };

    case "NOT_APPLICABLE":
      return {
        background: "#ECEFF1",
        border: "#B0BEC5",
        text: "#455A64",
      };

    default:
      return {
        background: "#FAFAFA",
        border: "#E0E0E0",
        text: "#616161",
      };
  }
}
