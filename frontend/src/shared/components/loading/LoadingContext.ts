import { createContext } from "react";

import type { LoadingContextValue } from "./Loading.types";

export const LoadingContext =
    createContext<LoadingContextValue | undefined>(
        undefined,
    );