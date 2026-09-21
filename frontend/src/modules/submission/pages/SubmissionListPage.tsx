import { useEffect, useState } from "react";
import {
    useNavigate,
    useSearchParams,
} from "react-router-dom";

import {
    Alert,
    Box,
    Card,
    CardContent,
    Button,
    Chip,
    CircularProgress,
    Stack,
    TextField,
    MenuItem,
    TablePagination,
    Typography,
} from "@mui/material";

import {
    getDataCollections,
    startDataCollection,
    submitDataCollection,
} from "@/modules/data-collection/api/dataCollectionApi";

import {
    getCampaigns,
    getCountries,
    getOrganizations,
    getPersons,
    getQuestionnaires,
} from "@/modules/data-collection/api/dataCollectionOptionsApi";

import type {
    CampaignOption,
    CountryOption,
    OrganizationOption,
    PersonOption,
    QuestionnaireOption,
} from "@/modules/data-collection/api/dataCollectionOptionsApi";

import type {
    DataCollectionSummary,
    DataCollectionStatus,
} from
    "@/modules/data-collection/types/dataCollection.types";

export default function SubmissionListPage() {
    const navigate = useNavigate();
    const [searchParams] = useSearchParams();

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

    const statusParam =
        searchParams.get("status");

    const status =
        statusParam as DataCollectionStatus | null;

    async function loadDataCollections() {
        try {
            setIsLoading(true);
            setError(null);

            const [
                data,
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

            setDataCollections(data);
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
                "Unable to load submissions.",
            );
        } finally {
            setIsLoading(false);
        }
    }

    useEffect(() => {
        void loadDataCollections();
    }, []);

    async function handleStart(id: string) {
        try {
            setError(null);

            await startDataCollection(id);

            await loadDataCollections();
        } catch (error) {
            console.error(
                "Failed to start data collection:",
                error,
            );

            setError(
                "Unable to start data collection.",
            );
        }
    }

    async function handleSubmit(id: string) {
        try {
            setError(null);

            await submitDataCollection(id);

            await loadDataCollections();
        } catch (error) {
            console.error(
                "Failed to submit data collection:",
                error,
            );

            setError(
                "Unable to submit data collection.",
            );
        }
    }

    const submissions =
        dataCollections.filter((item) => {
            if (!status) {
                return [
                    "DRAFT",
                    "IN_PROGRESS",
                    "SUBMITTED",
                ].includes(item.status);
            }

            return item.status === status;
        });

    const filteredSubmissions =
        submissions
            .filter((item) => {
                const query =
                    searchTerm.toLowerCase().trim();

                if (!query) {
                    return true;
                }

                const campaign =
                    campaigns.find(
                        (entry) =>
                            entry.id === item.campaignId,
                    );

                const country =
                    countries.find(
                        (entry) =>
                            entry.id === item.countryId,
                    );

                const questionnaire =
                    questionnaires.find(
                        (entry) =>
                            entry.id === item.questionnaireId,
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
                            entry.id === item.dataCollectorId,
                    );

                return [
                    item.id,
                    item.status,
                    campaign?.code ?? "",
                    campaign?.name ?? "",
                    country?.iso3Code ?? "",
                    country?.name ?? "",
                    questionnaire?.code ?? "",
                    questionnaire?.name ?? "",
                    organization?.code ?? "",
                    organization?.name ?? "",
                    person?.fullName ?? "",
                ].some((value) =>
                    value
                        .toLowerCase()
                        .includes(query),
                );
            })
            .sort((a, b) => {
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

                const campaignNameA =
                    campaignA
                        ? `${campaignA.code} ${campaignA.name}`
                        : a.campaignId;

                const campaignNameB =
                    campaignB
                        ? `${campaignB.code} ${campaignB.name}`
                        : b.campaignId;

                const countryNameA =
                    countryA
                        ? `${countryA.iso3Code} ${countryA.name}`
                        : a.countryId;

                const countryNameB =
                    countryB
                        ? `${countryB.iso3Code} ${countryB.name}`
                        : b.countryId;

                switch (sortOption) {
                    case "campaign-desc":
                        return campaignNameB.localeCompare(
                            campaignNameA,
                        );

                    case "country-asc":
                        return countryNameA.localeCompare(
                            countryNameB,
                        );

                    case "country-desc":
                        return countryNameB.localeCompare(
                            countryNameA,
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
                        return campaignNameA.localeCompare(
                            campaignNameB,
                        );
                }
            });

    const paginatedSubmissions =
        filteredSubmissions.slice(
            page * rowsPerPage,
            page * rowsPerPage + rowsPerPage,
        );

    useEffect(() => {
        setPage(0);
    }, [searchTerm, sortOption, status]);

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
                        sx={{
                            fontWeight: 700,
                            mb: 1,
                        }}
                    >
                        Submissions
                    </Typography>

                    <Typography
                        color="text.secondary"
                    >
                        {status
                            ? `Showing ${status} submissions`
                            : "Manage data collection submissions"}
                    </Typography>
                </Box>

                <Button
                    variant="contained"
                    onClick={() =>
                        navigate("/data-collections/new")
                    }
                >
                    Create Data Collection
                </Button>
            </Stack>

            <Stack
                direction="row"
                spacing={2}
                sx={{
                    mb: 3,
                    alignItems: "center",
                }}
            >
                <TextField
                    label="Search submissions"
                    value={searchTerm}
                    onChange={(event) =>
                        setSearchTerm(event.target.value)
                    }
                    fullWidth
                />

                <TextField
                    select
                    label="Sort by"
                    value={sortOption}
                    onChange={(event) =>
                        setSortOption(event.target.value)
                    }
                    sx={{
                        minWidth: 200,
                    }}
                >
                    <MenuItem value="campaign-asc">
                        Campaign: A–Z
                    </MenuItem>

                    <MenuItem value="campaign-desc">
                        Campaign: Z–A
                    </MenuItem>

                    <MenuItem value="country-asc">
                        Country: A–Z
                    </MenuItem>

                    <MenuItem value="country-desc">
                        Country: Z–A
                    </MenuItem>

                    <MenuItem value="status-asc">
                        Status: A–Z
                    </MenuItem>

                    <MenuItem value="status-desc">
                        Status: Z–A
                    </MenuItem>
                </TextField>
            </Stack>

            <Stack spacing={2}>
                {filteredSubmissions.length === 0 ? (
                    <Alert severity="info">
                        No submissions found.
                    </Alert>
                ) : (
                    paginatedSubmissions.map((item) => {
                        const campaign =
                            campaigns.find(
                                (entry) =>
                                    entry.id === item.campaignId,
                            );

                        const country =
                            countries.find(
                                (entry) =>
                                    entry.id === item.countryId,
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

                        return (
                        <Card
                            key={item.id}
                            onClick={() =>
                                navigate(
                                    `/data-collections/${item.id}`,
                                )
                            }
                            sx={{
                                cursor: "pointer",
                            }}
                        >
                            <CardContent>
                                <Stack
                                    direction="row"
                                    sx={{
                                        justifyContent:
                                            "space-between",
                                        alignItems:
                                            "center",
                                    }}
                                >
                                    <Box>
                                        <Typography
                                            variant="subtitle1"
                                            sx={{
                                                fontWeight: 600,
                                            }}
                                        >
                                            Data Collection
                                        </Typography>

                                        <Typography
                                            variant="body2"
                                            color="text.secondary"
                                        >
                                            ID: {item.id}
                                        </Typography>

                                        <Typography
                                            variant="body2"
                                            color="text.secondary"
                                        >
                                            Campaign: {campaign
                                                ? `${campaign.code} — ${campaign.name}`
                                                : item.campaignId}
                                        </Typography>

                                        <Typography
                                            variant="body2"
                                            color="text.secondary"
                                        >
                                            Country: {country
                                                ? `${country.iso3Code} — ${country.name}`
                                                : item.countryId}
                                        </Typography>

                                        <Typography
                                            variant="body2"
                                            color="text.secondary"
                                        >
                                            Questionnaire: {questionnaire
                                                ? `${questionnaire.code} — ${questionnaire.name}`
                                                : item.questionnaireId}
                                        </Typography>

                                        <Typography
                                            variant="body2"
                                            color="text.secondary"
                                        >
                                            Organization: {organization
                                                ? `${organization.code} — ${organization.name}`
                                                : item.responsibleOrganizationId}
                                        </Typography>

                                        <Typography
                                            variant="body2"
                                            color="text.secondary"
                                        >
                                            Data Collector: {person
                                                ? person.fullName
                                                : item.dataCollectorId}
                                        </Typography>
                                    </Box>

                                    <Stack
                                        direction="row"
                                        spacing={1}
                                        sx={{
                                            alignItems: "center",
                                        }}
                                    >
                                        <Chip
                                            label={item.status}
                                            color={
                                                item.status ===
                                                "SUBMITTED"
                                                    ? "primary"
                                                    : item.status ===
                                                      "IN_PROGRESS"
                                                    ? "warning"
                                                    : "default"
                                            }
                                        />

                                        {item.status === "DRAFT" && (
                                            <Button
                                                variant="contained"
                                                size="small"
                                                onClick={() =>
                                                    void handleStart(
                                                        item.id,
                                                    )
                                                }
                                            >
                                                Start
                                            </Button>
                                        )}

                                        {item.status ===
                                            "IN_PROGRESS" && (
                                            <Button
                                                variant="contained"
                                                size="small"
                                                onClick={() =>
                                                    void handleSubmit(
                                                        item.id,
                                                    )
                                                }
                                            >
                                                Submit
                                            </Button>
                                        )}
                                    </Stack>
                                </Stack>
                            </CardContent>
                        </Card>
                        );
                    })
                )}
            </Stack>

            <TablePagination
                component="div"
                count={filteredSubmissions.length}
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
                rowsPerPageOptions={[
                    5,
                    10,
                    25,
                ]}
            />
        </Box>
    );
}
