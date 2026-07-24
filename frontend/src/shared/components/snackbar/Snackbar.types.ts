import type { ReactNode } from "react";

export type SnackbarSeverity =
    | "success"
    | "error"
    | "warning"
    | "info";

export interface SnackbarMessage {

    id: string;

    severity: SnackbarSeverity;

    message: string;

    autoHideDuration?: number;

}

export interface SnackbarContextValue {

    success(
        message: string,
        autoHideDuration?: number,
    ): void;

    error(
        message: string,
        autoHideDuration?: number,
    ): void;

    warning(
        message: string,
        autoHideDuration?: number,
    ): void;

    info(
        message: string,
        autoHideDuration?: number,
    ): void;

}

export interface SnackbarProviderProps {

    children: ReactNode;

}