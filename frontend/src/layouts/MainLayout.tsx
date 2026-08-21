import type { ReactNode } from "react";

import Box from "@mui/material/Box";

import AppHeader from "./AppHeader";
import { AppDrawer } from "./AppDrawer";
import AppContent from "./AppContent";
import Toolbar from "@mui/material/Toolbar";

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
                display: "flex",
                bgcolor: "background.default",
            }}
        >
            <AppHeader />

            <AppDrawer />

            <Box
                component="main"
                sx={{
                    flexGrow: 1,
                    minWidth: 0,
                    display: "flex",
                    flexDirection: "column",
                    minHeight: "100vh",
                }}
            >
                
                <Toolbar />
                <Box
                    sx={{
                        flexGrow: 1,
                    }}
                >
                    <AppContent>
                        {children}
                    </AppContent>
                </Box>
            </Box>
        </Box>
    );
}