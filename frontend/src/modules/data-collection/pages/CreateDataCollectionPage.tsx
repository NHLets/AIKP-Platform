import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import {
    Alert,
    Box,
    Button,
    Card,
    CardContent,
    CircularProgress,
    MenuItem,
    Stack,
    TextField,
    Typography,
} from "@mui/material";

import {
    createDataCollection,
} from "../api/dataCollectionApi";

import {
    getCampaigns,
    getCountries,
    getCountriesByCampaign,
    getOrganizations,
    getPersons,
    getQuestionnaires,
    type CampaignOption,
    type CountryOption,
    type OrganizationOption,
    type PersonOption,
    type QuestionnaireOption,
} from "../api/dataCollectionOptionsApi";

export default function CreateDataCollectionPage() {
    const navigate = useNavigate();

    const [campaignId, setCampaignId] =
        useState("");

    const [countryId, setCountryId] =
        useState("");

    const [questionnaireId, setQuestionnaireId] =
        useState("");

    const [
        responsibleOrganizationId,
        setResponsibleOrganizationId,
    ] = useState("");

    const [
        operatorOrganizationId,
        setOperatorOrganizationId,
    ] = useState("");

    const [dataCollectorId, setDataCollectorId] =
        useState("");

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

    const [isLoading, setIsLoading] =
        useState(true);

    const [isSubmitting, setIsSubmitting] =
        useState(false);

    const [error, setError] =
        useState<string | null>(null);

    useEffect(() => {
        async function loadOptions() {
            try {
                setIsLoading(true);
                setError(null);

                const [
                    campaignData,
                    countryData,
                    questionnaireData,
                    organizationData,
                    personData,
                ] = await Promise.all([
                    getCampaigns(),
                    getCountries(),
                    getQuestionnaires(),
                    getOrganizations(),
                    getPersons(),
                ]);

                setCampaigns(
                    campaignData.filter(
                        (item) => item.active,
                    ),
                );

                setCountries(
                    countryData.filter(
                        (item) => item.active,
                    ),
                );

                setQuestionnaires(
                    questionnaireData.filter(
                        (item) => item.active,
                    ),
                );

                setOrganizations(
                    organizationData.filter(
                        (item) => item.active,
                    ),
                );

                setPersons(
                    personData.filter(
                        (item) => item.active,
                    ),
                );
            } catch (error) {
                console.error(
                    "Failed to load form options:",
                    error,
                );

                setError(
                    "Unable to load data collection options.",
                );
            } finally {
                setIsLoading(false);
            }
        }

        void loadOptions();
    }, []);

    useEffect(() => {
        async function loadCampaignCountries() {
            if (!campaignId) {
                return;
            }

            try {
                const data =
                    await getCountriesByCampaign(
                        campaignId,
                    );

                const campaignCountryIds =
                    new Set(
                        data.map(
                            (item) => item.countryId,
                        ),
                    );

                setCountries((currentCountries) =>
                    currentCountries.filter(
                        (country) =>
                            country.active &&
                            campaignCountryIds.has(
                                country.id,
                            ),
                    ),
                );
            } catch (error) {
                console.error(
                    "Failed to load campaign countries:",
                    error,
                );

                setError(
                    "Unable to load campaign countries.",
                );
            }
        }

        void loadCampaignCountries();
    }, [campaignId]);

    const filteredOrganizations =
        organizations.filter(
            (item) => item.countryId === countryId,
        );

    const filteredPersons =
        persons.filter(
            (item) =>
                item.organizationId ===
                responsibleOrganizationId,
        );

    async function handleSubmit(
        event: React.FormEvent<HTMLFormElement>,
    ) {
        event.preventDefault();

        try {
            setIsSubmitting(true);
            setError(null);

            const dataCollection =
                await createDataCollection({
                    campaignId,
                    countryId,
                    questionnaireId,
                    responsibleOrganizationId,
                    operatorOrganizationId,
                    dataCollectorId,
                });

            navigate(
                `/data-collections/${dataCollection.id}`,
            );
        } catch (error) {
            console.error(
                "Failed to create data collection:",
                error,
            );

            setError(
                "Unable to create data collection.",
            );
        } finally {
            setIsSubmitting(false);
        }
    }

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

    return (
        <Box>
            <Button
                onClick={() => navigate(-1)}
                sx={{
                    mb: 3,
                }}
            >
                Back
            </Button>

            <Typography
                variant="h4"
                sx={{
                    fontWeight: 700,
                    mb: 1,
                }}
            >
                Create Data Collection
            </Typography>

            <Typography
                color="text.secondary"
                sx={{
                    mb: 4,
                }}
            >
                Select the resources for the new data collection.
            </Typography>

            <Card>
                <CardContent>
                    <Box
                        component="form"
                        onSubmit={handleSubmit}
                    >
                        <Stack spacing={3}>
                            {error && (
                                <Alert severity="error">
                                    {error}
                                </Alert>
                            )}

                            <TextField
                                select
                                label="Campaign"
                                value={campaignId}
                                onChange={(event) => {
                                    setCampaignId(
                                        event.target.value,
                                    );
                                    setCountryId("");
                                    setResponsibleOrganizationId("");
                                    setDataCollectorId("");
                                }}
                                required
                                fullWidth
                            >
                                {campaigns.map((item) => (
                                    <MenuItem
                                        key={item.id}
                                        value={item.id}
                                    >
                                        {item.code} — {item.name}
                                    </MenuItem>
                                ))}
                            </TextField>

                            <TextField
                                select
                                label="Country"
                                value={countryId}
                                onChange={(event) => {
                                    setCountryId(
                                        event.target.value,
                                    );
                                    setResponsibleOrganizationId("");
                                    setOperatorOrganizationId("");
                                    setDataCollectorId("");
                                }}
                                required
                                disabled={!campaignId}
                                fullWidth
                            >
                                {countries.map((item) => (
                                    <MenuItem
                                        key={item.id}
                                        value={item.id}
                                    >
                                        {item.iso3Code} — {item.name}
                                    </MenuItem>
                                ))}
                            </TextField>

                            <TextField
                                select
                                label="Questionnaire"
                                value={questionnaireId}
                                onChange={(event) =>
                                    setQuestionnaireId(
                                        event.target.value,
                                    )
                                }
                                required
                                fullWidth
                            >
                                {questionnaires.map((item) => (
                                    <MenuItem
                                        key={item.id}
                                        value={item.id}
                                    >
                                        {item.code} — {item.name}
                                    </MenuItem>
                                ))}
                            </TextField>

                            <TextField
                                select
                                label="Responsible Organization"
                                value={
                                    responsibleOrganizationId
                                }
                                onChange={(event) => {
                                    setResponsibleOrganizationId(
                                        event.target.value,
                                    );
                                    setDataCollectorId("");
                                }}
                                required
                                fullWidth
                            >
                                {filteredOrganizations.map((item) => (
                                    <MenuItem
                                        key={item.id}
                                        value={item.id}
                                    >
                                        {item.code} — {item.name}
                                    </MenuItem>
                                ))}
                            </TextField>

                            <TextField
                                select
                                label="Operator Organization"
                                value={operatorOrganizationId}
                                onChange={(event) =>
                                    setOperatorOrganizationId(
                                        event.target.value,
                                    )
                                }
                                required
                                fullWidth
                            >
                                {filteredOrganizations.map((item) => (
                                    <MenuItem
                                        key={item.id}
                                        value={item.id}
                                    >
                                        {item.code} — {item.name}
                                    </MenuItem>
                                ))}
                            </TextField>

                            <TextField
                                select
                                label="Data Collector"
                                value={dataCollectorId}
                                onChange={(event) =>
                                    setDataCollectorId(
                                        event.target.value,
                                    )
                                }
                                required
                                fullWidth
                            >
                                {filteredPersons.map((item) => (
                                    <MenuItem
                                        key={item.id}
                                        value={item.id}
                                    >
                                        {item.fullName}
                                    </MenuItem>
                                ))}
                            </TextField>

                            <Stack
                                direction="row"
                                spacing={2}
                                sx={{
                                    justifyContent: "flex-end",
                                }}
                            >
                                <Button
                                    variant="outlined"
                                    onClick={() =>
                                        navigate(-1)
                                    }
                                    disabled={isSubmitting}
                                >
                                    Cancel
                                </Button>

                                <Button
                                    type="submit"
                                    variant="contained"
                                    disabled={isSubmitting}
                                >
                                    {isSubmitting
                                        ? "Creating..."
                                        : "Create Data Collection"}
                                </Button>
                            </Stack>
                        </Stack>
                    </Box>
                </CardContent>
            </Card>
        </Box>
    );
}
