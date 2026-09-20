import { useEffect, useMemo, useState } from "react";

import { useNavigate } from "react-router-dom";

import {
    Alert,
    Box,
    Button,
    Chip,
    CircularProgress,
    FormControl,
    InputLabel,
    MenuItem,
    Paper,
    Select,
    Stack,
    Table,
    TextField,
    TableBody,
    TableCell,
    TableHead,
    TablePagination,
    TableRow,
    Typography,
} from "@mui/material";

import { getDataCollections } from "../api/dataCollectionApi";

import {
    getCampaigns,
    getCountries,
    getOrganizations,
    getPersons,
    getQuestionnaires,
} from "../api/dataCollectionOptionsApi";

import type {
    CampaignOption,
    CountryOption,
    OrganizationOption,
    PersonOption,
    QuestionnaireOption,
} from "../api/dataCollectionOptionsApi";

import type {
    DataCollectionStatus,
    DataCollectionSummary,
} from "../types/dataCollection.types";

function getStatusColor(
    status: DataCollectionStatus,
):
    | "default"
    | "primary"
    | "success"
    | "warning"
    | "error" {
    switch (status) {
        case "VALIDATED":
            return "success";

        case "REJECTED":
        case "CANCELLED":
            return "error";

        case "SUBMITTED":
            return "primary";

        case "IN_PROGRESS":
            return "warning";

        default:
            return "default";
    }
}

export default function DataCollectionListPage() {
    const navigate = useNavigate();

    const [dataCollections, setDataCollections] =
        useState<DataCollectionSummary[]>([]);

    const [campaigns, setCampaigns] =
        useState<CampaignOption[]>([]);

    const [countries, setCountries] =
        useState<CountryOption[]>([]);

    const [questionnaires, setQuestionnaires] =
        useState<QuestionnaireOption[]>([]);

    const [organizations, setOrganizations] =
        useState<OrganizationOption[]>([]);

    const [persons, setPersons] =
        useState<PersonOption[]>([]);

    const [campaignFilter, setCampaignFilter] =
        useState("");

    const [countryFilter, setCountryFilter] =
        useState("");

    const [statusFilter, setStatusFilter] =
        useState("");

    const [searchTerm, setSearchTerm] =
        useState("");

    const [sortOption, setSortOption] =
        useState("campaign-asc");

    const [page, setPage] =
        useState(0);

    const [rowsPerPage, setRowsPerPage] =
        useState(10);

    const [isLoading, setIsLoading] =
        useState(true);

    const [error, setError] =
        useState<string | null>(null);

    useEffect(() => {
        async function loadData() {
            try {
                setIsLoading(true);
                setError(null);

                const [
                    dataCollectionData,
                    campaignData,
                    countryData,
                    questionnaireData,
                    organizationData,
                    personData,
                ] = await Promise.all([
                    getDataCollections(),
                    getCampaigns(),
                    getCountries(),
                    getQuestionnaires(),
                    getOrganizations(),
                    getPersons(),
                ]);

                setDataCollections(dataCollectionData);
                setCampaigns(campaignData);
                setCountries(countryData);
                setQuestionnaires(questionnaireData);
                setOrganizations(organizationData);
                setPersons(personData);
            } catch (error) {
                console.error(
                    "Failed to load data collections:",
                    error,
                );

                setError(
                    "Unable to load data collections.",
                );
            } finally {
                setIsLoading(false);
            }
        }

        void loadData();
    }, []);

    const filteredDataCollections = useMemo(
        () => {
            const normalizedSearchTerm =
                searchTerm.trim().toLowerCase();

            const filtered =
                dataCollections.filter((item) => {
                    const matchesCampaign =
                        !campaignFilter ||
                        item.campaignId === campaignFilter;

                    const matchesCountry =
                        !countryFilter ||
                        item.countryId === countryFilter;

                    const matchesStatus =
                        !statusFilter ||
                        item.status === statusFilter;

                    const campaign =
                        campaigns.find(
                            (entry) =>
                                entry.id ===
                                item.campaignId,
                        );

                    const country =
                        countries.find(
                            (entry) =>
                                entry.id ===
                                item.countryId,
                        );

                    const questionnaire =
                        questionnaires.find(
                            (entry) =>
                                entry.id ===
                                item.questionnaireId,
                        );

                    const organization =
                        organizations.find(
                            (entry) =>
                                entry.id ===
                                item.responsibleOrganizationId,
                        );

                    const person =
                        persons.find(
                            (entry) =>
                                entry.id ===
                                item.dataCollectorId,
                        );

                    const searchableValues = [
                        campaign?.code,
                        campaign?.name,
                        country?.iso3Code,
                        country?.name,
                        questionnaire?.code,
                        questionnaire?.name,
                        organization?.code,
                        organization?.name,
                        organizations.find(
                            (entry) =>
                                entry.id ===
                                item.operatorOrganizationId,
                        )?.code,
                        organizations.find(
                            (entry) =>
                                entry.id ===
                                item.operatorOrganizationId,
                        )?.name,
                        person?.fullName,
                        item.status,
                    ];

                    const matchesSearch =
                        !normalizedSearchTerm ||
                        searchableValues.some(
                            (value) =>
                                value
                                    ?.toLowerCase()
                                    .includes(
                                        normalizedSearchTerm,
                                    ),
                        );

                    return (
                        matchesCampaign &&
                        matchesCountry &&
                        matchesStatus &&
                        matchesSearch
                    );
                });

            return [...filtered].sort(
                (a, b) => {
                    const campaignA =
                        campaigns.find(
                            (entry) =>
                                entry.id === a.campaignId,
                        );

                    const campaignB =
                        campaigns.find(
                            (entry) =>
                                entry.id === b.campaignId,
                        );

                    const countryA =
                        countries.find(
                            (entry) =>
                                entry.id === a.countryId,
                        );

                    const countryB =
                        countries.find(
                            (entry) =>
                                entry.id === b.countryId,
                        );

                    switch (sortOption) {
                        case "campaign-desc":
                            return (
                                `${campaignB?.code ?? ""} ${campaignB?.name ?? ""}`
                                    .localeCompare(
                                        `${campaignA?.code ?? ""} ${campaignA?.name ?? ""}`,
                                    )
                            );

                        case "country-asc":
                            return (
                                `${countryA?.iso3Code ?? ""} ${countryA?.name ?? ""}`
                                    .localeCompare(
                                        `${countryB?.iso3Code ?? ""} ${countryB?.name ?? ""}`,
                                    )
                            );

                        case "country-desc":
                            return (
                                `${countryB?.iso3Code ?? ""} ${countryB?.name ?? ""}`
                                    .localeCompare(
                                        `${countryA?.iso3Code ?? ""} ${countryA?.name ?? ""}`,
                                    )
                            );

                        case "status-asc":
                            return a.status.localeCompare(
                                b.status,
                            );

                        case "status-desc":
                            return b.status.localeCompare(
                                a.status,
                            );

                        case "campaign-asc":
                        default:
                            return (
                                `${campaignA?.code ?? ""} ${campaignA?.name ?? ""}`
                                    .localeCompare(
                                        `${campaignB?.code ?? ""} ${campaignB?.name ?? ""}`,
                                    )
                            );
                    }
                },
            );
        },
        [
            dataCollections,
            campaigns,
            countries,
            questionnaires,
            organizations,
            persons,
            campaignFilter,
            countryFilter,
            statusFilter,
            searchTerm,
            sortOption,
        ],
    );

    useEffect(() => {
        setPage(0);
    }, [
        searchTerm,
        statusFilter,
        campaignFilter,
        countryFilter,
        sortOption,
    ]);

    const paginatedDataCollections =
        filteredDataCollections.slice(
            page * rowsPerPage,
            page * rowsPerPage + rowsPerPage,
        );

    if (isLoading) {
        return (
            <Box
                sx={{
                    display: "flex",
                    justifyContent: "center",
                    py: 8,
                }}
            >
                <CircularProgress />
            </Box>
        );
    }

    if (error) {
        return (
            <Alert severity="error">
                {error}
            </Alert>
        );
    }

    return (
        <Box>
            <Stack
                direction="row"
                sx={{
                    justifyContent: "space-between",
                    alignItems: "center",
                    mb: 4,
                }}
            >
                <Box>
                    <Typography
                        variant="h4"
                        sx={{ fontWeight: 700 }}
                    >
                        Data Collections
                    </Typography>

                    <Typography
                        color="text.secondary"
                    >
                        Manage and monitor data collection
                        workflows.
                    </Typography>
                </Box>

                <Button
                    variant="contained"
                    onClick={() =>
                        navigate(
                            "/data-collections/new",
                        )
                    }
                >
                    New Data Collection
                </Button>
            </Stack>

            <Stack
                direction="row"
                spacing={2}
                sx={{ mb: 2 }}
            >
                <TextField
                    fullWidth
                    size="small"
                    label="Search"
                    placeholder="Search data collections..."
                    value={searchTerm}
                    onChange={(event) =>
                        setSearchTerm(
                            event.target.value,
                        )
                    }
                />

                <FormControl
                    fullWidth
                    size="small"
                >
                    <InputLabel>
                        Sort by
                    </InputLabel>

                    <Select
                        label="Sort by"
                        value={sortOption}
                        onChange={(event) =>
                            setSortOption(
                                event.target.value,
                            )
                        }
                    >
                        <MenuItem value="campaign-asc">
                            Campaign A → Z
                        </MenuItem>

                        <MenuItem value="campaign-desc">
                            Campaign Z → A
                        </MenuItem>

                        <MenuItem value="country-asc">
                            Country A → Z
                        </MenuItem>

                        <MenuItem value="country-desc">
                            Country Z → A
                        </MenuItem>

                        <MenuItem value="status-asc">
                            Status A → Z
                        </MenuItem>

                        <MenuItem value="status-desc">
                            Status Z → A
                        </MenuItem>
                    </Select>
                </FormControl>
            </Stack>

            <Stack
                direction="row"
                spacing={2}
                sx={{ mb: 3 }}
            >
                <FormControl
                    fullWidth
                    size="small"
                >
                    <InputLabel>
                        Campaign
                    </InputLabel>

                    <Select
                        label="Campaign"
                        value={campaignFilter}
                        onChange={(event) =>
                            setCampaignFilter(
                                event.target.value,
                            )
                        }
                    >
                        <MenuItem value="">
                            All Campaigns
                        </MenuItem>

                        {campaigns.map((campaign) => (
                            <MenuItem
                                key={campaign.id}
                                value={campaign.id}
                            >
                                {campaign.code} — {campaign.name}
                            </MenuItem>
                        ))}
                    </Select>
                </FormControl>

                <FormControl
                    fullWidth
                    size="small"
                >
                    <InputLabel>
                        Country
                    </InputLabel>

                    <Select
                        label="Country"
                        value={countryFilter}
                        onChange={(event) =>
                            setCountryFilter(
                                event.target.value,
                            )
                        }
                    >
                        <MenuItem value="">
                            All Countries
                        </MenuItem>

                        {countries.map((country) => (
                            <MenuItem
                                key={country.id}
                                value={country.id}
                            >
                                {country.iso3Code} — {country.name}
                            </MenuItem>
                        ))}
                    </Select>
                </FormControl>

                <FormControl
                    fullWidth
                    size="small"
                >
                    <InputLabel>
                        Status
                    </InputLabel>

                    <Select
                        label="Status"
                        value={statusFilter}
                        onChange={(event) =>
                            setStatusFilter(
                                event.target.value,
                            )
                        }
                    >
                        <MenuItem value="">
                            All Statuses
                        </MenuItem>

                        <MenuItem value="DRAFT">
                            DRAFT
                        </MenuItem>

                        <MenuItem value="IN_PROGRESS">
                            IN PROGRESS
                        </MenuItem>

                        <MenuItem value="SUBMITTED">
                            SUBMITTED
                        </MenuItem>

                        <MenuItem value="VALIDATED">
                            VALIDATED
                        </MenuItem>

                        <MenuItem value="REJECTED">
                            REJECTED
                        </MenuItem>

                        <MenuItem value="CANCELLED">
                            CANCELLED
                        </MenuItem>
                    </Select>
                </FormControl>
            </Stack>

            <Paper>
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell>
                                Campaign
                            </TableCell>

                            <TableCell>
                                Country
                            </TableCell>

                            <TableCell>
                                Questionnaire
                            </TableCell>

                            <TableCell>
                                Responsible Organization
                            </TableCell>

                            <TableCell>
                                Operator Organization
                            </TableCell>

                            <TableCell>
                                Data Collector
                            </TableCell>

                            <TableCell>
                                Status
                            </TableCell>
                        </TableRow>
                    </TableHead>

                    <TableBody>
                        {paginatedDataCollections.map(
                            (item) => {
                                const campaign =
                                    campaigns.find(
                                        (entry) =>
                                            entry.id ===
                                            item.campaignId,
                                    );

                                const country =
                                    countries.find(
                                        (entry) =>
                                            entry.id ===
                                            item.countryId,
                                    );

                                const questionnaire =
                                    questionnaires.find(
                                        (entry) =>
                                            entry.id ===
                                            item.questionnaireId,
                                    );

                                const organization =
                                    organizations.find(
                                        (entry) =>
                                            entry.id ===
                                            item.responsibleOrganizationId,
                                    );

                                const operatorOrganization =
                                    organizations.find(
                                        (entry) =>
                                            entry.id ===
                                            item.operatorOrganizationId,
                                    );

                                const person =
                                    persons.find(
                                        (entry) =>
                                            entry.id ===
                                            item.dataCollectorId,
                                    );

                                return (
                                    <TableRow
                                        key={item.id}
                                        hover
                                        onClick={() =>
                                            navigate(
                                                `/data-collections/${item.id}`,
                                            )
                                        }
                                        sx={{
                                            cursor: "pointer",
                                        }}
                                    >
                                        <TableCell>
                                            {campaign
                                                ? `${campaign.code} — ${campaign.name}`
                                                : item.campaignId}
                                        </TableCell>

                                        <TableCell>
                                            {country
                                                ? `${country.iso3Code} — ${country.name}`
                                                : item.countryId}
                                        </TableCell>

                                        <TableCell>
                                            {questionnaire
                                                ? `${questionnaire.code} — ${questionnaire.name}`
                                                : item.questionnaireId}
                                        </TableCell>

                                        <TableCell>
                                            {organization
                                                ? `${organization.code} — ${organization.name}`
                                                : item.responsibleOrganizationId}
                                        </TableCell>

                                        <TableCell>
                                            {operatorOrganization
                                                ? `${operatorOrganization.code} — ${operatorOrganization.name}`
                                                : item.operatorOrganizationId}
                                        </TableCell>

                                        <TableCell>
                                            {person
                                                ? person.fullName
                                                : item.dataCollectorId}
                                        </TableCell>

                                        <TableCell>
                                            <Chip
                                                label={item.status}
                                                color={getStatusColor(
                                                    item.status,
                                                )}
                                                size="small"
                                            />
                                        </TableCell>
                                    </TableRow>
                                );
                            },
                        )}

                        {filteredDataCollections.length ===
                            0 && (
                            <TableRow>
                                <TableCell
                                    colSpan={7}
                                    align="center"
                                >
                                    No data collections found.
                                </TableCell>
                            </TableRow>
                        )}
                    </TableBody>
                </Table>

                <TablePagination
                    component="div"
                    count={filteredDataCollections.length}
                    page={page}
                    onPageChange={(_, newPage) =>
                        setPage(newPage)
                    }
                    rowsPerPage={rowsPerPage}
                    onRowsPerPageChange={(event) => {
                        setRowsPerPage(
                            parseInt(
                                event.target.value,
                                10,
                            ),
                        );
                        setPage(0);
                    }}
                    rowsPerPageOptions={[5, 10, 25, 50]}
                />
            </Paper>
        </Box>
    );
}
