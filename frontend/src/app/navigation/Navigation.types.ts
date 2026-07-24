import type { SvgIconComponent } from "@mui/icons-material";

export interface NavigationItem {
    id: string;
    label: string;
    path: string;
    icon: SvgIconComponent;
    children?: NavigationItem[];
    requiresAuth?: boolean;
    roles?: string[];
}