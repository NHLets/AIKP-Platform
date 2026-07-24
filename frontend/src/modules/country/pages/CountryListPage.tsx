import { useState } from "react";

import { Alert, CircularProgress} from "@mui/material";

import { CountryDialog } from "../components/country-dialog";
import { CountryTable } from "../components/country-table";
import { useActivateCountry } from "../hooks/useActivateCountry";
import { useCountries } from "../hooks/useCountries";
import { useDeactivateCountry } from "../hooks/useDeactivateCountry";
import { useDeleteCountry } from "../hooks/useDeleteCountry";
import type { CountrySummary } from "../types/CountrySummary";

import { ConfirmDialog } from "@/shared/components/confirm-dialog";
import { EntityToolbar } from "@/shared/components/entity-toolbar";
import { PageHeader } from "@/shared/components/page-header";
import { useSnackbar } from "@/shared/hooks/useSnackbar";

type ConfirmationAction =
    | "activate"
    | "deactivate"
    | "delete"
    | null;

export default function CountryListPage() {

    const snackbar = useSnackbar();

    const { data: countries = [], isLoading, error } = useCountries();

    const activateMutation = useActivateCountry();
    const deactivateMutation = useDeactivateCountry();
    const deleteMutation = useDeleteCountry();

    const [dialogOpen, setDialogOpen] = useState(false);

    const [selectedCountry, setSelectedCountry] =
        useState<CountrySummary>();

    const [confirmationAction, setConfirmationAction] =
        useState<ConfirmationAction>(null);

    const [selectedActionCountry, setSelectedActionCountry] =
        useState<CountrySummary>();

    const handleCreate = () => {

        setSelectedCountry(undefined);

        setDialogOpen(true);

    };

    const handleEdit = (country: CountrySummary) => {

        setSelectedCountry(country);

        setDialogOpen(true);

    };

    const handleClose = () => {
    setDialogOpen(false);
    setSelectedCountry(undefined);
    };
    
    const handleDelete = (country: CountrySummary) => {

        setSelectedActionCountry(country);

        setConfirmationAction("delete");

    };

    const handleActivate = (country: CountrySummary) => {

        setSelectedActionCountry(country);

        setConfirmationAction("activate");

    };

    const handleDeactivate = (country: CountrySummary) => {

        setSelectedActionCountry(country);

        setConfirmationAction("deactivate");

    };

    const handleConfirmAction = async () => {

        if (!selectedActionCountry || !confirmationAction) {
            return;
        }

        try {

            switch (confirmationAction) {

                case "activate":

                    await activateMutation.mutateAsync(
                        selectedActionCountry.id,
                    );

                    snackbar.success(
                        "Country activated successfully.",
                    );

                    break;

                case "deactivate":

                    await deactivateMutation.mutateAsync(
                        selectedActionCountry.id,
                    );

                    snackbar.success(
                        "Country deactivated successfully.",
                    );

                    break;

                case "delete":

                    await deleteMutation.mutateAsync(
                        selectedActionCountry.id,
                    );

                    snackbar.success(
                        "Country deleted successfully.",
                    );

                    break;

            }

        } catch {

            snackbar.error(
                "The requested operation could not be completed.",
            );

        } finally {

            setConfirmationAction(null);

            setSelectedActionCountry(undefined);

        }

    };

    if (isLoading) {

        return <CircularProgress />;

    }

    if (error) {

        return (

            <Alert severity="error">

                Unable to load countries.

            </Alert>

        );

    }

    return (

        <>

            <PageHeader

                title="Countries"

                subtitle="Manage reference countries."

            />

            <EntityToolbar
                searchPlaceholder="Search countries..."
                createLabel="New Country"
                onCreate={handleCreate}
            />

            <CountryTable

                countries={countries}

                onEdit={handleEdit}

                onActivate={handleActivate}

                onDeactivate={handleDeactivate}

                onDelete={handleDelete}

            />

            <CountryDialog
                open={dialogOpen}
                mode={selectedCountry ? "edit" : "create"}
                country={selectedCountry}
                onClose={handleClose}
            />
            <ConfirmDialog

                open={confirmationAction !== null}

                title={

                    confirmationAction === "delete"

                        ? "Delete Country"

                        : confirmationAction === "activate"

                            ? "Activate Country"

                            : "Deactivate Country"

                }

                message={

                    confirmationAction === "delete"

                        ? `Are you sure you want to delete "${selectedActionCountry?.name}"?`

                        : confirmationAction === "activate"

                            ? `Activate "${selectedActionCountry?.name}"?`

                            : `Deactivate "${selectedActionCountry?.name}"?`

                }

                confirmLabel={

                    confirmationAction === "delete"

                        ? "Delete"

                        : confirmationAction === "activate"

                            ? "Activate"

                            : "Deactivate"

                }

                onConfirm={handleConfirmAction}

                onCancel={() => {

                    setConfirmationAction(null);

                    setSelectedActionCountry(undefined);

                }}

            />

        </>

    );

}