import { useCallback, useEffect, useMemo, useState } from "react";

import {
    Alert,
    Box,
    Button,
    CircularProgress,
    FormControl,
    FormControlLabel,
    FormLabel,
    Grid,
    MenuItem,
    Paper,
    Radio,
    RadioGroup,
    Stack,
    TextField,
    Typography,
} from "@mui/material";

import {
    createDataCollectionObservation,
    createNotApplicableObservation,
    createNotAvailableObservation,
    deleteDataCollectionObservation,
    getDataCollectionObservations,
    updateDataCollectionObservation,
} from "../api/observationApi";

import type {
    DataCollectionObservation,
    ObservationStatus,
} from "../types/observation.types";

import type { QuestionnaireGroup } from "@/modules/questionnaire/types/questionnaireGroup.types";
import type { QuestionnaireVariable } from "@/modules/questionnaire/types/questionnaireVariable.types";

interface DataEntryFormProps {
    dataCollectionId: string;
    groups: QuestionnaireGroup[];
    variables: QuestionnaireVariable[];
}

interface ObservationDraft {
    status: ObservationStatus;
    value: string;
    comment: string;
}


function createEmptyDraft(): ObservationDraft {
    return {
        status: "PROVIDED",
        value: "",
        comment: "",
    };
}

function observationToDraft(
    observation: DataCollectionObservation,
): ObservationDraft {
    let value = "";

    if (observation.numericValue !== null) {
        value = observation.numericValue;
    } else if (observation.textValue !== null) {
        value = observation.textValue;
    } else if (observation.booleanValue !== null) {
        value = String(observation.booleanValue);
    } else if (observation.dateValue !== null) {
        value = observation.dateValue;
    }

    return {
        status: observation.status,
        value,
        comment: observation.comment ?? "",
    };
}

function getValueType(
    dataType: QuestionnaireVariable["dataType"],
): "numeric" | "text" | "boolean" | "date" {
    switch (dataType) {
        case "NUMBER":
        case "INTEGER":
        case "DECIMAL":
        case "PERCENTAGE":
            return "numeric";
        case "BOOLEAN":
            return "boolean";
        case "DATE":
            return "date";
        case "TEXT":
        default:
            return "text";
    }
}

export default function DataEntryForm({
    dataCollectionId,
    groups,
    variables,
}: DataEntryFormProps) {
    const [referenceYear, setReferenceYear] =
        useState(new Date().getFullYear());

    const [observations, setObservations] = useState<
        Map<string, DataCollectionObservation>
    >(new Map());

    const [drafts, setDrafts] = useState<
        Map<string, ObservationDraft>
    >(new Map());

    const [isLoading, setIsLoading] = useState(true);
    const [savingVariableId, setSavingVariableId] =
        useState<string | null>(null);
    const [error, setError] = useState<string | null>(null);
    const [success, setSuccess] = useState<string | null>(null);

    const activeVariables = useMemo(
        () =>
            variables
                .filter((variable) => variable.active)
                .sort(
                    (a, b) =>
                        a.displayOrder - b.displayOrder,
                ),
        [variables],
    );

    const groupedVariables = useMemo(() => {
        const result = new Map<
            string | null,
            QuestionnaireVariable[]
        >();

        for (const variable of activeVariables) {
            const groupId =
                variable.questionnaireGroupId;

            const existing = result.get(groupId) ?? [];
            existing.push(variable);
            result.set(groupId, existing);
        }

        return result;
    }, [activeVariables]);

    const loadObservations = useCallback(async () => {
        try {
            setIsLoading(true);
            setError(null);
            setSuccess(null);

            const data =
                await getDataCollectionObservations(
                    dataCollectionId,
                );

            const matchingObservations =
                data.filter(
                    (observation) =>
                        observation.referenceYear ===
                        referenceYear,
                );

            const observationMap = new Map(
                matchingObservations.map(
                    (observation) => [
                        observation.questionnaireVariableId,
                        observation,
                    ],
                ),
            );

            const draftMap = new Map<string, ObservationDraft>();

            for (const variable of activeVariables) {
                const observation =
                    observationMap.get(variable.id);

                draftMap.set(
                    variable.id,
                    observation
                        ? observationToDraft(observation)
                        : createEmptyDraft(),
                );
            }

            setObservations(observationMap);
            setDrafts(draftMap);
        } catch (loadError) {
            console.error(
                "Failed to load data collection observations:",
                loadError,
            );

            setError(
                "Unable to load observations.",
            );
        } finally {
            setIsLoading(false);
        }
    }, [
        activeVariables,
        dataCollectionId,
        referenceYear,
    ]);

    useEffect(() => {
        void loadObservations();
    }, [loadObservations]);

    function updateDraft(
        variableId: string,
        changes: Partial<ObservationDraft>,
    ) {
        setDrafts((current) => {
            const next = new Map(current);
            const currentDraft =
                next.get(variableId) ??
                createEmptyDraft();

            next.set(variableId, {
                ...currentDraft,
                ...changes,
            });

            return next;
        });

        setSuccess(null);
    }

    function getDraft(
        variableId: string,
    ): ObservationDraft {
        return (
            drafts.get(variableId) ??
            createEmptyDraft()
        );
    }

    function buildRequest(
        variable: QuestionnaireVariable,
        draft: ObservationDraft,
    ) {
        const base = {
            dataCollectionId,
            questionnaireVariableId: variable.id,
            referenceYear,
            selectedUnit: variable.unit,
            comment: draft.comment.trim() || null,
        };

        if (draft.status !== "PROVIDED") {
            return {
                ...base,
                numericValue: null,
                textValue: null,
                booleanValue: null,
                dateValue: null,
            };
        }

        const valueType = getValueType(
            variable.dataType,
        );

        if (valueType === "numeric") {
            return {
                ...base,
                numericValue:
                    draft.value.trim() || null,
                textValue: null,
                booleanValue: null,
                dateValue: null,
            };
        }

        if (valueType === "boolean") {
            return {
                ...base,
                numericValue: null,
                textValue: null,
                booleanValue:
                    draft.value === ""
                        ? null
                        : draft.value === "true",
                dateValue: null,
            };
        }

        if (valueType === "date") {
            return {
                ...base,
                numericValue: null,
                textValue: null,
                booleanValue: null,
                dateValue:
                    draft.value.trim() || null,
            };
        }

        return {
            ...base,
            numericValue: null,
            textValue: draft.value,
            booleanValue: null,
            dateValue: null,
        };
    }

    async function handleSave(
        variable: QuestionnaireVariable,
    ) {
        const draft = getDraft(variable.id);

        if (
            variable.required &&
            draft.status === "PROVIDED" &&
            !draft.value.trim()
        ) {
            setError(
                `"${variable.name}" is required.`,
            );
            setSuccess(null);
            return;
        }

        try {
            setSavingVariableId(variable.id);
            setError(null);
            setSuccess(null);

            const request = buildRequest(
                variable,
                draft,
            );

            const existing =
                observations.get(variable.id);

            let savedObservation: DataCollectionObservation;

            if (existing) {
                if (existing.status === draft.status) {
                    savedObservation =
                        await updateDataCollectionObservation(
                            existing.id,
                            {
                                numericValue:
                                    request.numericValue,
                                textValue:
                                    request.textValue,
                                booleanValue:
                                    request.booleanValue,
                                dateValue:
                                    request.dateValue,
                                selectedUnit:
                                    request.selectedUnit,
                                comment:
                                    request.comment,
                            },
                        );
                } else {
                    await deleteDataCollectionObservation(
                        existing.id,
                    );

                    if (draft.status === "PROVIDED") {
                        savedObservation =
                            await createDataCollectionObservation(
                                request,
                            );
                    } else if (
                        draft.status === "NOT_AVAILABLE"
                    ) {
                        savedObservation =
                            await createNotAvailableObservation(
                                request,
                            );
                    } else {
                        savedObservation =
                            await createNotApplicableObservation(
                                request,
                            );
                    }
                }
            } else if (draft.status === "PROVIDED") {
                savedObservation =
                    await createDataCollectionObservation(
                        request,
                    );
            } else if (draft.status === "NOT_AVAILABLE") {
                savedObservation =
                    await createNotAvailableObservation(
                        request,
                    );
            } else {
                savedObservation =
                    await createNotApplicableObservation(
                        request,
                    );
            }

            setObservations((current) => {
                const next = new Map(current);
                next.set(
                    variable.id,
                    savedObservation,
                );
                return next;
            });

            setDrafts((current) => {
                const next = new Map(current);
                next.set(
                    variable.id,
                    observationToDraft(
                        savedObservation,
                    ),
                );
                return next;
            });

            setSuccess(
                `"${variable.name}" saved successfully.`,
            );
        } catch (saveError) {
            console.error(
                "Failed to save observation:",
                saveError,
            );

            setError(
                `Unable to save "${variable.name}".`,
            );
        } finally {
            setSavingVariableId(null);
        }
    }

    function renderValueField(
        variable: QuestionnaireVariable,
        draft: ObservationDraft,
    ) {
        if (draft.status !== "PROVIDED") {
            return null;
        }

        switch (variable.dataType) {
            case "BOOLEAN":
                return (
                    <TextField
                        select
                        label="Value"
                        value={draft.value}
                        onChange={(event) =>
                            updateDraft(
                                variable.id,
                                {
                                    value:
                                        event.target
                                            .value,
                                },
                            )
                        }
                        fullWidth
                    >
                        <MenuItem value="">
                            Select a value
                        </MenuItem>
                        <MenuItem value="true">
                            Yes
                        </MenuItem>
                        <MenuItem value="false">
                            No
                        </MenuItem>
                    </TextField>
                );

            case "DATE":
                return (
                    <TextField
                        type="date"
                        label="Value"
                        value={draft.value}
                        onChange={(event) =>
                            updateDraft(
                                variable.id,
                                {
                                    value:
                                        event.target
                                            .value,
                                },
                            )
                        }
                        fullWidth
                        slotProps={{
                            inputLabel: {
                                shrink: true,
                            },
                        }}
                    />
                );

            case "INTEGER":
                return (
                    <TextField
                        type="number"
                        label="Value"
                        value={draft.value}
                        onChange={(event) =>
                            updateDraft(
                                variable.id,
                                {
                                    value:
                                        event.target
                                            .value,
                                },
                            )
                        }
                        slotProps={{
                            htmlInput: {
                                step: 1,
                            },
                        }}
                        fullWidth
                    />
                );

            case "NUMBER":
            case "DECIMAL":
            case "PERCENTAGE":
                return (
                    <TextField
                        type="number"
                        label="Value"
                        value={draft.value}
                        onChange={(event) =>
                            updateDraft(
                                variable.id,
                                {
                                    value:
                                        event.target
                                            .value,
                                },
                            )
                        }
                        slotProps={{
                            htmlInput: {
                                step:
                                    variable.dataType ===
                                    "PERCENTAGE"
                                        ? "any"
                                        : "any",
                            },
                        }}
                        fullWidth
                    />
                );

            case "TEXT":
            default:
                return (
                    <TextField
                        label="Value"
                        value={draft.value}
                        onChange={(event) =>
                            updateDraft(
                                variable.id,
                                {
                                    value:
                                        event.target
                                            .value,
                                },
                            )
                        }
                        fullWidth
                    />
                );
        }
    }

    function renderVariable(
        variable: QuestionnaireVariable,
    ) {
        const draft = getDraft(variable.id);
        const isSaving =
            savingVariableId === variable.id;

        return (
            <Paper
                key={variable.id}
                variant="outlined"
                sx={{ p: 2 }}
            >
                <Stack spacing={2}>
                    <Box>
                        <Typography
                            variant="subtitle1"
                            sx={{ fontWeight: 600 }}
                        >
                            {variable.name}
                            {variable.required
                                ? " *"
                                : ""}
                        </Typography>

                        <Typography
                            variant="body2"
                            color="text.secondary"
                        >
                            {variable.seriesCode} ·{" "}
                            {variable.dataType}
                            {variable.unit
                                ? ` · ${variable.unit}`
                                : ""}
                        </Typography>

                        {variable.definition && (
                            <Typography
                                variant="body2"
                                color="text.secondary"
                                sx={{ mt: 0.5 }}
                            >
                                {variable.definition}
                            </Typography>
                        )}
                    </Box>

                    <FormControl>
                        <FormLabel>
                            Observation status
                        </FormLabel>

                        <RadioGroup
                            row
                            value={draft.status}
                            onChange={(event) =>
                                updateDraft(
                                    variable.id,
                                    {
                                        status: event
                                            .target
                                            .value as ObservationStatus,
                                        value: "",
                                    },
                                )
                            }
                        >
                            <FormControlLabel
                                value="PROVIDED"
                                control={<Radio />}
                                label="Provided"
                            />
                            <FormControlLabel
                                value="NOT_AVAILABLE"
                                control={<Radio />}
                                label="Not available"
                            />
                            <FormControlLabel
                                value="NOT_APPLICABLE"
                                control={<Radio />}
                                label="Not applicable"
                            />
                        </RadioGroup>
                    </FormControl>

                    <Grid
                        container
                        spacing={2}
                    >
                        <Grid
                            size={{
                                xs: 12,
                                md: 8,
                            }}
                        >
                            {renderValueField(
                                variable,
                                draft,
                            )}
                        </Grid>

                        <Grid
                            size={{
                                xs: 12,
                                md: 4,
                            }}
                        >
                            <TextField
                                label="Comment"
                                value={
                                    draft.comment
                                }
                                onChange={(event) =>
                                    updateDraft(
                                        variable.id,
                                        {
                                            comment:
                                                event
                                                    .target
                                                    .value,
                                        },
                                    )
                                }
                                fullWidth
                            />
                        </Grid>
                    </Grid>

                    <Box
                        sx={{
                            display: "flex",
                            justifyContent:
                                "flex-end",
                        }}
                    >
                        <Button
                            variant="contained"
                            onClick={() =>
                                void handleSave(
                                    variable,
                                )
                            }
                            disabled={isSaving}
                        >
                            {isSaving
                                ? "Saving..."
                                : "Save"}
                        </Button>
                    </Box>
                </Stack>
            </Paper>
        );
    }

    return (
        <CardLikeContainer>
            <Stack spacing={3}>
                <Stack
                    direction={{
                        xs: "column",
                        sm: "row",
                    }}
                    spacing={2}
                    sx={{
                        justifyContent:
                            "space-between",
                        alignItems: {
                            xs: "stretch",
                            sm: "center",
                        },
                    }}
                >
                    <Box>
                        <Typography
                            variant="h6"
                            sx={{
                                fontWeight: 600,
                            }}
                        >
                            Data Entry
                        </Typography>

                        <Typography
                            variant="body2"
                            color="text.secondary"
                        >
                            Enter observations for the
                            selected reference year.
                        </Typography>
                    </Box>

                    <TextField
                        type="number"
                        label="Reference year"
                        value={referenceYear}
                        onChange={(event) =>
                            setReferenceYear(
                                Number(
                                    event.target.value,
                                ),
                            )
                        }
                        sx={{ minWidth: 160 }}
                        helperText="The backend validates the allowed reference-year range."
                    />
                </Stack>

                {error && (
                    <Alert
                        severity="error"
                        onClose={() =>
                            setError(null)
                        }
                    >
                        {error}
                    </Alert>
                )}

                {success && (
                    <Alert
                        severity="success"
                        onClose={() =>
                            setSuccess(null)
                        }
                    >
                        {success}
                    </Alert>
                )}

                {isLoading ? (
                    <Box
                        sx={{
                            display: "flex",
                            justifyContent:
                                "center",
                            py: 6,
                        }}
                    >
                        <CircularProgress />
                    </Box>
                ) : activeVariables.length === 0 ? (
                    <Typography color="text.secondary">
                        No active variables are defined
                        for this questionnaire.
                    </Typography>
                ) : (
                    <Stack spacing={3}>
                        {groups
                            .filter((group) =>
                                groupedVariables.has(
                                    group.id,
                                ),
                            )
                            .sort(
                                (a, b) =>
                                    a.displayOrder -
                                    b.displayOrder,
                            )
                            .map((group) => (
                                <Box key={group.id}>
                                    <Typography
                                        variant="h6"
                                        sx={{
                                            fontWeight: 600,
                                            mb: 1.5,
                                        }}
                                    >
                                        {group.name}
                                    </Typography>

                                    <Stack spacing={2}>
                                        {(
                                            groupedVariables.get(
                                                group.id,
                                            ) ?? []
                                        ).map(
                                            renderVariable,
                                        )}
                                    </Stack>
                                </Box>
                            ))}

                        {groupedVariables.has(null) && (
                            <Box>
                                <Typography
                                    variant="h6"
                                    sx={{
                                        fontWeight: 600,
                                        mb: 1.5,
                                    }}
                                >
                                    Variables without
                                    group
                                </Typography>

                                <Stack spacing={2}>
                                    {(
                                        groupedVariables.get(
                                            null,
                                        ) ?? []
                                    ).map(
                                        renderVariable,
                                    )}
                                </Stack>
                            </Box>
                        )}
                    </Stack>
                )}
            </Stack>
        </CardLikeContainer>
    );
}

function CardLikeContainer({
    children,
}: {
    children: React.ReactNode;
}) {
    return (
        <Paper
            variant="outlined"
            sx={{ p: 3 }}
        >
            {children}
        </Paper>
    );
}
