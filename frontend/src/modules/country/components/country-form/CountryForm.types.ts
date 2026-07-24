import type { CreateCountryRequest } from "../../types";

export interface CountryFormProps {
    initialValues?: Partial<CreateCountryRequest>;
    submitLabel: string;
    submitting?: boolean;
    onSubmit: (values: CreateCountryRequest) => void;
    onCancel?: () => void;
}