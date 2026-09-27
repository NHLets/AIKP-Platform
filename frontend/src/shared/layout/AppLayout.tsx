import { useEffect, useState } from "react";
import { Outlet, useLocation } from "react-router-dom";

import Box from "@mui/material/Box";
import IconButton from "@mui/material/IconButton";
import MenuIcon from "@mui/icons-material/Menu";

import AppFooter from "./AppFooter";
import AppHeader from "./AppHeader";
import AppSidebar from "./AppSidebar";

const drawerWidth = 260;

export default function AppLayout() {
    const [sidebarOpen, setSidebarOpen] = useState(false);
    const [sidebarCollapsed, setSidebarCollapsed] = useState(false);

    const location = useLocation();

    const isDataCollectionDetail =
        /^\/data-collections\/[^/]+$/.test(location.pathname);

    useEffect(() => {
        setSidebarCollapsed(isDataCollectionDetail);
        setSidebarOpen(false);
    }, [isDataCollectionDetail]);

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
                onMenuClick={() => {
                    setSidebarCollapsed(false);
                    setSidebarOpen(true);
                }}
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
                    collapsed={sidebarCollapsed}
                />

                {sidebarCollapsed && (
                    <IconButton
                        aria-label="Show navigation menu"
                        onClick={() => {
                            setSidebarCollapsed(false);
                            setSidebarOpen(true);
                        }}
                        size="small"
                        sx={{
                            position: "fixed",
                            left: 8,
                            top: 72,
                            zIndex: (theme) => theme.zIndex.drawer + 2,
                            backgroundColor: "background.paper",
                            boxShadow: 2,
                        }}
                    >
                        <MenuIcon fontSize="small" />
                    </IconButton>
                )}

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