import { useEffect, useState } from "react";

import { isAxiosError } from "axios";
import {
    Alert,
    CircularProgress,
} from "@mui/material";

import {
    getCountries,
    type CountryOption,
} from "@/modules/data-collection/api/dataCollectionOptionsApi";
import { ConfirmDialog } from "@/shared/components/confirm-dialog";
import { EntityToolbar } from "@/shared/components/entity-toolbar";
import { PageHeader } from "@/shared/components/page-header";
import { useSnackbar } from "@/shared/hooks/useSnackbar";

import OrganizationDialog from "../components/organization-dialog/OrganizationDialog";
import OrganizationTable from "../components/organization-table/OrganizationTable";
import { useDeleteOrganization } from "../hooks/useDeleteOrganization";
import { useOrganizations } from "../hooks/useOrganizations";
import type { OrganizationSummary } from "../types";

export default function OrganizationListPage() {
    const [search, setSearch] = useState("");
    const [dialogOpen, setDialogOpen] = useState(false);
    const [selectedOrganization, setSelectedOrganization] =
        useState<OrganizationSummary>();

    const snackbar = useSnackbar();

    const {
        data: organizations = [],
        isLoading: organizationsLoading,
        error: organizationsError,
    } = useOrganizations();

    const deleteMutation = useDeleteOrganization();

    const [countries, setCountries] = useState<CountryOption[]>([]);
    const [countriesLoading, setCountriesLoading] = useState(false);
    const [countriesError, setCountriesError] = useState(false);

    useEffect(() => {
        setCountriesLoading(true);

        getCountries()
            .then(setCountries)
            .catch(() => setCountriesError(true))
            .finally(() => setCountriesLoading(false));
    }, []);

    const filteredOrganizations = organizations.filter((organization) => {
        const query = search.trim().toLowerCase();

        if (!query) {
            return true;
        }

        const country = countries.find(
            (item) => item.id === organization.countryId,
        );

        return [
            organization.code,
            organization.name,
            organization.type,
            country?.name,
            country?.iso2Code,
            country?.iso3Code,
        ].some((value) =>
            String(value ?? "")
                .toLowerCase()
                .includes(query),
        );
    });

    const handleDialogClose = () => {
        setDialogOpen(false);
    };

    const handleDelete = (organization: OrganizationSummary) => {
        setSelectedOrganization(organization);
    };

    const handleConfirmDelete = async () => {
        if (!selectedOrganization) {
            return;
        }

        try {
            await deleteMutation.mutateAsync(selectedOrganization.id);

            snackbar.success("Organization deleted successfully.");
        } catch (error) {
            if (isAxiosError(error) && error.response?.status === 409) {
                const responseData = error.response.data as
                    | { detail?: string; message?: string }
                    | undefined;

                snackbar.error(
                    responseData?.detail ??
                        responseData?.message ??
                        "This organization cannot be deleted because it is involved in a data collection.",
                );
            } else {
                snackbar.error(
                    "The organization could not be deleted.",
                );
            }
        } finally {
            setSelectedOrganization(undefined);
        }
    };

    if (organizationsLoading || countriesLoading) {
        return <CircularProgress />;
    }

    if (organizationsError || countriesError) {
        return (
            <Alert severity="error">
                Unable to load organizations or countries.
            </Alert>
        );
    }

    return (
        <>
            <PageHeader
                title="Organizations"
                subtitle="Manage organizations participating in the AIKP platform."
            />

            <EntityToolbar
                searchPlaceholder="Search organizations..."
                createLabel="Add Organization"
                onSearch={setSearch}
                onCreate={() => setDialogOpen(true)}
            />

            <OrganizationTable
                organizations={filteredOrganizations}
                countries={countries}
                onDelete={handleDelete}
            />

            <OrganizationDialog
                open={dialogOpen}
                onClose={handleDialogClose}
            />

            <ConfirmDialog
                open={selectedOrganization !== undefined}
                title="Delete Organization"
                message={`Are you sure you want to delete "${selectedOrganization?.name}"?`}
                confirmLabel="Delete"
                cancelLabel="Cancel"
                loading={deleteMutation.isPending}
                danger
                onConfirm={handleConfirmDelete}
                onCancel={() => {
                    if (!deleteMutation.isPending) {
                        setSelectedOrganization(undefined);
                    }
                }}
            />
        </>
    );
}
