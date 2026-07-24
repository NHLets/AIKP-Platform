import type { ReactNode } from "react";

export interface AppLayoutProps {
    children: ReactNode;
}

export interface AppHeaderProps {
    title?: string;
}

export interface AppSidebarProps {
    open: boolean;
    onClose: () => void;
}

export interface AppFooterProps {
    version?: string;
}