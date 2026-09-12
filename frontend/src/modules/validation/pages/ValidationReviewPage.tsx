import { useEffect, useMemo, useState } from "react";

import {
    useNavigate,
    useParams,
} from "react-router-dom";

import {
    Alert,
    Box,
    Button,
    Card,
    CardContent,
    Chip,
    CircularProgress,
    Dialog,
    DialogActions,
    DialogContent,
    DialogContentText,
    DialogTitle,
    Divider,
    Stack,
    Typography,
} from "@mui/material";

import {
    getDataCollectionById,
    rejectDataCollection,
    validateDataCollection,
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

import {
    getDataCollectionObservations,
} from "@/modules/data-collection/api/observationApi";

import type {
    DataCollection,
} from "@/modules/data-collection/types/dataCollection.types";

import type {
    DataCollectionObservation,
} from "@/modules/data-collection/types/observation.types";

import {
    getQuestionnaireGroups,
} from "@/modules/questionnaire/api/questionnaireGroupApi";

import {
    getQuestionnaireVariables,
} from "@/modules/questionnaire/api/questionnaireVariableApi";

import type {
    QuestionnaireGroup,
} from "@/modules/questionnaire/types/questionnaireGroup.types";

import type {
    QuestionnaireVariable,
} from "@/modules/questionnaire/types/questionnaireVariable.types";

type PendingDecision =
    | "VALIDATE"
    | "REJECT"
    | null;

function formatObservationValue(
    observation: DataCollectionObservation,
): string {
    switch (observation.status) {
        case "NOT_AVAILABLE":
            return "Not available";

        case "NOT_APPLICABLE":
            return "Not applicable";

        case "PROVIDED":
            if (observation.numericValue !== null) {
                return observation.numericValue;
            }

            if (observation.textValue !== null) {
                return observation.textValue;
            }

            if (observation.booleanValue !== null) {
                return observation.booleanValue ? "Yes" : "No";
            }

            if (observation.dateValue !== null) {
                return observation.dateValue;
            }

            return "—";

        default:
            return "—";
    }
}

function getStatusColor(
    status: DataCollectionObservation["status"],
): "success" | "warning" | "default" {
    switch (status) {
        case "PROVIDED":
            return "success";
        case "NOT_AVAILABLE":
            return "warning";
        case "NOT_APPLICABLE":
            return "default";
    }
}

export default function ValidationReviewPage() {
    const { id } = useParams();
    const navigate = useNavigate();

    const [dataCollection, setDataCollection] =
        useState<DataCollection | null>(null);

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

    const [groups, setGroups] =
        useState<QuestionnaireGroup[]>([]);

    const [variables, setVariables] =
        useState<QuestionnaireVariable[]>([]);

    const [observations, setObservations] =
        useState<DataCollectionObservation[]>([]);

    const [isLoading, setIsLoading] =
        useState(true);

    const [isActionLoading, setIsActionLoading] =
        useState(false);

    const [pendingDecision, setPendingDecision] =
        useState<PendingDecision>(null);

    const [error, setError] =
        useState<string | null>(null);

    async function loadReview() {
        if (!id) {
            setError("Data Collection ID is missing.");
            setIsLoading(false);
            return;
        }

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
                getDataCollectionById(id),
                getCampaigns(),
                getCountries(),
                getQuestionnaires(),
                getOrganizations(),
                getPersons(),
            ]);

            setDataCollection(data);
            setCampaigns(campaignData);
            setCountries(countryData);
            setQuestionnaires(questionnaireData);
            setOrganizations(organizationData);
            setPersons(personData);

            const [
                groupData,
                variableData,
                observationData,
            ] = await Promise.all([
                getQuestionnaireGroups(data.questionnaireId),
                getQuestionnaireVariables(data.questionnaireId),
                getDataCollectionObservations(data.id),
            ]);

            setGroups(groupData);
            setVariables(variableData);
            setObservations(observationData);
        } catch (error) {
            console.error(
                "Failed to load validation review:",
                error,
            );

            setError(
                "Unable to load validation review.",
            );
        } finally {
            setIsLoading(false);
        }
    }

    useEffect(() => {
        void loadReview();
    }, [id]);

    async function handleDecision(
        decision: Exclude<PendingDecision, null>,
    ) {
        if (!id) {
            return;
        }

        try {
            setIsActionLoading(true);
            setError(null);

            if (decision === "VALIDATE") {
                await validateDataCollection(id);
            } else {
                await rejectDataCollection(id);
            }

            navigate("/validation");
        } catch (error) {
            console.error(
                "Failed to update validation decision:",
                error,
            );

            setError(
                decision === "VALIDATE"
                    ? "Unable to validate data collection."
                    : "Unable to reject data collection.",
            );
        } finally {
            setIsActionLoading(false);
            setPendingDecision(null);
        }
    }

    const campaign =
        dataCollection
            ? campaigns.find(
                  (entry) =>
                      entry.id === dataCollection.campaignId,
              )
            : undefined;

    const country =
        dataCollection
            ? countries.find(
                  (entry) =>
                      entry.id === dataCollection.countryId,
              )
            : undefined;

    const questionnaire =
        dataCollection
            ? questionnaires.find(
                  (entry) =>
                      entry.id === dataCollection.questionnaireId,
              )
            : undefined;

    const organization =
        dataCollection
            ? organizations.find(
                  (entry) =>
                      entry.id ===
                      dataCollection.responsibleOrganizationId,
              )
            : undefined;

    const person =
        dataCollection
            ? persons.find(
                  (entry) =>
                      entry.id === dataCollection.dataCollectorId,
              )
            : undefined;

    const observationsByVariable = useMemo(() => {
        const map = new Map<
            string,
            DataCollectionObservation[]
        >();

        for (const observation of observations) {
            const existing =
                map.get(observation.questionnaireVariableId) ?? [];

            existing.push(observation);
            map.set(
                observation.questionnaireVariableId,
                existing,
            );
        }

        for (const entries of map.values()) {
            entries.sort(
                (a, b) =>
                    b.referenceYear - a.referenceYear,
            );
        }

        return map;
    }, [observations]);

    const sortedGroups = useMemo(
        () =>
            [...groups].sort(
                (a, b) =>
                    a.displayOrder - b.displayOrder,
            ),
        [groups],
    );

    const sortedVariables = useMemo(
        () =>
            [...variables].sort(
                (a, b) =>
                    a.displayOrder - b.displayOrder,
            ),
        [variables],
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

    if (error || !dataCollection) {
        return (
            <Box>
                <Button
                    onClick={() => navigate("/validation")}
                    sx={{ mb: 3 }}
                >
                    Back to Validation
                </Button>

                <Alert severity="error">
                    {error ?? "Data Collection not found."}
                </Alert>
            </Box>
        );
    }

    return (
        <Box>
            <Button
                onClick={() => navigate("/validation")}
                sx={{ mb: 3 }}
            >
                Back to Validation
            </Button>

            <Typography
                variant="h4"
                sx={{
                    fontWeight: 700,
                    mb: 1,
                }}
            >
                Validation Review
            </Typography>

            <Typography
                color="text.secondary"
                sx={{ mb: 4 }}
            >
                Review the submitted data collection before making
                a validation decision.
            </Typography>

            {error && (
                <Alert
                    severity="error"
                    sx={{ mb: 3 }}
                >
                    {error}
                </Alert>
            )}

            <Card sx={{ mb: 3 }}>
                <CardContent>
                    <Stack spacing={2}>
                        <Stack
                            direction="row"
                            sx={{
                                justifyContent: "space-between",
                                alignItems: "center",
                            }}
                        >
                            <Typography
                                variant="h6"
                                sx={{ fontWeight: 600 }}
                            >
                                Data Collection
                            </Typography>

                            <Chip
                                label={dataCollection.status}
                                color={
                                    dataCollection.status === "VALIDATED"
                                        ? "success"
                                        : dataCollection.status === "REJECTED"
                                          ? "error"
                                          : dataCollection.status === "SUBMITTED"
                                            ? "primary"
                                            : "default"
                                }
                            />
                        </Stack>

                        <Divider />

                        <Box>
                            <Typography
                                variant="body2"
                                color="text.secondary"
                            >
                                Campaign
                            </Typography>
                            <Typography>
                                {campaign
                                    ? `${campaign.code} — ${campaign.name}`
                                    : dataCollection.campaignId}
                            </Typography>
                        </Box>

                        <Box>
                            <Typography
                                variant="body2"
                                color="text.secondary"
                            >
                                Country
                            </Typography>
                            <Typography>
                                {country
                                    ? `${country.iso3Code} — ${country.name}`
                                    : dataCollection.countryId}
                            </Typography>
                        </Box>

                        <Box>
                            <Typography
                                variant="body2"
                                color="text.secondary"
                            >
                                Questionnaire
                            </Typography>
                            <Typography>
                                {questionnaire
                                    ? `${questionnaire.code} — ${questionnaire.name}`
                                    : dataCollection.questionnaireId}
                            </Typography>
                        </Box>

                        <Box>
                            <Typography
                                variant="body2"
                                color="text.secondary"
                            >
                                Responsible Organization
                            </Typography>
                            <Typography>
                                {organization
                                    ? `${organization.code} — ${organization.name}`
                                    : dataCollection.responsibleOrganizationId}
                            </Typography>
                        </Box>

                        <Box>
                            <Typography
                                variant="body2"
                                color="text.secondary"
                            >
                                Data Collector
                            </Typography>
                            <Typography>
                                {person
                                    ? person.fullName
                                    : dataCollection.dataCollectorId}
                            </Typography>
                        </Box>
                    </Stack>
                </CardContent>
            </Card>

            <Card sx={{ mb: 3 }}>
                <CardContent>
                    <Typography
                        variant="h6"
                        sx={{
                            fontWeight: 600,
                            mb: 1,
                        }}
                    >
                        Questionnaire Responses
                    </Typography>

                    <Typography
                        variant="body2"
                        color="text.secondary"
                        sx={{ mb: 3 }}
                    >
                        Submitted observations are displayed for
                        review and cannot be edited here.
                    </Typography>

                    {sortedGroups.map((group) => {
                        const groupVariables =
                            sortedVariables.filter(
                                (variable) =>
                                    variable.questionnaireGroupId ===
                                    group.id,
                            );

                        if (groupVariables.length === 0) {
                            return null;
                        }

                        return (
                            <Box
                                key={group.id}
                                sx={{ mb: 4 }}
                            >
                                <Typography
                                    variant="subtitle1"
                                    sx={{
                                        fontWeight: 700,
                                        mb: 2,
                                    }}
                                >
                                    {group.code} — {group.name}
                                </Typography>

                                <Stack spacing={2}>
                                    {groupVariables.map(
                                        (variable) => {
                                            const variableObservations =
                                                observationsByVariable.get(
                                                    variable.id,
                                                ) ?? [];

                                            return (
                                                <Card
                                                    key={variable.id}
                                                    variant="outlined"
                                                >
                                                    <CardContent>
                                                        <Stack
                                                            spacing={1.5}
                                                        >
                                                            <Stack
                                                                direction="row"
                                                                sx={{
                                                                    justifyContent:
                                                                        "space-between",
                                                                    gap: 2,
                                                                }}
                                                            >
                                                                <Box>
                                                                    <Typography
                                                                        sx={{
                                                                            fontWeight: 600,
                                                                        }}
                                                                    >
                                                                        {
                                                                            variable.seriesCode
                                                                        }{" "}
                                                                        —{" "}
                                                                        {
                                                                            variable.name
                                                                        }
                                                                    </Typography>

                                                                    {variable.definition && (
                                                                        <Typography
                                                                            variant="body2"
                                                                            color="text.secondary"
                                                                            sx={{
                                                                                mt: 0.5,
                                                                            }}
                                                                        >
                                                                            {
                                                                                variable.definition
                                                                            }
                                                                        </Typography>
                                                                    )}
                                                                </Box>

                                                                <Chip
                                                                    label={
                                                                        variable.dataType
                                                                    }
                                                                    size="small"
                                                                />
                                                            </Stack>

                                                            {variableObservations.length ===
                                                            0 ? (
                                                                <Typography
                                                                    variant="body2"
                                                                    color="text.secondary"
                                                                >
                                                                    No observation
                                                                    recorded.
                                                                </Typography>
                                                            ) : (
                                                                <Stack
                                                                    spacing={
                                                                        1
                                                                    }
                                                                >
                                                                    {variableObservations.map(
                                                                        (
                                                                            observation,
                                                                        ) => (
                                                                            <Box
                                                                                key={
                                                                                    observation.id
                                                                                }
                                                                                sx={{
                                                                                    p: 1.5,
                                                                                    borderRadius: 1,
                                                                                    bgcolor:
                                                                                        "action.hover",
                                                                                }}
                                                                            >
                                                                                <Stack
                                                                                    direction={{
                                                                                        xs: "column",
                                                                                        md: "row",
                                                                                    }}
                                                                                    spacing={
                                                                                        2
                                                                                    }
                                                                                    sx={{
                                                                                        alignItems:
                                                                                            {
                                                                                                md: "center",
                                                                                            },
                                                                                    }}
                                                                                >
                                                                                    <Typography
                                                                                        sx={{
                                                                                            minWidth:
                                                                                                {
                                                                                                    md: 100,
                                                                                                },
                                                                                            fontWeight: 600,
                                                                                        }}
                                                                                    >
                                                                                        {
                                                                                            observation.referenceYear
                                                                                        }
                                                                                    </Typography>

                                                                                    <Typography
                                                                                        sx={{
                                                                                            flex: 1,
                                                                                        }}
                                                                                    >
                                                                                        {formatObservationValue(
                                                                                            observation,
                                                                                        )}
                                                                                    </Typography>

                                                                                    {observation.selectedUnit && (
                                                                                        <Typography
                                                                                            variant="body2"
                                                                                            color="text.secondary"
                                                                                        >
                                                                                            {
                                                                                                observation.selectedUnit
                                                                                            }
                                                                                        </Typography>
                                                                                    )}

                                                                                    <Chip
                                                                                        label={
                                                                                            observation.status
                                                                                        }
                                                                                        size="small"
                                                                                        color={getStatusColor(
                                                                                            observation.status,
                                                                                        )}
                                                                                    />
                                                                                </Stack>

                                                                                {observation.comment && (
                                                                                    <Typography
                                                                                        variant="body2"
                                                                                        color="text.secondary"
                                                                                        sx={{
                                                                                            mt: 1,
                                                                                        }}
                                                                                    >
                                                                                        Comment:{" "}
                                                                                        {
                                                                                            observation.comment
                                                                                        }
                                                                                    </Typography>
                                                                                )}
                                                                            </Box>
                                                                        ),
                                                                    )}
                                                                </Stack>
                                                            )}
                                                        </Stack>
                                                    </CardContent>
                                                </Card>
                                            );
                                        },
                                    )}
                                </Stack>
                            </Box>
                        );
                    })}

                    {sortedGroups.length === 0 && (
                        <Alert severity="info">
                            No questionnaire groups were found.
                        </Alert>
                    )}

                    {observations.length === 0 && (
                        <Alert
                            severity="warning"
                            sx={{ mt: 2 }}
                        >
                            No observations were found for this data
                            collection.
                        </Alert>
                    )}
                </CardContent>
            </Card>

            {dataCollection.status === "SUBMITTED" && (
                <Card>
                    <CardContent>
                        <Stack spacing={2}>
                            <Typography
                                variant="h6"
                                sx={{ fontWeight: 600 }}
                            >
                                Validation Decision
                            </Typography>

                            <Typography
                                variant="body2"
                                color="text.secondary"
                            >
                                After reviewing the submitted
                                observations, choose whether to
                                validate or reject this data
                                collection.
                            </Typography>

                            <Stack
                                direction="row"
                                spacing={2}
                            >
                                <Button
                                    variant="contained"
                                    disabled={isActionLoading}
                                    onClick={() =>
                                        setPendingDecision(
                                            "VALIDATE",
                                        )
                                    }
                                >
                                    Validate
                                </Button>

                                <Button
                                    variant="outlined"
                                    color="error"
                                    disabled={isActionLoading}
                                    onClick={() =>
                                        setPendingDecision(
                                            "REJECT",
                                        )
                                    }
                                >
                                    Reject
                                </Button>
                            </Stack>
                        </Stack>
                    </CardContent>
                </Card>
            )}

            <Dialog
                open={pendingDecision !== null}
                onClose={() => {
                    if (!isActionLoading) {
                        setPendingDecision(null);
                    }
                }}
            >
                <DialogTitle>
                    {pendingDecision === "VALIDATE"
                        ? "Validate Data Collection"
                        : "Reject Data Collection"}
                </DialogTitle>

                <DialogContent>
                    <DialogContentText>
                        {pendingDecision === "VALIDATE"
                            ? "Are you sure you want to validate this data collection?"
                            : "Are you sure you want to reject this data collection?"}
                    </DialogContentText>
                </DialogContent>

                <DialogActions>
                    <Button
                        disabled={isActionLoading}
                        onClick={() =>
                            setPendingDecision(null)
                        }
                    >
                        Back
                    </Button>

                    <Button
                        variant="contained"
                        color={
                            pendingDecision === "VALIDATE"
                                ? "primary"
                                : "error"
                        }
                        disabled={isActionLoading}
                        onClick={() => {
                            if (pendingDecision) {
                                void handleDecision(
                                    pendingDecision,
                                );
                            }
                        }}
                    >
                        {pendingDecision === "VALIDATE"
                            ? "Validate"
                            : "Reject"}
                    </Button>
                </DialogActions>
            </Dialog>
        </Box>
    );
}
