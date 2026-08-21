import { useMediaQuery, useTheme } from "@mui/material";

import ChevronLeftIcon from "@mui/icons-material/ChevronLeft";
import DashboardOutlinedIcon from "@mui/icons-material/DashboardOutlined";
import CampaignOutlinedIcon from "@mui/icons-material/CampaignOutlined";
import AssignmentOutlinedIcon from "@mui/icons-material/AssignmentOutlined";
import UploadFileOutlinedIcon from "@mui/icons-material/UploadFileOutlined";
import FactCheckOutlinedIcon from "@mui/icons-material/FactCheckOutlined";
import PublicOutlinedIcon from "@mui/icons-material/PublicOutlined";
import BusinessOutlinedIcon from "@mui/icons-material/BusinessOutlined";
import AccountBalanceOutlinedIcon from "@mui/icons-material/AccountBalanceOutlined";
import AssessmentOutlinedIcon from "@mui/icons-material/AssessmentOutlined";
import PeopleOutlineOutlinedIcon from "@mui/icons-material/PeopleOutlineOutlined";
import SettingsOutlinedIcon from "@mui/icons-material/SettingsOutlined";
import type { ReactNode } from "react";

import {
    Box,
    Divider,
    Drawer,
    IconButton,
    List,
    ListItemButton,
    ListItemIcon,
    ListItemText,
    Toolbar,
    Typography,
} from "@mui/material";

import { NavLink } from "react-router-dom";

interface AppSidebarProps {
    open: boolean;
    onClose: () => void;
    width?: number;
}

const drawerWidth = 260;

interface NavigationItem {
    label: string;
    path: string;
    icon: ReactNode;
}

const dataCollectionItems: NavigationItem[] = [
    {
        label: "Campaigns",
        path: "/campaigns",
        icon: <CampaignOutlinedIcon />,
    },
    {
        label: "Questionnaires",
        path: "/questionnaires",
        icon: <AssignmentOutlinedIcon />,
    },
    {
        label: "Submissions",
        path: "/submissions",
        icon: <UploadFileOutlinedIcon />,
    },
    {
        label: "Validation",
        path: "/validation",
        icon: <FactCheckOutlinedIcon />,
    },
];

const referenceItems: NavigationItem[] = [
    {
        label: "Countries",
        path: "/countries",
        icon: <PublicOutlinedIcon />,
    },
    {
        label: "Organizations",
        path: "/organizations",
        icon: <BusinessOutlinedIcon />,
    },
    {
        label: "Institutions",
        path: "/institutions",
        icon: <AccountBalanceOutlinedIcon />,
    },
];

const reportingItems: NavigationItem[] = [
    {
        label: "Indicators",
        path: "/indicators",
        icon: <AssessmentOutlinedIcon />,
    },
];

const administrationItems: NavigationItem[] = [
    {
        label: "Users",
        path: "/users",
        icon: <PeopleOutlineOutlinedIcon />,
    },
    {
        label: "Settings",
        path: "/settings",
        icon: <SettingsOutlinedIcon />,
    },
];

function NavigationSection({
    title,
    items,
    onClose,
}: {
    title: string;
    items: NavigationItem[];
    onClose: () => void;
}) {
    return (
        <>
            <Typography
                variant="overline"
                color="text.secondary"
                sx={{
                    display: "block",
                    px: 2.5,
                    pt: 2.5,
                    pb: 1,
                    fontWeight: 700,
                    letterSpacing: 1,
                }}
            >
                {title}
            </Typography>

            <List disablePadding>
                {items.map((item) => (
                    <ListItemButton
                        key={item.path}
                        component={NavLink}
                        to={item.path}
                        onClick={onClose}
                        sx={{
                            mx: 1,
                            mb: 0.5,
                            borderRadius: 1.5,

                            "&.active": {
                                bgcolor: "action.selected",
                                color: "primary.main",

                                "& .MuiListItemIcon-root": {
                                    color: "primary.main",
                                },
                            },
                        }}
                    >
                        <ListItemIcon
                            sx={{
                                minWidth: 40,
                            }}
                        >
                            {item.icon}
                        </ListItemIcon>

                        <ListItemText
                            primary={item.label}
                        />
                    </ListItemButton>
                ))}
            </List>
        </>
    );
}

export default function AppSidebar({
    open,
    onClose,
    width = drawerWidth,
}: AppSidebarProps) {
    const theme = useTheme();

    const isDesktop = useMediaQuery(
        theme.breakpoints.up("md")
    );

    const drawer = (
        <Box
            sx={{
                height: "100%",
                display: "flex",
                flexDirection: "column",
            }}
        >
            <Toolbar
                sx={{
                    justifyContent: "space-between",
                    px: 2,
                }}
            >
                <Typography
                    variant="subtitle1"
                    sx={{ fontWeight: 700 }}
                >
                    AIKP
                </Typography>
                   

                {!isDesktop && (
                    <IconButton onClick={onClose}>
                        <ChevronLeftIcon />
                    </IconButton>
                )}
            </Toolbar>

            <Divider />

            <Box
                sx={{
                    flexGrow: 1,
                    overflowY: "auto",
                    py: 1,
                }}
            >
                <List disablePadding>
                    <ListItemButton
                        component={NavLink}
                        to="/dashboard"
                        onClick={onClose}
                        sx={{
                            mx: 1,
                            borderRadius: 1.5,

                            "&.active": {
                                bgcolor: "action.selected",
                                color: "primary.main",

                                "& .MuiListItemIcon-root": {
                                    color: "primary.main",
                                },
                            },
                        }}
                    >
                        <ListItemIcon
                            sx={{ minWidth: 40 }}
                        >
                            <DashboardOutlinedIcon />
                        </ListItemIcon>

                        <ListItemText primary="Dashboard" />
                    </ListItemButton>
                </List>

                <NavigationSection
                    title="Data Collection"
                    items={dataCollectionItems}
                    onClose={onClose}
                />

                <NavigationSection
                    title="Reference Data"
                    items={referenceItems}
                    onClose={onClose}
                />

                <NavigationSection
                    title="Reporting"
                    items={reportingItems}
                    onClose={onClose}
                />

                <NavigationSection
                    title="Administration"
                    items={administrationItems}
                    onClose={onClose}
                />
            </Box>

            <Divider />

            <Box sx={{ p: 2 }}>
                <Typography
                    variant="caption"
                    color="text.secondary"
                >
                    AIKP Platform
                </Typography>

                <Typography
                    variant="caption"
                    sx={{
                        display: "block",
                        color: "text.secondary",
                    }}
                >
                    Data Collection Platform
                </Typography>
            </Box>
        </Box>
    );

    return (
        <Drawer
            variant={isDesktop ? "permanent" : "temporary"}
            open={isDesktop || open}
            onClose={onClose}
            ModalProps={{
                keepMounted: true,
            }}
            sx={{
                width,
                flexShrink: 0,

                "& .MuiDrawer-paper": {
                    width,
                    boxSizing: "border-box",
                    position: isDesktop
                        ? "relative"
                        : "fixed",
                },
            }}
        >
            {drawer}
        </Drawer>
    );
}