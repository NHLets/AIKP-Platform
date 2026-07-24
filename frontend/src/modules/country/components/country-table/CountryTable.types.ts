import type { CountrySummary } from "../../types";

export interface CountryTableProps {

    countries: CountrySummary[];

    onEdit: (country: CountrySummary) => void;

    onActivate: (country: CountrySummary) => void;

    onDeactivate: (country: CountrySummary) => void;

    onDelete: (country: CountrySummary) => void;

}