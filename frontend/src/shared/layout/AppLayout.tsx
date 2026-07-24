import { useState } from "react";

import Box from "@mui/material/Box";
import Toolbar from "@mui/material/Toolbar";

import AppFooter from "./AppFooter";
import AppHeader from "./AppHeader";
import AppSidebar from "./AppSidebar";
import type { AppLayoutProps } from "./Layout.types";

export default function AppLayout({
    children,
}: AppLayoutProps) {
    const [sidebarOpen, setSidebarOpen] = useState(false);

    return (
        <Box
            sx={{
                display: "flex",
                minHeight: "100vh",
                flexDirection: "column",
            }}
        >
            <AppHeader />

            <AppSidebar
                open={sidebarOpen}
                onClose={() => setSidebarOpen(false)}
            />

            <Toolbar />

            <Box
                component="main"
                sx={{
                    flexGrow: 1,
                    p: 3,
                }}
            >
                {children}
            </Box>

            <AppFooter />
        </Box>
    );
}