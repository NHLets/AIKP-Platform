import type { PropsWithChildren } from "react";

import {
    QueryClient,
    QueryClientProvider,
} from "@tanstack/react-query";

import { SnackbarProvider } from "../components/snackbar";

const queryClient = new QueryClient();

export function AppProviders({
    children,
}: PropsWithChildren) {
    return (
        <QueryClientProvider client={queryClient}>
            <SnackbarProvider>
                {children}
            </SnackbarProvider>
        </QueryClientProvider>
    );
}