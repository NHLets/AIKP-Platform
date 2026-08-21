import type { ReactNode } from "react";

import {
    Box,
    Chip,
    Divider,
    Drawer,
    List,
    ListItemButton,
    ListItemIcon,
    ListItemText,
    Toolbar,
    Typography,
} from "@mui/material";

import DashboardOutlinedIcon from "@mui/icons-material/DashboardOutlined";
import CampaignOutlinedIcon from "@mui/icons-material/CampaignOutlined";
import PublicOutlinedIcon from "@mui/icons-material/PublicOutlined";
import AssignmentOutlinedIcon from "@mui/icons-material/AssignmentOutlined";
import FactCheckOutlinedIcon from "@mui/icons-material/FactCheckOutlined";

import BusinessOutlinedIcon from "@mui/icons-material/BusinessOutlined";
import AccountBalanceOutlinedIcon from "@mui/icons-material/AccountBalanceOutlined";
import AssessmentOutlinedIcon from "@mui/icons-material/AssessmentOutlined";

import BarChartOutlinedIcon from "@mui/icons-material/BarChartOutlined";

import PeopleOutlinedIcon from "@mui/icons-material/PeopleOutlined";
import SettingsOutlinedIcon from "@mui/icons-material/SettingsOutlined";

import { NavLink } from "react-router-dom";

import type { AppDrawerProps } from "./AppDrawer.types";

const drawerWidth = 280;

interface NavigationItem {
    label: string;
    icon: ReactNode;
    path?: string;
}

interface NavigationSection {
    title?: string;
    items: NavigationItem[];
}

const navigationSections: NavigationSection[] = [
    {
        items: [
            {
                label: "Dashboard",
                icon: <DashboardOutlinedIcon />,
                path: "/dashboard",
            },
        ],
    },
    {
        title: "DATA COLLECTION",
        items: [
            {
                label: "Campaigns",
                icon: <CampaignOutlinedIcon />,
                path: "/campaigns",
            },
            {
                label: "Countries",
                icon: <PublicOutlinedIcon />,
                path: "/countries",
            },
            {
                label: "Questionnaires",
                icon: <AssignmentOutlinedIcon />,
            },
            {
                label: "Submissions",
                icon: <AssignmentOutlinedIcon />,
            },
            {
                label: "Validation",
                icon: <FactCheckOutlinedIcon />,
            },
        ],
    },
    {
        title: "REFERENCE DATA",
        items: [
            {
                label: "Organizations",
                icon: <BusinessOutlinedIcon />,
            },
            {
                label: "Institutions",
                icon: <AccountBalanceOutlinedIcon />,
            },
            {
                label: "Indicators",
                icon: <AssessmentOutlinedIcon />,
            },
        ],
    },
    {
        title: "REPORTING",
        items: [
            {
                label: "Reports",
                icon: <BarChartOutlinedIcon />,
            },
        ],
    },
    {
        title: "ADMINISTRATION",
        items: [
            {
                label: "Users",
                icon: <PeopleOutlinedIcon />,
            },
            {
                label: "Settings",
                icon: <SettingsOutlinedIcon />,
            },
        ],
    },
];

export default function AppDrawer({
    width = drawerWidth,
}: AppDrawerProps) {
    return (
        <Drawer
            variant="permanent"
            sx={{
                width,
                flexShrink: 0,

                "& .MuiDrawer-paper": {
                    width,
                    boxSizing: "border-box",
                    borderRight: 1,
                    borderColor: "divider",
                },
            }}
        >
            <Toolbar />

            <Box
                sx={{
                    px: 2.5,
                    py: 2,
                }}
            >
                <Typography
                    variant="subtitle2"
                    sx={{
                        fontWeight: 700,
                        letterSpacing: 0.5,
                    }}
                >
                    AIKP PORTAL
                </Typography>

                <Typography
                    variant="caption"
                    color="text.secondary"
                >
                    Infrastructure Data Platform
                </Typography>
            </Box>

            <Divider />

            <Box
                sx={{
                    overflowY: "auto",
                    flexGrow: 1,
                    py: 1,
                }}
            >
                {navigationSections.map((section, index) => (
                    <Box
                        key={section.title ?? `section-${index}`}
                        sx={{
                            mb: 1,
                        }}
                    >
                        {section.title && (
                            <Typography
                                variant="caption"
                                sx={{
                                    display: "block",
                                    px: 2.5,
                                    pt: 2,
                                    pb: 0.75,
                                    fontWeight: 700,
                                    letterSpacing: 0.8,
                                    color: "text.secondary",
                                }}
                            >
                                {section.title}
                            </Typography>
                        )}

                        <List
                            disablePadding
                            sx={{
                                px: 1,
                            }}
                        >
                            {section.items.map((item) => {
                                if (!item.path) {
                                    return (
                                        <ListItemButton
                                            key={item.label}
                                            disabled
                                            sx={{
                                                py: 1.1,
                                                opacity: 0.65,
                                            }}
                                        >
                                            <ListItemIcon>
                                                {item.icon}
                                            </ListItemIcon>

                                            <ListItemText
                                                primary={item.label}
                                            />

                                            <Chip
                                                label="COMING SOON"
                                                size="small"
                                                variant="outlined"
                                                sx={{
                                                    height: 20,
                                                    fontSize: "0.6rem",
                                                    fontWeight: 700,
                                                }}
                                            />
                                        </ListItemButton>
                                    );
                                }

                                return (
                                    <ListItemButton
                                        key={item.label}
                                        component={NavLink}
                                        to={item.path}
                                        sx={{
                                            py: 1.1,

                                            "&.active": {
                                                bgcolor: "primary.main",
                                                color: "primary.contrastText",

                                                "& .MuiListItemIcon-root": {
                                                    color: "primary.contrastText",
                                                },

                                                "&:hover": {
                                                    bgcolor: "primary.dark",
                                                },
                                            },
                                        }}
                                    >
                                        <ListItemIcon>
                                            {item.icon}
                                        </ListItemIcon>

                                        <ListItemText
                                            primary={item.label}
                                        />
                                    </ListItemButton>
                                );
                            })}
                        </List>
                    </Box>
                ))}
            </Box>

            <Box
                sx={{
                    p: 2,
                    borderTop: 1,
                    borderColor: "divider",
                }}
            >
                <Typography
                    variant="caption"
                    color="text.secondary"
                >
                    AIKP Platform
                </Typography>

                <Typography
                    variant="caption"
                    color="text.secondary"
                    sx={{
                        display: "block",
                    }}
                >
                    Version 0.1.0
                </Typography>
            </Box>
        </Drawer>
    );
}