import {
    useEffect,
    useMemo,
    useState,
} from "react";

import {
    Alert,
    Box,
    Button,
    CircularProgress,
    Stack,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
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

import type { DataCollectionStatus } from "../types/dataCollection.types";

import type { QuestionnaireGroup } from
    "@/modules/questionnaire/types/questionnaireGroup.types";

import type { QuestionnaireVariable } from
    "@/modules/questionnaire/types/questionnaireVariable.types";

import {
    PW_A_CHOICE_OPTIONS,
    PW_A_REFERENCE_YEARS,
} from "../config/pwATemplate";

interface SpreadsheetDataEntryFormProps {
    dataCollectionId: string;
    groups: QuestionnaireGroup[];
    variables: QuestionnaireVariable[];
    status: DataCollectionStatus;
    onObservationSelect?: (
        observationId: string | undefined,
    ) => void;
}

type CellKey = `${string}:${number}`;

type CellValue = string | null;

interface CellState {
    value: CellValue;
    status: ObservationStatus | null;
    observationId: string | null;
}

function makeCellKey(
    variableId: string,
    year: number,
): CellKey {
    return `${variableId}:${year}`;
}

function getObservationValue(
    observation: DataCollectionObservation,
    variable: QuestionnaireVariable,
): CellValue {
    if (
        observation.numericValue !== null &&
        observation.numericValue !== undefined
    ) {
        const numericValue =
            String(observation.numericValue);

        const choices =
            PW_A_CHOICE_OPTIONS[
                variable.seriesCode.toUpperCase()
            ];

        if (choices) {
            const matchedChoice =
                choices.find(
                    (option) =>
                        String(option.value) ===
                        numericValue,
                );

            if (matchedChoice) {
                return (
                    matchedChoice.aliases?.[0] ??
                    matchedChoice.label
                );
            }
        }

        return numericValue;
    }

    if (observation.textValue !== null &&
        observation.textValue !== undefined) {
        return String(observation.textValue);
    }

    if (observation.booleanValue !== null &&
        observation.booleanValue !== undefined) {
        return String(observation.booleanValue);
    }

    if (observation.dateValue !== null &&
        observation.dateValue !== undefined) {
        return String(observation.dateValue);
    }

    return null;
}

function createInitialCells(
    variables: QuestionnaireVariable[],
    observations: DataCollectionObservation[],
): Record<string, CellState> {
    const cells: Record<string, CellState> = {};

    for (const variable of variables) {
        for (const year of PW_A_REFERENCE_YEARS) {
            const key = makeCellKey(
                variable.id,
                year,
            );

            cells[key] = {
                value: null,
                status: null,
                observationId: null,
            };
        }
    }

    for (const observation of observations) {
        const key = makeCellKey(
            observation.questionnaireVariableId,
            observation.referenceYear,
        );

        if (!(key in cells)) {
            continue;
        }

        const variable =
            variables.find(
                (item) =>
                    item.id ===
                    observation.questionnaireVariableId,
            );

        if (!variable) {
            continue;
        }

        cells[key] = {
            value: getObservationValue(
                observation,
                variable,
            ),
            status: observation.status,
            observationId: observation.id,
        };
    }

    return cells;
}

export default function SpreadsheetDataEntryForm({
    dataCollectionId,
    groups,
    variables,
    status,
    onObservationSelect,
}: SpreadsheetDataEntryFormProps) {
    const [cells, setCells] = useState<
        Record<string, CellState>
    >({});

    const [savedCells, setSavedCells] = useState<
        Record<string, CellState>
    >({});

    const isReadOnly =
        status === "SUBMITTED" ||
        status === "VALIDATED";

    const [isLoading, setIsLoading] =
        useState(true);

    const [isSaving, setIsSaving] =
        useState(false);

    const [error, setError] =
        useState<string | null>(null);

    const [successMessage, setSuccessMessage] =
        useState<string | null>(null);

    async function loadObservations() {
        try {
            setIsLoading(true);
            setError(null);

            const observations =
                await getDataCollectionObservations(
                    dataCollectionId,
                );

            const initialCells =
                createInitialCells(
                    variables,
                    observations,
                );

            setCells(initialCells);
            setSavedCells(initialCells);
        } catch (loadError) {
            console.error(
                "Failed to load spreadsheet observations:",
                loadError,
            );

            setError(
                "Unable to load spreadsheet observations.",
            );
        } finally {
            setIsLoading(false);
        }
    }

    useEffect(() => {
        void loadObservations();
    }, [
        dataCollectionId,
        variables,
    ]);

    const groupedVariables = useMemo(() => {
        return groups
            .slice()
            .sort(
                (a, b) =>
                    a.displayOrder -
                    b.displayOrder,
            )
            .map((group) => ({
                group,
                variables: variables
                    .filter(
                        (variable) =>
                            variable.questionnaireGroupId ===
                            group.id,
                    )
                    .sort(
                        (a, b) =>
                            a.displayOrder -
                            b.displayOrder,
                    ),
            }))
            .filter(
                (entry) =>
                    entry.variables.length > 0,
            );
    }, [groups, variables]);

    const hasChanges = useMemo(() => {
        const keys = new Set([
            ...Object.keys(cells),
            ...Object.keys(savedCells),
        ]);

        for (const key of keys) {
            const current = cells[key];
            const saved = savedCells[key];

            if (!current || !saved) {
                return true;
            }

            if (
                current.value !== saved.value ||
                current.status !== saved.status
            ) {
                return true;
            }
        }

        return false;
    }, [cells, savedCells]);

    function handleSpreadsheetPaste(
        event: React.ClipboardEvent<HTMLElement>,
        startVariable: QuestionnaireVariable,
        startYear: number,
    ) {
        if (isReadOnly) {
            return;
        }

        const clipboardText =
            event.clipboardData.getData("text/plain");

        if (!clipboardText) {
            return;
        }

        event.preventDefault();

        const rows = clipboardText
            .replace(/\r\n/g, "\n")
            .replace(/\r/g, "\n")
            .split("\n");

        // Excel ajoute parfois une ligne vide finale.
        while (
            rows.length > 1 &&
            rows[rows.length - 1] === ""
        ) {
            rows.pop();
        }

        const matrix = rows.map((row) =>
            row.split("\t"),
        );

        const startYearIndex =
            PW_A_REFERENCE_YEARS.findIndex(
                (year) => year === startYear,
            );

        if (startYearIndex < 0) {
            return;
        }

        /*
         * Le tableau est affiché groupe par groupe.
         * Le collage doit donc utiliser exactement
         * l'ordre des variables du groupe affiché,
         * et non l'ordre global de `variables`.
         */
        const startGroup = groupedVariables.find(
            ({ variables: groupVariables }) =>
                groupVariables.some(
                    (variable) =>
                        variable.id === startVariable.id,
                ),
        );

        if (!startGroup) {
            return;
        }

        const startVariableIndex =
            startGroup.variables.findIndex(
                (variable) =>
                    variable.id === startVariable.id,
            );

        if (startVariableIndex < 0) {
            return;
        }

        const pastedCells: Array<{
            key: string;
            rawValue: string;
            variable: QuestionnaireVariable;
            year: number;
        }> = [];

        matrix.forEach((row, rowOffset) => {
            const variableIndex =
                startVariableIndex + rowOffset;

            /*
             * Le collage ne déborde pas du groupe courant.
             */
            if (
                variableIndex < 0 ||
                variableIndex >=
                    startGroup.variables.length
            ) {
                return;
            }

            const variable =
                startGroup.variables[variableIndex];

            row.forEach(
                (rawValue, columnOffset) => {
                    const yearIndex =
                        startYearIndex +
                        columnOffset;

                    if (
                        yearIndex < 0 ||
                        yearIndex >=
                            PW_A_REFERENCE_YEARS.length
                    ) {
                        return;
                    }

                    /*
                     * On conserve exactement le texte
                     * provenant d'Excel.
                     *
                     * Une cellule vide n'écrase pas
                     * la valeur existante.
                     */
                    if (rawValue === "") {
                        return;
                    }

                    const year =
                        PW_A_REFERENCE_YEARS[yearIndex];

                    pastedCells.push({
                        key: makeCellKey(
                            variable.id,
                            year,
                        ),
                        variable,
                        year,
                        rawValue,
                    });
                },
            );
        });

        if (pastedCells.length === 0) {
            return;
        }

        setCells((current) => {
            const next = { ...current };

            pastedCells.forEach(
                ({
                    key,
                    rawValue,
                }) => {
                    const existing =
                        current[key];

                    next[key] = {
                        ...existing,
                        value: rawValue,
                        status: "PROVIDED",
                    };
                },
            );

            return next;
        });

        setSuccessMessage(
            `${pastedCells.length} cell${
                pastedCells.length === 1
                    ? ""
                    : "s"
            } pasted from Excel.`,
        );
    }

    function updateCell(
        variable: QuestionnaireVariable,
        year: number,
        value: CellValue,
        status: ObservationStatus | null = "PROVIDED",
    ) {
        const key = makeCellKey(
            variable.id,
            year,
        );

        setCells((current) => ({
            ...current,
            [key]: {
                ...current[key],
                value,
                status:
                    value === null &&
                    status === "PROVIDED"
                        ? null
                        : status,
            },
        }));

        setSuccessMessage(null);
    }

    function getCell(
        variableId: string,
        year: number,
    ): CellState {
        return (
            cells[
                makeCellKey(
                    variableId,
                    year,
                )
            ] ?? {
                value: null,
                status: null,
                observationId: null,
            }
        );
    }

    function buildObservationRequest(
        variable: QuestionnaireVariable,
        cell: CellState,
        year: number,
    ) {
        const rawValue = cell.value;

        const base = {
            dataCollectionId,
            questionnaireVariableId: variable.id,
            referenceYear: year,
            numericValue: null as string | null,
            textValue: null as string | null,
            booleanValue: null as boolean | null,
            dateValue: null as string | null,
            selectedUnit: variable.unit ?? null,
            comment: null as string | null,
        };

        if (rawValue === null) {
            return base;
        }

        switch (variable.dataType) {
            case "NUMBER":
            case "INTEGER":
            case "DECIMAL":
            case "PERCENTAGE": {
                const choices =
                    PW_A_CHOICE_OPTIONS[
                        variable.seriesCode.toUpperCase()
                    ];

                if (choices) {
                    const normalizedRawValue =
                        rawValue
                            .trim()
                            .replace(/\\s+/g, " ")
                            .toLowerCase();

                    const matchedChoice =
                        choices.find((option) => {
                            const normalizedLabel =
                                option.label
                                    .replace(/\\s+/g, " ")
                                    .trim()
                                    .toLowerCase();

                            const normalizedAliases =
                                (option.aliases ?? []).map(
                                    (alias) =>
                                        alias
                                            .replace(
                                                /\\s+/g,
                                                " ",
                                            )
                                            .trim()
                                            .toLowerCase(),
                                );

                            return (
                                String(option.value) ===
                                    rawValue.trim() ||
                                normalizedLabel ===
                                    normalizedRawValue ||
                                normalizedAliases.includes(
                                    normalizedRawValue,
                                )
                            );
                        });

                    if (matchedChoice) {
                        return {
                            ...base,
                            numericValue:
                                String(
                                    matchedChoice.value,
                                ),
                        };
                    }
                }

                // For ordinary numeric variables, or for an
                // unrecognised coded value, keep the raw value.
                // Backend validation remains authoritative.
                return {
                    ...base,
                    numericValue: rawValue.trim(),
                };
            }

            case "TEXT":
                return {
                    ...base,
                    textValue: rawValue,
                };

            case "BOOLEAN": {
                const normalized =
                    rawValue.trim().toLowerCase();

                if (
                    normalized === "true" ||
                    normalized === "1" ||
                    normalized === "yes" ||
                    normalized === "oui"
                ) {
                    return {
                        ...base,
                        booleanValue: true,
                    };
                }

                if (
                    normalized === "false" ||
                    normalized === "0" ||
                    normalized === "no" ||
                    normalized === "non"
                ) {
                    return {
                        ...base,
                        booleanValue: false,
                    };
                }

                throw new Error(
                    `Invalid boolean value "${rawValue}" for variable "${variable.seriesCode}".`,
                );
            }

            case "DATE":
                return {
                    ...base,
                    dateValue: rawValue.trim(),
                };

            default:
                throw new Error(
                    `Unsupported data type "${variable.dataType}" for variable "${variable.seriesCode}".`,
                );
        }
    }

    async function saveCell(
        variable: QuestionnaireVariable,
        year: number,
        cell: CellState,
        previousCell: CellState | undefined,
    ) {
        const request =
            buildObservationRequest(
                variable,
                cell,
                year,
            );

        // Observation status is managed by dedicated backend endpoints.
        if (cell.status === "NOT_AVAILABLE") {
            if (cell.observationId) {
                await deleteDataCollectionObservation(
                    cell.observationId,
                );
            }

            await createNotAvailableObservation(
                request,
            );

            return;
        }

        if (cell.status === "NOT_APPLICABLE") {
            if (cell.observationId) {
                await deleteDataCollectionObservation(
                    cell.observationId,
                );
            }

            await createNotApplicableObservation(
                request,
            );

            return;
        }

        // An empty PROVIDED cell means that the observation
        // should not exist.
        if (
            cell.status === null ||
            cell.value === null
        ) {
            if (cell.observationId) {
                await deleteDataCollectionObservation(
                    cell.observationId,
                );
            }

            return;
        }

        // PROVIDED
        // A special-status observation cannot be converted
        // through PUT because its status is managed by the
        // dedicated backend endpoints. Replace it instead.
        if (
            cell.observationId &&
            (
                previousCell?.status ===
                    "NOT_AVAILABLE" ||
                previousCell?.status ===
                    "NOT_APPLICABLE"
            )
        ) {
            await deleteDataCollectionObservation(
                cell.observationId,
            );

            await createDataCollectionObservation(
                request,
            );

            return;
        }

        if (cell.observationId) {
            await updateDataCollectionObservation(
                cell.observationId,
                request,
            );

            return;
        }

        await createDataCollectionObservation(
            request,
        );
    }

    async function saveChanges() {
        if (!hasChanges) {
            return;
        }

        try {
            setIsSaving(true);
            setError(null);
            setSuccessMessage(null);

            const changedEntries: Array<{
                variable: QuestionnaireVariable;
                year: number;
                cell: CellState;
            }> = [];

            for (const group of groupedVariables) {
                for (const variable of group.variables) {
                    for (
                        const year of
                        PW_A_REFERENCE_YEARS
                    ) {
                        const key =
                            makeCellKey(
                                variable.id,
                                year,
                            );

                        const current =
                            cells[key];

                        const saved =
                            savedCells[key];

                        if (!current) {
                            continue;
                        }

                        if (
                            !saved ||
                            current.value !==
                                saved.value ||
                            current.status !==
                                saved.status
                        ) {
                            changedEntries.push({
                                variable,
                                year,
                                cell: current,
                            });
                        }
                    }
                }
            }

            for (const entry of changedEntries) {
                try {
                    console.log(
                        "[PW_A SAVE]",
                        entry.variable.seriesCode,
                        entry.variable.dataType,
                        entry.year,
                        entry.cell.value,
                    );

                    await saveCell(
                        entry.variable,
                        entry.year,
                        entry.cell,
                        savedCells[
                            makeCellKey(
                                entry.variable.id,
                                entry.year,
                            )
                        ],
                    );
                } catch (error) {
                    console.error(
                        "[PW_A SAVE FAILED]",
                        {
                            seriesCode:
                                entry.variable.seriesCode,
                            dataType:
                                entry.variable.dataType,
                            year:
                                entry.year,
                            value:
                                entry.cell.value,
                            error,
                        },
                    );

                    throw error;
                }
            }

            await loadObservations();

            setSuccessMessage(
                "Spreadsheet changes saved successfully.",
            );
        } catch (saveError) {
            console.error(
                "Failed to save spreadsheet changes:",
                saveError,
            );

            setError(
                "Unable to save spreadsheet changes.",
            );
        } finally {
            setIsSaving(false);
        }
    }

    if (isLoading) {
        return (
            <Box
                sx={{
                    display: "flex",
                    justifyContent: "center",
                    py: 6,
                }}
            >
                <CircularProgress />
            </Box>
        );
    }

    return (
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
                        sx={{ fontWeight: 600 }}
                    >
                        Spreadsheet Data Entry
                    </Typography>

                    <Typography
                        variant="body2"
                        color="text.secondary"
                    >
                        Reference years: 2025–2015
                    </Typography>
                </Box>

                {!isReadOnly && (
                    <Button
                        variant="contained"
                        onClick={() => void saveChanges()}
                        disabled={
                            isSaving ||
                            !hasChanges
                        }
                    >
                        {isSaving
                            ? "Saving..."
                            : "Save changes"}
                    </Button>
                )}
            </Stack>

            {error && (
                <Alert severity="error">
                    {error}
                </Alert>
            )}

            {successMessage && (
                <Alert severity="success">
                    {successMessage}
                </Alert>
            )}

            <TableContainer
                sx={{
                    maxHeight: 700,
                    overflow: "auto",
                    border: 1,
                    borderColor: "divider",
                }}
            >
                <Table
                    stickyHeader
                    size="small"
                    sx={{
                        minWidth:
                            70 +
                            280 +
                            60 +
                            PW_A_REFERENCE_YEARS.length * 82,
                        tableLayout: "fixed",
                    }}
                >
                    <TableHead>
                        <TableRow>
                            <TableCell
                                sx={{
                                    width: 70,
                                    minWidth: 70,
                                    position: "sticky",
                                    left: 0,
                                    zIndex: 4,
                                    backgroundColor: "background.paper",
                                    fontWeight: 700,
                                }}
                            >
                                Code
                            </TableCell>

                            <TableCell
                                sx={{
                                    width: 280,
                                    minWidth: 280,
                                    position: "sticky",
                                    left: 70,
                                    zIndex: 4,
                                    backgroundColor: "background.paper",
                                    fontWeight: 700,
                                }}
                            >
                                Variable
                            </TableCell>

                            <TableCell
                                sx={{
                                    width: 60,
                                    minWidth: 60,
                                    position: "sticky",
                                    left: 350,
                                    zIndex: 4,
                                    backgroundColor: "background.paper",
                                    fontWeight: 700,
                                }}
                            >
                                Unit
                            </TableCell>

                            {PW_A_REFERENCE_YEARS.map((year) => (
                                <TableCell
                                    key={year}
                                    align="center"
                                    sx={{
                                        width: 82,
                                        minWidth: 82,
                                        fontWeight: 700,
                                        backgroundColor: "background.paper",
                                    }}
                                >
                                    {year}
                                </TableCell>
                            ))}
                        </TableRow>
                    </TableHead>

                    <TableBody>
                        {groupedVariables.flatMap(
                            ({ group, variables: groupVariables }) => [
                                <TableRow key={`group-${group.id}`}>
                                    <TableCell
                                        colSpan={
                                            3 +
                                            PW_A_REFERENCE_YEARS.length
                                        }
                                        sx={{
                                            fontWeight: 700,
                                            backgroundColor:
                                                "action.hover",
                                        }}
                                    >
                                        {group.name}
                                    </TableCell>
                                </TableRow>,

                                ...groupVariables.map((variable) => (
                                    <TableRow
                                        key={variable.id}
                                        hover
                                    >
                                        <TableCell
                                            sx={{
                                                width: 60,
                                                minWidth: 60,
                                                position: "sticky",
                                                left: 0,
                                                zIndex: 2,
                                                backgroundColor:
                                                    "background.paper",
                                            }}
                                        >
                                            <Typography
                                                variant="body2"
                                                sx={{
                                                    fontWeight: 600,
                                                    fontSize: "0.75rem",
                                                    whiteSpace: "nowrap",
                                                }}
                                            >
                                                {variable.seriesCode}
                                            </Typography>
                                        </TableCell>

                                        <TableCell
                                            sx={{
                                                width: 250,
                                                minWidth: 250,
                                                position: "sticky",
                                                left: 70,
                                                zIndex: 2,
                                                backgroundColor:
                                                    "background.paper",
                                            }}
                                        >
                                            <Typography
                                                variant="body2"
                                                sx={{
                                                    fontWeight: 600,
                                                    fontSize: "0.75rem",
                                                    whiteSpace: "nowrap",
                                                    overflow: "hidden",
                                                    textOverflow: "ellipsis",
                                                }}
                                                title={
                                                    variable.definition ??
                                                    variable.name
                                                }
                                            >
                                                {variable.name}
                                            </Typography>

                                            <Typography
                                                variant="caption"
                                                color="text.secondary"
                                                sx={{
                                                    display: "block",
                                                    fontSize: "0.6875rem",
                                                    lineHeight: 1.2,
                                                    whiteSpace: "nowrap",
                                                    overflow: "hidden",
                                                    textOverflow: "ellipsis",
                                                }}
                                                title={
                                                    variable.definition ??
                                                    ""
                                                }
                                            >
                                                {variable.definition ?? ""}
                                            </Typography>
                                        </TableCell>

                                        <TableCell
                                            sx={{
                                                width: 150,
                                                minWidth: 150,
                                                position: "sticky",
                                                left: 310,
                                                zIndex: 2,
                                                backgroundColor:
                                                    "background.paper",
                                            }}
                                        >
                                            {variable.unit ?? "—"}
                                        </TableCell>

                                        {PW_A_REFERENCE_YEARS.map(
                                            (year) => {
                                                const cell = getCell(
                                                    variable.id,
                                                    year,
                                                );

                                                return (
                                                    <TableCell
                                                        key={year}
                                                        align="center"
                                                        onClick={() =>
                                                            onObservationSelect?.(
                                                                cell.observationId ?? undefined,
                                                            )
                                                        }
                                                        sx={{
                                                            width: 95,
                                                            minWidth: 95,
                                                            p: 0.25,
                                                        }}
                                                    >
                                                        <TextField
                                                            size="small"
                                                            fullWidth
                                                            disabled={isReadOnly}
                                                            type="text"
                                                            sx={{
                                                                "& .MuiInputBase-input": {
                                                                    fontSize: "0.75rem",
                                                                    padding: "6px 8px",
                                                                },
                                                            }}
                                                            value={cell.value ?? ""}
                                                            onChange={(event) =>
                                                                updateCell(
                                                                    variable,
                                                                    year,
                                                                    event.target.value === ""
                                                                        ? null
                                                                        : event.target.value,
                                                                )
                                                            }
                                                            onPaste={(event) =>
                                                                handleSpreadsheetPaste(
                                                                    event,
                                                                    variable,
                                                                    year,
                                                                )
                                                            }
                                                        />
                                                    </TableCell>
                                                );
                                            },
                                        )}
                                    </TableRow>
                                )),
                            ],
                        )}
                    </TableBody>
                </Table>
            </TableContainer>
        </Stack>
    );
}
