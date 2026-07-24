import type { ReactNode } from "react";

export interface ErrorBoundaryProps {
    children: ReactNode;
}

export interface ErrorFallbackProps {
    error: Error;
    onReset: () => void;
}