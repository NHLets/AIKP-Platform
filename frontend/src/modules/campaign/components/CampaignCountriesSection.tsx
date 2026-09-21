import {
    Alert,
    Box,
    Button,
    CircularProgress,
    FormControl,
    InputLabel,
    MenuItem,
    Paper,
    Select,
    Stack,
    Typography,
} from "@mui/material";

import {
    useEffect,
    useMemo,
    useState,
} from "react";

import {
    addCountryToCampaign,
    getCampaignCountries,
    removeCountryFromCampaign,
} from "../api/campaignCountryApi";

import type {
    CampaignCountry,
} from "../types/campaignCountry.types";

import type {
    CampaignStatus,
} from "../types/campaign.types";

import {
    useCountries,
} from "../../country/hooks/useCountries";

interface CampaignCountriesSectionProps {
    campaignId: string;
    campaignStatus: CampaignStatus;
}


export default function CampaignCountriesSection({
    campaignId,
    campaignStatus,
}: CampaignCountriesSectionProps) {

    const {
        data: countries = [],
        isLoading: countriesLoading,
        error: countriesError,
    } = useCountries();

    const [campaignCountries, setCampaignCountries] =
        useState<CampaignCountry[]>([]);

    const [loading, setLoading] =
        useState(true);

    const [actionLoading, setActionLoading] =
        useState(false);

    const [selectedCountryId, setSelectedCountryId] =
        useState("");

    const [error, setError] =
        useState<string | null>(null);


    const canModify =
        campaignStatus === "DRAFT" ||
        campaignStatus === "PLANNED" ||
        campaignStatus === "ACTIVE";


    async function loadCampaignCountries() {
        try {
            setLoading(true);
            setError(null);

            const response =
                await getCampaignCountries(campaignId);

            setCampaignCountries(response);
        } catch (err) {
            console.error(
                "Failed to load campaign countries:",
                err,
            );

            setError(
                err instanceof Error
                    ? err.message
                    : "Unable to load campaign countries.",
            );
        } finally {
            setLoading(false);
        }
    }


    useEffect(() => {
        void loadCampaignCountries();
    }, [campaignId]);


    const assignedCountryIds = useMemo(
        () =>
            new Set(
                campaignCountries.map(
                    (campaignCountry) =>
                        campaignCountry.countryId,
                ),
            ),
        [campaignCountries],
    );


    const availableCountries = useMemo(
        () =>
            countries.filter(
                (country) =>
                    country.active &&
                    !assignedCountryIds.has(country.id),
            ),
        [countries, assignedCountryIds],
    );


    function getCountryName(
        countryId: string,
    ): string {
        return (
            countries.find(
                (country) =>
                    country.id === countryId,
            )?.name ?? countryId
        );
    }


    async function handleAddCountry() {
        if (
            !selectedCountryId ||
            !canModify
        ) {
            return;
        }

        try {
            setActionLoading(true);
            setError(null);

            const created =
                await addCountryToCampaign(
                    campaignId,
                    {
                        countryId: selectedCountryId,
                    },
                );

            setCampaignCountries(
                (current) => [
                    ...current,
                    created,
                ],
            );

            setSelectedCountryId("");
        } catch (err) {
            console.error(
                "Failed to add country to campaign:",
                err,
            );

            setError(
                err instanceof Error
                    ? err.message
                    : "Unable to add country to campaign.",
            );
        } finally {
            setActionLoading(false);
        }
    }


    async function handleRemoveCountry(
        countryId: string,
    ) {
        if (!canModify) {
            return;
        }

        try {
            setActionLoading(true);
            setError(null);

            await removeCountryFromCampaign(
                campaignId,
                countryId,
            );

            setCampaignCountries(
                (current) =>
                    current.filter(
                        (campaignCountry) =>
                            campaignCountry.countryId !==
                            countryId,
                    ),
            );
        } catch (err) {
            console.error(
                "Failed to remove country from campaign:",
                err,
            );

            setError(
                err instanceof Error
                    ? err.message
                    : "Unable to remove country from campaign.",
            );
        } finally {
            setActionLoading(false);
        }
    }


    return (
        <Paper
            variant="outlined"
            sx={{
                p: 3,
                mt: 3,
            }}
        >
            <Stack spacing={3}>
                <Box>
                    <Typography variant="h6">
                        Countries participating in this campaign
                    </Typography>

                    <Typography
                        variant="body2"
                        color="text.secondary"
                    >
                        Countries included in the campaign
                        data collection.
                    </Typography>
                </Box>

                {error && (
                    <Alert severity="error">
                        {error}
                    </Alert>
                )}

                {countriesError && (
                    <Alert severity="error">
                        Unable to load the country list.
                    </Alert>
                )}

                {canModify && (
                    <Stack
                        direction={{
                            xs: "column",
                            sm: "row",
                        }}
                        spacing={2}
                        sx={{
                            alignItems: {
                                xs: "stretch",
                                sm: "center",
                            },
                        }}
                    >
                        <FormControl
                            fullWidth
                            size="small"
                            disabled={
                                countriesLoading ||
                                actionLoading
                            }
                        >
                            <InputLabel>
                                Add Country
                            </InputLabel>

                            <Select
                                value={selectedCountryId}
                                label="Add Country"
                                onChange={(event) =>
                                    setSelectedCountryId(
                                        event.target.value,
                                    )
                                }
                            >
                                <MenuItem value="">
                                    <em>
                                        Select a country
                                    </em>
                                </MenuItem>

                                {availableCountries.map(
                                    (country) => (
                                        <MenuItem
                                            key={country.id}
                                            value={country.id}
                                        >
                                            {country.name}
                                        </MenuItem>
                                    ),
                                )}
                            </Select>
                        </FormControl>

                        <Button
                            variant="contained"
                            onClick={handleAddCountry}
                            disabled={
                                !selectedCountryId ||
                                actionLoading ||
                                countriesLoading
                            }
                        >
                            Add Country
                        </Button>
                    </Stack>
                )}

                {loading ? (
                    <Box
                        sx={{
                            display: "flex",
                            justifyContent: "center",
                            py: 3,
                        }}
                    >
                        <CircularProgress size={28} />
                    </Box>
                ) : campaignCountries.length === 0 ? (
                    <Typography
                        variant="body2"
                        color="text.secondary"
                    >
                        No countries are currently assigned
                        to this campaign.
                    </Typography>
                ) : (
                    <Stack spacing={1}>
                        {campaignCountries.map(
                            (campaignCountry) => (
                                <Stack
                                    key={campaignCountry.id}
                                    direction="row"
                                    sx={{
                                        justifyContent:
                                            "space-between",
                                        alignItems:
                                            "center",
                                        py: 1,
                                        px: 2,
                                        border: 1,
                                        borderColor:
                                            "divider",
                                        borderRadius: 1,
                                    }}
                                >
                                    <Typography>
                                        {getCountryName(
                                            campaignCountry.countryId,
                                        )}
                                    </Typography>

                                    {canModify && (
                                        <Button
                                            color="error"
                                            size="small"
                                            onClick={() =>
                                                void handleRemoveCountry(
                                                    campaignCountry.countryId,
                                                )
                                            }
                                            disabled={
                                                actionLoading
                                            }
                                        >
                                            Remove
                                        </Button>
                                    )}
                                </Stack>
                            ),
                        )}
                    </Stack>
                )}

                {!canModify && (
                    <Alert severity="info">
                        Country participation is read-only
                        for completed and archived campaigns.
                    </Alert>
                )}
            </Stack>
        </Paper>
    );
}
