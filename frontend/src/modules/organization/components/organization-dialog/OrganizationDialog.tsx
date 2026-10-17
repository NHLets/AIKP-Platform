import { useEffect, useState } from "react";

import {
    Alert,
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
    FormControl,
    InputLabel,
    MenuItem,
    Select,
    Stack,
    TextField,
} from "@mui/material";

import { getCountries, type CountryOption } from "@/modules/data-collection/api/dataCollectionOptionsApi";
import { useCreateOrganization } from "../../hooks/useCreateOrganization";
import type { CreateOrganizationRequest } from "../../types";

interface OrganizationDialogProps {
    open: boolean;
    onClose: () => void;
}

const ORGANIZATION_TYPES = [
    {
        value: "STATISTICAL_OFFICE",
        label: "Statistical Office",
    },
    {
        value: "UTILITY",
        label: "Utility",
    },
    {
        value: "MINISTRY",
        label: "Ministry",
    },
    {
        value: "REGULATOR",
        label: "Regulator",
    },
];

const INITIAL_FORM: CreateOrganizationRequest = {
    code: "",
    name: "",
    type: "",
    countryId: "",
};

export default function OrganizationDialog({
    open,
    onClose,
}: OrganizationDialogProps) {
    const [form, setForm] =
        useState<CreateOrganizationRequest>(INITIAL_FORM);

    const [countries, setCountries] = useState<CountryOption[]>([]);
    const [countriesLoading, setCountriesLoading] = useState(false);
    const [countriesError, setCountriesError] = useState(false);

    const createMutation = useCreateOrganization();

    useEffect(() => {
        if (!open) {
            return;
        }

        setForm(INITIAL_FORM);
        setCountriesError(false);
        setCountriesLoading(true);

        getCountries()
            .then(setCountries)
            .catch(() => setCountriesError(true))
            .finally(() => setCountriesLoading(false));
    }, [open]);

    const handleChange =
        (field: keyof CreateOrganizationRequest) =>
        (
            event:
                | React.ChangeEvent<HTMLInputElement>
                | { target: { value: unknown } },
        ) => {
            setForm((current) => ({
                ...current,
                [field]: String(event.target.value),
            }));
        };

    const handleSubmit = async () => {
        if (
            !form.code.trim() ||
            !form.name.trim() ||
            !form.type ||
            !form.countryId
        ) {
            return;
        }

        try {
            await createMutation.mutateAsync({
                code: form.code.trim(),
                name: form.name.trim(),
                type: form.type,
                countryId: form.countryId,
            });

            onClose();
        } catch {
            // The dialog remains open so the user can correct the data.
        }
    };

    const canSubmit =
        form.code.trim().length > 0 &&
        form.name.trim().length > 0 &&
        form.type.length > 0 &&
        form.countryId.length > 0 &&
        !createMutation.isPending &&
        !countriesLoading;

    return (
        <Dialog
            open={open}
            onClose={createMutation.isPending ? undefined : onClose}
            fullWidth
            maxWidth="sm"
        >
            <DialogTitle>Add Organization</DialogTitle>

            <DialogContent>
                <Stack spacing={2.5} sx={{ pt: 1 }}>
                    {createMutation.isError && (
                        <Alert severity="error">
                            Unable to create the organization. Please verify
                            the information and try again.
                        </Alert>
                    )}

                    {countriesError && (
                        <Alert severity="error">
                            Unable to load countries.
                        </Alert>
                    )}

                    <TextField
                        label="Code"
                        value={form.code}
                        onChange={handleChange("code")}
                        slotProps={{ htmlInput: { maxLength: 50 } }}
                        fullWidth
                        required
                    />

                    <TextField
                        label="Organization Name"
                        value={form.name}
                        onChange={handleChange("name")}
                        slotProps={{ htmlInput: { maxLength: 255 } }}
                        fullWidth
                        required
                    />

                    <FormControl fullWidth required>
                        <InputLabel>Type</InputLabel>

                        <Select
                            value={form.type}
                            label="Type"
                            onChange={handleChange("type")}
                        >
                            {ORGANIZATION_TYPES.map((type) => (
                                <MenuItem
                                    key={type.value}
                                    value={type.value}
                                >
                                    {type.label}
                                </MenuItem>
                            ))}
                        </Select>
                    </FormControl>

                    <FormControl fullWidth required>
                        <InputLabel>Country</InputLabel>

                        <Select
                            value={form.countryId}
                            label="Country"
                            onChange={handleChange("countryId")}
                            disabled={countriesLoading}
                        >
                            {countries.map((country) => (
                                <MenuItem
                                    key={country.id}
                                    value={country.id}
                                >
                                    {country.name} ({country.iso3Code})
                                </MenuItem>
                            ))}
                        </Select>
                    </FormControl>
                </Stack>
            </DialogContent>

            <DialogActions>
                <Button
                    onClick={onClose}
                    disabled={createMutation.isPending}
                >
                    Cancel
                </Button>

                <Button
                    variant="contained"
                    onClick={handleSubmit}
                    disabled={!canSubmit}
                >
                    {createMutation.isPending
                        ? "Creating..."
                        : "Create Organization"}
                </Button>
            </DialogActions>
        </Dialog>
    );
}
