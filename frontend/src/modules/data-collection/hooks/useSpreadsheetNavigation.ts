import { useCallback } from "react";

export interface CellCoordinate {
  row: number;
  col: number;
}

export function useSpreadsheetNavigation(
  rows: number,
  cols: number,
  onMove: (next: CellCoordinate) => void,
) {
  return useCallback(
    (
      event: React.KeyboardEvent,
      current: CellCoordinate,
    ) => {
      let next = { ...current };

      switch (event.key) {
        case "ArrowUp":
          next.row = Math.max(0, current.row - 1);
          break;

        case "ArrowDown":
        case "Enter":
          next.row = Math.min(rows - 1, current.row + 1);
          break;

        case "ArrowLeft":
          next.col = Math.max(0, current.col - 1);
          break;

        case "ArrowRight":
        case "Tab":
          next.col = Math.min(cols - 1, current.col + 1);
          event.preventDefault();
          break;

        default:
          return;
      }

      onMove(next);
    },
    [rows, cols, onMove],
  );
}
