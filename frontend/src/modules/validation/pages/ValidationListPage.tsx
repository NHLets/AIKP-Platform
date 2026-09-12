import {
    useEffect,
    useMemo,
    useState,
} from "react";

import {
    useNavigate,
    useSearchParams,
} from "react-router-dom";

import {
    Alert,
    Box,
    Button,
    Card,
    CardContent,
    Chip,
    CircularProgress,
    MenuItem,
    Stack,
    TablePagination,
    TextField,
    Typography,
} from "@mui/material";

import {
    getDataCollections,
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
} from "@/modules/data-collection/types/dataCollection.types";

export default function ValidationListPage() {
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
                "Failed to load validation data:",
                error,
            );

            setError(
                "Unable to load validation data.",
            );
        } finally {
            setIsLoading(false);
        }
    }

    useEffect(() => {
        void loadDataCollections();
    }, []);

    const filteredValidations = useMemo(
        () => {
            const normalizedSearch =
                searchTerm.trim().toLowerCase();

            return dataCollections.filter((item) => {
                const matchesStatus =
                    !status
                        ? [
                              "SUBMITTED",
                              "VALIDATED",
                              "REJECTED",
                          ].includes(item.status)
                        : item.status === status;

                if (!matchesStatus) {
                    return false;
                }

                if (!normalizedSearch) {
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
                            entry.id ===
                            item.dataCollectorId,
                    );

                const searchableValues = [
                    campaign?.code,
                    campaign?.name,
                    country?.iso2Code,
                    country?.iso3Code,
                    country?.name,
                    questionnaire?.code,
                    questionnaire?.name,
                    organization?.code,
                    organization?.name,
                    person?.fullName,
                    item.status,
                ];

                return searchableValues.some(
                    (value) =>
                        value
                            ?.toLowerCase()
                            .includes(
                                normalizedSearch,
                            ),
                );
            });
        },
        [
            campaigns,
            countries,
            dataCollections,
            organizations,
            persons,
            questionnaires,
            searchTerm,
            status,
        ],
    );

    const sortedValidations = useMemo(
        () =>
            [...filteredValidations].sort(
                (a, b) => {
                    const campaignA =
                        campaigns.find(
                            (entry) =>
                                entry.id ===
                                a.campaignId,
                        );

                    const campaignB =
                        campaigns.find(
                            (entry) =>
                                entry.id ===
                                b.campaignId,
                        );

                    const countryA =
                        countries.find(
                            (entry) =>
                                entry.id ===
                                a.countryId,
                        );

                    const countryB =
                        countries.find(
                            (entry) =>
                                entry.id ===
                                b.countryId,
                        );

                    const questionnaireA =
                        questionnaires.find(
                            (entry) =>
                                entry.id ===
                                a.questionnaireId,
                        );

                    const questionnaireB =
                        questionnaires.find(
                            (entry) =>
                                entry.id ===
                                b.questionnaireId,
                        );

                    const organizationA =
                        organizations.find(
                            (entry) =>
                                entry.id ===
                                a.responsibleOrganizationId,
                        );

                    const organizationB =
                        organizations.find(
                            (entry) =>
                                entry.id ===
                                b.responsibleOrganizationId,
                        );

                    const personA =
                        persons.find(
                            (entry) =>
                                entry.id ===
                                a.dataCollectorId,
                        );

                    const personB =
                        persons.find(
                            (entry) =>
                                entry.id ===
                                b.dataCollectorId,
                        );

                    const campaignNameA =
                        campaignA
                            ? `${campaignA.code} ${campaignA.name}`
                            : "";

                    const campaignNameB =
                        campaignB
                            ? `${campaignB.code} ${campaignB.name}`
                            : "";

                    const countryNameA =
                        countryA
                            ? `${countryA.iso3Code} ${countryA.name}`
                            : "";

                    const countryNameB =
                        countryB
                            ? `${countryB.iso3Code} ${countryB.name}`
                            : "";

                    const questionnaireNameA =
                        questionnaireA
                            ? `${questionnaireA.code} ${questionnaireA.name}`
                            : "";

                    const questionnaireNameB =
                        questionnaireB
                            ? `${questionnaireB.code} ${questionnaireB.name}`
                            : "";

                    const organizationNameA =
                        organizationA
                            ? `${organizationA.code} ${organizationA.name}`
                            : "";

                    const organizationNameB =
                        organizationB
                            ? `${organizationB.code} ${organizationB.name}`
                            : "";

                    const personNameA =
                        personA?.fullName ?? "";

                    const personNameB =
                        personB?.fullName ?? "";

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

                        case "questionnaire-asc":
                            return questionnaireNameA.localeCompare(
                                questionnaireNameB,
                            );

                        case "questionnaire-desc":
                            return questionnaireNameB.localeCompare(
                                questionnaireNameA,
                            );

                        case "organization-asc":
                            return organizationNameA.localeCompare(
                                organizationNameB,
                            );

                        case "organization-desc":
                            return organizationNameB.localeCompare(
                                organizationNameA,
                            );

                        case "collector-asc":
                            return personNameA.localeCompare(
                                personNameB,
                            );

                        case "collector-desc":
                            return personNameB.localeCompare(
                                personNameA,
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
                },
            ),
        [
            campaigns,
            countries,
            filteredValidations,
            organizations,
            persons,
            questionnaires,
            sortOption,
        ],
    );

    const paginatedValidations =
        sortedValidations.slice(
            page * rowsPerPage,
            page * rowsPerPage + rowsPerPage,
        );

    useEffect(() => {
        setPage(0);
    }, [
        searchTerm,
        sortOption,
        status,
    ]);

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
            <Typography
                variant="h4"
                sx={{
                    fontWeight: 700,
                    mb: 1,
                }}
            >
                Validation
            </Typography>

            <Typography
                color="text.secondary"
                sx={{ mb: 4 }}
            >
                Review submitted, validated and rejected data
                collections.
            </Typography>

            <Stack
                direction="row"
                spacing={2}
                sx={{
                    mb: 3,
                    alignItems: "center",
                }}
            >
                <TextField
                    label="Search"
                    value={searchTerm}
                    onChange={(event) =>
                        setSearchTerm(
                            event.target.value,
                        )
                    }
                    fullWidth
                />

                <TextField
                    select
                    label="Sort by"
                    value={sortOption}
                    onChange={(event) =>
                        setSortOption(
                            event.target.value,
                        )
                    }
                    sx={{ minWidth: 220 }}
                >
                    <MenuItem value="campaign-asc">
                        Campaign A–Z
                    </MenuItem>
                    <MenuItem value="campaign-desc">
                        Campaign Z–A
                    </MenuItem>
                    <MenuItem value="country-asc">
                        Country A–Z
                    </MenuItem>
                    <MenuItem value="country-desc">
                        Country Z–A
                    </MenuItem>
                    <MenuItem value="questionnaire-asc">
                        Questionnaire A–Z
                    </MenuItem>
                    <MenuItem value="questionnaire-desc">
                        Questionnaire Z–A
                    </MenuItem>
                    <MenuItem value="organization-asc">
                        Organization A–Z
                    </MenuItem>
                    <MenuItem value="organization-desc">
                        Organization Z–A
                    </MenuItem>
                    <MenuItem value="collector-asc">
                        Data Collector A–Z
                    </MenuItem>
                    <MenuItem value="collector-desc">
                        Data Collector Z–A
                    </MenuItem>
                    <MenuItem value="status-asc">
                        Status A–Z
                    </MenuItem>
                    <MenuItem value="status-desc">
                        Status Z–A
                    </MenuItem>
                </TextField>
            </Stack>

            <Stack spacing={2}>
                {paginatedValidations.length === 0 ? (
                    <Alert severity="info">
                        No validation records found.
                    </Alert>
                ) : (
                    paginatedValidations.map((item) => {
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

                        return (
                            <Card key={item.id}>
                                <CardContent>
                                    <Stack
                                        direction={{
                                            xs: "column",
                                            md: "row",
                                        }}
                                        sx={{
                                            justifyContent:
                                                "space-between",
                                            alignItems: {
                                                xs: "stretch",
                                                md: "center",
                                            },
                                            gap: 2,
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
                                                Campaign:{" "}
                                                {campaign
                                                    ? `${campaign.code} — ${campaign.name}`
                                                    : item.campaignId}
                                            </Typography>

                                            <Typography
                                                variant="body2"
                                                color="text.secondary"
                                            >
                                                Country:{" "}
                                                {country
                                                    ? `${country.iso3Code} — ${country.name}`
                                                    : item.countryId}
                                            </Typography>

                                            <Typography
                                                variant="body2"
                                                color="text.secondary"
                                            >
                                                Questionnaire:{" "}
                                                {questionnaire
                                                    ? `${questionnaire.code} — ${questionnaire.name}`
                                                    : item.questionnaireId}
                                            </Typography>

                                            <Typography
                                                variant="body2"
                                                color="text.secondary"
                                            >
                                                Organization:{" "}
                                                {organization
                                                    ? `${organization.code} — ${organization.name}`
                                                    : item.responsibleOrganizationId}
                                            </Typography>

                                            <Typography
                                                variant="body2"
                                                color="text.secondary"
                                            >
                                                Data Collector:{" "}
                                                {person
                                                    ? person.fullName
                                                    : item.dataCollectorId}
                                            </Typography>
                                        </Box>

                                        <Stack
                                            direction="row"
                                            spacing={1}
                                            sx={{
                                                alignItems: "center",
                                                justifyContent: {
                                                    xs: "space-between",
                                                    md: "flex-end",
                                                },
                                            }}
                                        >
                                            <Chip
                                                label={item.status}
                                                color={
                                                    item.status ===
                                                    "VALIDATED"
                                                        ? "success"
                                                        : item.status ===
                                                            "REJECTED"
                                                          ? "error"
                                                          : "warning"
                                                }
                                            />

                                            <Button
                                                variant="contained"
                                                size="small"
                                                onClick={() =>
                                                    navigate(
                                                        `/validation/${item.id}`,
                                                    )
                                                }
                                            >
                                                Review
                                            </Button>
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
                count={filteredValidations.length}
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
                    50,
                ]}
            />
        </Box>
    );
}
