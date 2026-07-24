import DashboardOutlinedIcon from "@mui/icons-material/DashboardOutlined";
import PublicOutlinedIcon from "@mui/icons-material/PublicOutlined";
import SettingsOutlinedIcon from "@mui/icons-material/SettingsOutlined";

import type { NavigationItem } from "./Navigation.types";

export const navigation: NavigationItem[] = [
    {
        id: "home",
        label: "Dashboard",
        path: "/",
        icon: DashboardOutlinedIcon,
    },
    {
        id: "countries",
        label: "Countries",
        path: "/countries",
        icon: PublicOutlinedIcon,
    },
    {
        id: "administration",
        label: "Administration",
        path: "/administration",
        icon: SettingsOutlinedIcon,
        requiresAuth: true,
    },
];