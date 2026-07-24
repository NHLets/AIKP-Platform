import type { ReactNode } from "react";

import { QueryClient, QueryClientProvider } from "@tanstack/react-query";

import { LoadingProvider } from "@/shared/components/loading";

import SnackbarProvider from "./SnackbarProvider";

import { ErrorBoundary } from "@/shared/components/error-boundary";

interface AppProvidersProps {

    children: ReactNode;
}

const queryClient = new QueryClient();

export default function AppProviders({
    children,
}: AppProvidersProps) {
    return (
        <QueryClientProvider client={queryClient}>
            <ErrorBoundary>
                <LoadingProvider>
                    <SnackbarProvider>
                        {children}
                    </SnackbarProvider>
                </LoadingProvider>
            </ErrorBoundary>
        </QueryClientProvider>
    );
}