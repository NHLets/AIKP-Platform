import type { PropsWithChildren } from "react";

import { QueryClient, QueryClientProvider } from "@tanstack/react-query";

import { CssBaseline, ThemeProvider } from "@mui/material";
import { theme } from "../theme";
import { SnackbarProvider } from "../components/snackbar";
import { AuthProvider } from "../context/AuthContext";
import { CampaignProvider } from "../context/CampaignContext";

const queryClient = new QueryClient();

export function AppProviders({ children }: PropsWithChildren) {
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <QueryClientProvider client={queryClient}>
        <SnackbarProvider>
          <AuthProvider>
            <CampaignProvider>
              {children}
            </CampaignProvider>
          </AuthProvider>
        </SnackbarProvider>
      </QueryClientProvider>
    </ThemeProvider>
  );
}
