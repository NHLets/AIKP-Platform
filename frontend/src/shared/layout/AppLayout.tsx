import { useState } from "react";
import { Outlet } from "react-router-dom";

import Box from "@mui/material/Box";

import AppFooter from "./AppFooter";
import AppHeader from "./AppHeader";
import AppSidebar from "./AppSidebar";

const drawerWidth = 260;

export default function AppLayout() {
    const [sidebarOpen, setSidebarOpen] = useState(false);

    return (
        <Box
            sx={{
                minHeight: "100vh",
                display: "flex",
                flexDirection: "column",
                bgcolor: "background.default",
            }}
        >
            <AppHeader
                onMenuClick={() => setSidebarOpen(true)}
            />

            <Box
                sx={{
                    display: "flex",
                    flexGrow: 1,
                    minHeight: 0,
                }}
            >
                <AppSidebar
                    open={sidebarOpen}
                    onClose={() => setSidebarOpen(false)}
                    width={drawerWidth}
                />

                <Box
                    component="main"
                    sx={{
                        flexGrow: 1,
                        minWidth: 0,
                        p: {
                            xs: 2,
                            sm: 3,
                            md: 4,
                        },
                    }}
                >
                    <Outlet />
                </Box>
            </Box>

            <AppFooter />
        </Box>
    );
}