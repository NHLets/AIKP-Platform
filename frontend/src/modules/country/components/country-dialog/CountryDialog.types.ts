import type { CountrySummary } from "../../types";

export interface CountryDialogProps {
    open: boolean;
    onClose: () => void;

    mode: "create" | "edit";

    country?: CountrySummary;
}