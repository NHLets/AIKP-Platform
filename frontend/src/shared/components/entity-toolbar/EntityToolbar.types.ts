import type { ReactNode } from "react";

export interface EntityToolbarProps {
    searchPlaceholder: string;
    createLabel: string;
    onSearch?: (value: string) => void;
    onCreate?: () => void;
    createIcon?: ReactNode;
}