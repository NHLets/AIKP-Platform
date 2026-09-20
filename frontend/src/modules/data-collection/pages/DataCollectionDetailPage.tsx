import { useEffect, useState } from "react";

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
    Stack,
    Typography,
} from "@mui/material";

import {
    cancelDataCollection,
    getDataCollectionById,
    rejectDataCollection,
    startDataCollection,
    submitDataCollection,
    validateDataCollection,
} from "../api/dataCollectionApi";

import {
    getCampaigns,
    getCountries,
    getOrganizations,
    getPersons,
    getQuestionnaires,
} from "../api/dataCollectionOptionsApi";

import { getQuestionnaire } from "@/modules/questionnaire/api/questionnaireApi";
import { getQuestionnaireGroups } from "@/modules/questionnaire/api/questionnaireGroupApi";
import { getQuestionnaireVariables } from "@/modules/questionnaire/api/questionnaireVariableApi";

import type { QuestionnaireGroup } from "@/modules/questionnaire/types/questionnaireGroup.types";
import type { QuestionnaireVariable } from "@/modules/questionnaire/types/questionnaireVariable.types";

import DataEntryForm from "../components/DataEntryForm";
import SpreadsheetDataEntryForm from "../components/SpreadsheetDataEntryForm";

import type {
    CampaignOption,
    CountryOption,
    OrganizationOption,
    PersonOption,
    QuestionnaireOption,
} from "../api/dataCollectionOptionsApi";

import type {
    DataCollection,
} from "../types/dataCollection.types";

type PendingAction =
    | "VALIDATE"
    | "REJECT"
    | "CANCEL"
    | null;

export default function DataCollectionDetailPage() {
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

    const [questionnaireGroups, setQuestionnaireGroups] =
        useState<QuestionnaireGroup[]>([]);

    const [questionnaireVariables, setQuestionnaireVariables] =
        useState<QuestionnaireVariable[]>([]);

    const [questionnaireRenderType, setQuestionnaireRenderType] =
        useState<"FORM" | "SPREADSHEET" | "HYBRID" | null>(null);

    const [isQuestionnaireStructureLoading, setIsQuestionnaireStructureLoading] =
        useState(false);

    const [questionnaireStructureError, setQuestionnaireStructureError] =
        useState<string | null>(null);

    const [isLoading, setIsLoading] =
        useState(true);

    const [isActionLoading, setIsActionLoading] =
        useState(false);

    const [pendingAction, setPendingAction] =
        useState<PendingAction>(null);

    const [error, setError] =
        useState<string | null>(null);

    async function loadDataCollection() {
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
        } catch (error) {
            console.error(
                "Failed to load data collection:",
                error,
            );

            setError(
                "Unable to load data collection.",
            );
        } finally {
            setIsLoading(false);
        }
    }

    useEffect(() => {
        void loadDataCollection();
    }, [id]);

    useEffect(() => {
        async function loadQuestionnaireStructure() {
            if (!dataCollection?.questionnaireId) {
                setQuestionnaireGroups([]);
                setQuestionnaireVariables([]);
                setQuestionnaireRenderType(null);
                setQuestionnaireStructureError(null);
                return;
            }

            try {
                setIsQuestionnaireStructureLoading(true);
                setQuestionnaireStructureError(null);

                const [questionnaire, groups, variables] =
                    await Promise.all([
                        getQuestionnaire(
                            dataCollection.questionnaireId,
                        ),
                        getQuestionnaireGroups(
                            dataCollection.questionnaireId,
                        ),
                        getQuestionnaireVariables(
                            dataCollection.questionnaireId,
                        ),
                    ]);

                setQuestionnaireRenderType(
                    questionnaire.renderType,
                );
                setQuestionnaireGroups(groups);
                setQuestionnaireVariables(variables);
            } catch (error) {
                console.error(
                    "Failed to load questionnaire structure:",
                    error,
                );

                setQuestionnaireStructureError(
                    "Unable to load questionnaire groups and variables.",
                );
            } finally {
                setIsQuestionnaireStructureLoading(false);
            }
        }

        void loadQuestionnaireStructure();
    }, [dataCollection?.questionnaireId]);

    async function handleAction(
        action: (
            dataCollectionId: string,
        ) => Promise<void>,
    ) {
        if (!id) {
            return;
        }

        try {
            setIsActionLoading(true);
            setError(null);

            await action(id);

            await loadDataCollection();
        } catch (error) {
            console.error(
                "Failed to update data collection:",
                error,
            );

            setError(
                "Unable to update data collection.",
            );
        } finally {
            setIsActionLoading(false);
        }
    }

    function getActionDetails(
        action: Exclude<PendingAction, null>,
    ) {
        switch (action) {
            case "VALIDATE":
                return {
                    title: "Validate Data Collection",
                    message:
                        "Are you sure you want to validate this data collection?",
                    confirmLabel: "Validate",
                    handler: validateDataCollection,
                };

            case "REJECT":
                return {
                    title: "Reject Data Collection",
                    message:
                        "Are you sure you want to reject this data collection?",
                    confirmLabel: "Reject",
                    handler: rejectDataCollection,
                };

            case "CANCEL":
                return {
                    title: "Cancel Data Collection",
                    message:
                        "Are you sure you want to cancel this data collection?",
                    confirmLabel: "Cancel Collection",
                    handler: cancelDataCollection,
                };
        }
    }

    async function handleConfirmAction() {
        if (!pendingAction) {
            return;
        }

        const { handler } =
            getActionDetails(pendingAction);

        await handleAction(handler);

        setPendingAction(null);
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

    if (error || !dataCollection) {
        return (
            <Alert severity="error">
                {error ?? "Data Collection not found."}
            </Alert>
        );
    }

    const item = dataCollection;

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

    const operatorOrganization =
        organizations.find(
            (entry) =>
                entry.id ===
                item.operatorOrganizationId,
        );

    const person =
        persons.find(
            (entry) =>
                entry.id === item.dataCollectorId,
        );

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
                    textAlign: "left",
                }}
            >
                Data Collection Details
            </Typography>

            <Typography
                color="text.secondary"
                sx={{
                    mb: 4,
                    textAlign: "left",
                }}
            >
                Detailed information and workflow actions.
            </Typography>

            <Card>
                <CardContent sx={{ textAlign: "left" }}>
                    <Stack spacing={3}>
                        <Stack
                            direction="row"
                            sx={{
                                justifyContent:
                                    "space-between",
                                alignItems:
                                    "center",
                            }}
                        >
                            <Typography
                                variant="h6"
                                sx={{
                                    fontWeight: 600,
                                }}
                            >
                                Status
                            </Typography>

                            <Chip
                                label={item.status}
                                color={
                                    item.status ===
                                    "VALIDATED"
                                        ? "success"
                                        : item.status ===
                                          "REJECTED"
                                        ? "error"
                                        : item.status ===
                                          "SUBMITTED"
                                        ? "primary"
                                        : item.status ===
                                          "IN_PROGRESS"
                                        ? "warning"
                                        : "default"
                                }
                            />
                        </Stack>

                        {[
                            "DRAFT",
                            "IN_PROGRESS",
                            "SUBMITTED",
                            "REJECTED",
                        ].includes(item.status) && (
                            <Button
                                variant="outlined"
                                color="error"
                                disabled={isActionLoading}
                                onClick={() =>
                                    setPendingAction(
                                        "CANCEL",
                                    )
                                }
                            >
                                Cancel
                            </Button>
                        )}

                        {item.status === "DRAFT" && (
                            <Button
                                variant="contained"
                                disabled={
                                    isActionLoading
                                }
                                onClick={() =>
                                    void handleAction(
                                        startDataCollection,
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
                                disabled={
                                    isActionLoading
                                }
                                onClick={() =>
                                    void handleAction(
                                        submitDataCollection,
                                    )
                                }
                            >
                                Submit
                            </Button>
                        )}

                        {item.status ===
                            "SUBMITTED" && (
                            <Stack
                                direction="row"
                                spacing={2}
                            >
                                <Button
                                    variant="contained"
                                    disabled={
                                        isActionLoading
                                    }
                                    onClick={() =>
                                        setPendingAction(
                                            "VALIDATE",
                                        )
                                    }
                                >
                                    Validate
                                </Button>

                                <Button
                                    variant="outlined"
                                    color="error"
                                    disabled={
                                        isActionLoading
                                    }
                                    onClick={() =>
                                        setPendingAction(
                                            "REJECT",
                                        )
                                    }
                                >
                                    Reject
                                </Button>
                            </Stack>
                        )}

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
                                    : item.campaignId}
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
                                    : item.countryId}
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
                                    : item.questionnaireId}
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
                                    : item.responsibleOrganizationId}
                            </Typography>
                        </Box>

                        <Box>
                            <Typography
                                variant="body2"
                                color="text.secondary"
                            >
                                Operator Organization
                            </Typography>

                            <Typography>
                                {operatorOrganization
                                    ? `${operatorOrganization.code} — ${operatorOrganization.name}`
                                    : item.operatorOrganizationId}
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
                                    : item.dataCollectorId}
                            </Typography>
                        </Box>

                        <Box>
                            <Typography
                                variant="body2"
                                color="text.secondary"
                            >
                                Data Collection ID
                            </Typography>

                            <Typography>
                                {item.id}
                            </Typography>
                        </Box>
                    </Stack>
                </CardContent>
            </Card>

            {isQuestionnaireStructureLoading && (
                <Card>
                    <CardContent>
                        <Box
                            sx={{
                                display: "flex",
                                justifyContent: "center",
                                py: 4,
                            }}
                        >
                            <CircularProgress />
                        </Box>
                    </CardContent>
                </Card>
            )}

            {questionnaireStructureError && (
                <Alert severity="error">
                    {questionnaireStructureError}
                </Alert>
            )}

            {!isQuestionnaireStructureLoading &&
                !questionnaireStructureError &&
                dataCollection &&
                questionnaireRenderType === "SPREADSHEET" && (
                    <SpreadsheetDataEntryForm
                        dataCollectionId={dataCollection.id}
                        groups={questionnaireGroups}
                        variables={questionnaireVariables}
                    />
                )}

            {!isQuestionnaireStructureLoading &&
                !questionnaireStructureError &&
                dataCollection &&
                questionnaireRenderType !== "SPREADSHEET" && (
                    <DataEntryForm
                        dataCollectionId={dataCollection.id}
                        groups={questionnaireGroups}
                        variables={questionnaireVariables}
                    />
                )}

            <Dialog
                open={pendingAction !== null}
                onClose={() => {
                    if (!isActionLoading) {
                        setPendingAction(null);
                    }
                }}
            >
                {pendingAction && (
                    <>
                        <DialogTitle>
                            {
                                getActionDetails(
                                    pendingAction,
                                ).title
                            }
                        </DialogTitle>

                        <DialogContent>
                            <DialogContentText>
                                {
                                    getActionDetails(
                                        pendingAction,
                                    ).message
                                }
                            </DialogContentText>
                        </DialogContent>

                        <DialogActions>
                            <Button
                                disabled={
                                    isActionLoading
                                }
                                onClick={() =>
                                    setPendingAction(
                                        null,
                                    )
                                }
                            >
                                Back
                            </Button>

                            <Button
                                variant="contained"
                                color={
                                    pendingAction ===
                                    "VALIDATE"
                                        ? "primary"
                                        : "error"
                                }
                                disabled={
                                    isActionLoading
                                }
                                onClick={() =>
                                    void handleConfirmAction()
                                }
                            >
                                {
                                    getActionDetails(
                                        pendingAction,
                                    ).confirmLabel
                                }
                            </Button>
                        </DialogActions>
                    </>
                )}
            </Dialog>
        </Box>
    );
}
