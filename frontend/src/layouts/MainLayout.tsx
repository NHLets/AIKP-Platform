import type { ReactNode } from "react";
import { Box } from "@mui/material";

import AppHeader from "./AppHeader";
import AppContent from "./AppContent";

interface MainLayoutProps {
    children: ReactNode;
}

export default function MainLayout({
    children,
}: MainLayoutProps) {

    return (

        <Box
            sx={{
                minHeight: "100vh",
                bgcolor: "background.default",
            }}
        >

            <AppHeader />

            <AppContent>

                {children}

            </AppContent>

        </Box>

    );

}