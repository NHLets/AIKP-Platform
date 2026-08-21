export interface AppLayoutProps {
}

export interface AppHeaderProps {
    title?: string;
    onMenuClick?: () => void;
}

export interface AppSidebarProps {
    open: boolean;
    onClose: () => void;
    width?: number;
}

export interface AppFooterProps {
    version?: string;
}