import type { ObservationStatus } from "@/modules/data-collection/types";

export interface CellStatusStyle {
  background: string;
  border: string;
  text: string;
}

export function getCellStatusStyle(
  status: ObservationStatus | null,
): CellStatusStyle {
  switch (status) {
    case "VALIDATED":
      return {
        background: "#DCFCE7",
        border: "#16A34A",
        text: "#166534",
      };

    case "REJECTED":
      return {
        background: "#FEE2E2",
        border: "#DC2626",
        text: "#991B1B",
      };

    case "PENDING":
      return {
        background: "#FEF3C7",
        border: "#D97706",
        text: "#92400E",
      };

    case "NOT_AVAILABLE":
      return {
        background: "#F3F4F6",
        border: "#9CA3AF",
        text: "#4B5563",
      };

    default:
      return {
        background: "#FFFFFF",
        border: "#D1D5DB",
        text: "#111827",
      };
  }
}
