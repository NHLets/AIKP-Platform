import { createContext } from "react";

import type { SnackbarContextValue } from "./Snackbar.types";

export const SnackbarContext = createContext<SnackbarContextValue | undefined>(
    undefined,
);