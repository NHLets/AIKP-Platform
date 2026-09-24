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
    FormControl,
    InputLabel,
    MenuItem,
    Select,
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

import type { QuestionnaireGroup } from
    "@/modules/questionnaire/types/questionnaireGroup.types";

import type { QuestionnaireVariable } from
    "@/modules/questionnaire/types/questionnaireVariable.types";

import { PW_A_CHOICE_OPTIONS } from "../config/pwATemplate";

import { REFERENCE_YEARS } from "../config/spreadsheetConfig";

interface SpreadsheetDataEntryFormProps {
    dataCollectionId: string;
    groups: QuestionnaireGroup[];
    variables: QuestionnaireVariable[];
}

type CellKey = `${string}:${number}`;

type CellValue =
    | string
    | number
    | boolean
    | null;

interface CellState {
    value: CellValue;
    status: ObservationStatus | null;
    observationId: string | null;
    selectedUnit: string | null;
}

function makeCellKey(
    variableId: string,
    year: number,
): CellKey {
    return `${variableId}:${year}`;
}

function getObservationValue(
    observation: DataCollectionObservation,
): CellValue {
    if (observation.numericValue !== null &&
        observation.numericValue !== undefined) {
        return observation.numericValue;
    }

    if (observation.textValue !== null &&
        observation.textValue !== undefined) {
        return observation.textValue;
    }

    if (observation.booleanValue !== null &&
        observation.booleanValue !== undefined) {
        return observation.booleanValue;
    }

    if (observation.dateValue !== null &&
        observation.dateValue !== undefined) {
        return observation.dateValue;
    }

    return null;
}

function createInitialCells(
    variables: QuestionnaireVariable[],
    observations: DataCollectionObservation[],
): Record<string, CellState> {
    const cells: Record<string, CellState> = {};

    for (const variable of variables) {
        for (const year of REFERENCE_YEARS) {
            const key = makeCellKey(
                variable.id,
                year,
            );

            cells[key] = {
                value: null,
                status: null,
                observationId: null,
                selectedUnit: null,
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

        cells[key] = {
            value: getObservationValue(observation),
            status: observation.status,
            observationId: observation.id,
            selectedUnit: observation.selectedUnit ?? null,
        };
    }

    return cells;
}

function getInputType(
    variable: QuestionnaireVariable,
): "text" | "number" {
    switch (variable.dataType) {
        case "NUMBER":
        case "INTEGER":
        case "DECIMAL":
        case "PERCENTAGE":
            return "text";

        default:
            return "text";
    }
}

function normalizeValue(
    variable: QuestionnaireVariable,
    value: string,
): CellValue {
    if (value === "") {
        return null;
    }

    switch (variable.dataType) {
        case "NUMBER":
        case "INTEGER":
        case "DECIMAL":
        case "PERCENTAGE": {
            const numericValue = Number(value);

            return Number.isNaN(numericValue)
                ? null
                : numericValue;
        }

        case "BOOLEAN":
            return value === "true";

        default:
            return value;
    }
}

function getUnitOptions(
    variable: QuestionnaireVariable,
): string[] {
    if (!variable.unit) {
        return [];
    }

    return variable.unit
        .split(",")
        .map((unit) => unit.trim())
        .filter(Boolean);
}

function getDefaultUnit(
    variable: QuestionnaireVariable,
): string | null {
    const options = getUnitOptions(variable);

    return options.length === 1
        ? options[0]
        : null;
}

export default function SpreadsheetDataEntryForm({
    dataCollectionId,
    groups,
    variables,
}: SpreadsheetDataEntryFormProps) {
    const [cells, setCells] = useState<
        Record<string, CellState>
    >({});

    const [savedCells, setSavedCells] = useState<
        Record<string, CellState>
    >({});

    const [inputValues, setInputValues] = useState<
        Record<string, string>
    >({});

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
                current.status !== saved.status ||
                current.selectedUnit !== saved.selectedUnit
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
        const clipboardText =
            event.clipboardData.getData("text/plain");

        /*
         * Let the browser handle an ordinary single-cell paste.
         * This handler is only responsible for rectangular clipboard
         * content containing tabs or multiple rows.
         */
        if (
            !clipboardText ||
            (
                !clipboardText.includes("\t") &&
                !clipboardText.includes("\n") &&
                !clipboardText.includes("\r")
            )
        ) {
            return;
        }

        event.preventDefault();

        const rows = clipboardText
            .replace(/\r\n/g, "\n")
            .replace(/\r/g, "\n")
            .split("\n")
            .map((row) => row.split("\t"));

        const startVariableIndex =
            variables.findIndex(
                (variable) =>
                    variable.id === startVariable.id,
            );

        const startYearIndex =
            REFERENCE_YEARS.findIndex(
                (year) => year === startYear,
            );

        if (
            startVariableIndex < 0 ||
            startYearIndex < 0
        ) {
            return;
        }

        /*
         * Build the entire paste matrix before touching React state.
         * This prevents asynchronous setState behaviour from affecting
         * the mapping of rows and columns.
         */
        const pastedCells: Array<{
            key: string;
            variable: QuestionnaireVariable;
            year: number;
            rawValue: string;
        }> = [];

        rows.forEach(
            (row, rowOffset) => {
                const variableIndex =
                    startVariableIndex +
                    rowOffset;

                if (
                    variableIndex < 0 ||
                    variableIndex >=
                        variables.length
                ) {
                    return;
                }

                const variable =
                    variables[variableIndex];

                row.forEach(
                    (
                        rawValue,
                        columnOffset,
                    ) => {
                        const yearIndex =
                            startYearIndex +
                            columnOffset;

                        if (
                            yearIndex < 0 ||
                            yearIndex >=
                                REFERENCE_YEARS.length
                        ) {
                            return;
                        }

                        /*
                         * Do not overwrite existing observations
                         * with empty Excel cells.
                         */
                        if (
                            rawValue.trim() === ""
                        ) {
                            return;
                        }

                        const year =
                            REFERENCE_YEARS[
                                yearIndex
                            ];

                        pastedCells.push({
                            key: makeCellKey(
                                variable.id,
                                year,
                            ),
                            variable,
                            year,
                            rawValue:
                                rawValue.trim(),
                        });
                    },
                );
            },
        );

        if (pastedCells.length === 0) {
            return;
        }

        /*
         * Apply the complete matrix in one React state update.
         */
        setCells((current) => {
            const next = { ...current };

            pastedCells.forEach(
                ({
                    key,
                    variable,
                    rawValue,
                }) => {
                    const existing =
                        current[key];

                    const normalizedText =
                        rawValue
                            .trim()
                            .toUpperCase();

                    /*
                     * NA = NOT_AVAILABLE
                     */
                    if (
                        normalizedText ===
                        "NA"
                    ) {
                        next[key] = {
                            ...existing,
                            value: null,
                            status:
                                "NOT_AVAILABLE",
                            selectedUnit:
                                existing?.selectedUnit ??
                                getDefaultUnit(
                                    variable,
                                ),
                        };

                        return;
                    }

                    /*
                     * N/A = NOT_APPLICABLE
                     */
                    if (
                        normalizedText ===
                        "N/A"
                    ) {
                        next[key] = {
                            ...existing,
                            value: null,
                            status:
                                "NOT_APPLICABLE",
                            selectedUnit:
                                existing?.selectedUnit ??
                                getDefaultUnit(
                                    variable,
                                ),
                        };

                        return;
                    }

                    const normalized =
                        normalizeValue(
                            variable,
                            rawValue,
                        );

                    /*
                     * Invalid numeric values do not overwrite
                     * an existing valid observation.
                     */
                    if (
                        normalized === null
                    ) {
                        return;
                    }

                    next[key] = {
                        ...existing,
                        value: normalized,
                        status: "PROVIDED",
                        selectedUnit:
                            existing?.selectedUnit ??
                            getDefaultUnit(
                                variable,
                            ),
                    };
                },
            );

            return next;
        });

        /*
         * Clear any controlled-input text belonging to cells
         * affected by the rectangular paste.
         */
        setInputValues((current) => {
            const next = { ...current };

            pastedCells.forEach(
                ({ key }) => {
                    delete next[key];
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

    function getInputValue(
        variable: QuestionnaireVariable,
        year: number,
        cell: CellState,
    ): string {
        const key = makeCellKey(
            variable.id,
            year,
        );

        if (
            Object.prototype.hasOwnProperty.call(
                inputValues,
                key,
            )
        ) {
            return inputValues[key];
        }

        if (cell.status === "NOT_AVAILABLE") {
            return "NA";
        }

        if (cell.status === "NOT_APPLICABLE") {
            return "N/A";
        }

        return cell.value === null
            ? ""
            : String(cell.value);
    }

    function updateCell(
        variable: QuestionnaireVariable,
        year: number,
        value: CellValue,
        status: ObservationStatus | null = "PROVIDED",
        selectedUnit?: string | null,
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
                selectedUnit:
                    selectedUnit ??
                    current[key]?.selectedUnit ??
                    getDefaultUnit(variable),
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
                selectedUnit: null,
            }
        );
    }

    async function saveCell(
        variable: QuestionnaireVariable,
        year: number,
        cell: CellState,
        previousCell: CellState | undefined,
    ) {
        const request = {
            dataCollectionId,
            questionnaireVariableId:
                variable.id,
            referenceYear: year,
            numericValue:
                typeof cell.value === "number"
                    ? String(cell.value)
                    : null,
            textValue:
                typeof cell.value === "string"
                    ? cell.value
                    : null,
            booleanValue:
                typeof cell.value === "boolean"
                    ? cell.value
                    : null,
            dateValue:
                variable.dataType === "DATE" &&
                typeof cell.value === "string"
                    ? cell.value
                    : null,
            selectedUnit:
                cell.selectedUnit ??
                getDefaultUnit(variable),
            comment: null,
        };

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
                        REFERENCE_YEARS
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
                                saved.status ||
                            current.selectedUnit !==
                                saved.selectedUnit
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
                            120 +
                            REFERENCE_YEARS.length * 82,
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
                                    width: 120,
                                    minWidth: 120,
                                    position: "sticky",
                                    left: 350,
                                    zIndex: 4,
                                    backgroundColor: "background.paper",
                                    fontWeight: 700,
                                }}
                            >
                                Unit
                            </TableCell>

                            {REFERENCE_YEARS.map((year) => (
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
                                            REFERENCE_YEARS.length
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
                                                width: 70,
                                                minWidth: 70,
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
                                                    whiteSpace: "nowrap",
                                                }}
                                            >
                                                {variable.seriesCode}
                                            </Typography>
                                        </TableCell>

                                        <TableCell
                                            sx={{
                                                width: 280,
                                                minWidth: 280,
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
                                                width: 120,
                                                minWidth: 120,
                                                position: "sticky",
                                                left: 350,
                                                zIndex: 2,
                                                backgroundColor:
                                                    "background.paper",
                                                p: 0.5,
                                            }}
                                        >
                                            {getUnitOptions(variable).length > 1 ? (
                                                <FormControl
                                                    size="small"
                                                    fullWidth
                                                >
                                                    <Select
                                                        value={
                                                            getCell(
                                                                variable.id,
                                                                2025,
                                                            ).selectedUnit ??
                                                            ""
                                                        }
                                                        displayEmpty
                                                        onChange={(event) => {
                                                            const selectedUnit =
                                                                event.target.value;

                                                            setCells((current) => {
                                                                const updated = {
                                                                    ...current,
                                                                };

                                                                for (
                                                                    const year of
                                                                        REFERENCE_YEARS
                                                                ) {
                                                                    const key =
                                                                        makeCellKey(
                                                                            variable.id,
                                                                            year,
                                                                        );

                                                                    if (updated[key]) {
                                                                        updated[key] = {
                                                                            ...updated[key],
                                                                            selectedUnit,
                                                                        };
                                                                    }
                                                                }

                                                                return updated;
                                                            });

                                                            setSuccessMessage(null);
                                                        }}
                                                    >
                                                        <MenuItem value="">
                                                            Select unit
                                                        </MenuItem>

                                                        {getUnitOptions(
                                                            variable,
                                                        ).map((unit) => (
                                                            <MenuItem
                                                                key={unit}
                                                                value={unit}
                                                            >
                                                                {unit}
                                                            </MenuItem>
                                                        ))}
                                                    </Select>
                                                </FormControl>
                                            ) : (
                                                variable.unit ?? "—"
                                            )}
                                        </TableCell>

                                        {REFERENCE_YEARS.map(
                                            (year) => {
                                                const cell = getCell(
                                                    variable.id,
                                                    year,
                                                );

                                                const choices =
                                                    PW_A_CHOICE_OPTIONS[
                                                        variable.seriesCode.toUpperCase()
                                                    ];

                                                if (choices) {
                                                    return (
                                                        <TableCell
                                                            key={year}
                                                            align="center"
                                                            sx={{
                                                                width: 82,
                                                                minWidth: 82,
                                                                p: 0.5,
                                                            }}
                                                        >
                                                            <FormControl
                                                                size="small"
                                                                fullWidth
                                                            >
                                                                <InputLabel>
                                                                    Value
                                                                </InputLabel>

                                                                <Select
                                                                    label="Value"
                                                                    value={
                                                                        cell.value ===
                                                                        null
                                                                            ? ""
                                                                            : String(
                                                                                  cell.value,
                                                                              )
                                                                    }
                                                                    onChange={(
                                                                        event,
                                                                    ) =>
                                                                        updateCell(
                                                                            variable,
                                                                            year,
                                                                            normalizeValue(
                                                                                variable,
                                                                                event
                                                                                    .target
                                                                                    .value,
                                                                            ),
                                                                        )
                                                                    }
                                                                >
                                                                    {choices.map(
                                                                        (
                                                                            option,
                                                                        ) => (
                                                                            <MenuItem
                                                                                key={
                                                                                    option.value
                                                                                }
                                                                                value={
                                                                                    option.value
                                                                                }
                                                                            >
                                                                                {
                                                                                    option.label
                                                                                }
                                                                            </MenuItem>
                                                                        ),
                                                                    )}
                                                                </Select>
                                                            </FormControl>
                                                        </TableCell>
                                                    );
                                                }

                                                if (
                                                    variable.dataType ===
                                                    "BOOLEAN"
                                                ) {
                                                    const booleanSelectValue =
                                                        cell.status ===
                                                            "NOT_AVAILABLE"
                                                            ? "NOT_AVAILABLE"
                                                            : cell.status ===
                                                                "NOT_APPLICABLE"
                                                            ? "NOT_APPLICABLE"
                                                            : cell.value ===
                                                                null
                                                            ? ""
                                                            : cell.value
                                                                ? "true"
                                                                : "false";

                                                    return (
                                                        <TableCell
                                                            key={year}
                                                            align="center"
                                                            sx={{
                                                                width: 82,
                                                                minWidth: 82,
                                                                p: 0.5,
                                                            }}
                                                        >
                                                            <FormControl
                                                                size="small"
                                                                fullWidth
                                                            >
                                                                <InputLabel>
                                                                    Value
                                                                </InputLabel>

                                                                <Select
                                                                    label="Value"
                                                                    value={
                                                                        booleanSelectValue
                                                                    }
                                                                    onChange={(
                                                                        event,
                                                                    ) => {
                                                                        const selected =
                                                                            event
                                                                                .target
                                                                                .value;

                                                                        if (
                                                                            selected ===
                                                                            "NOT_AVAILABLE"
                                                                        ) {
                                                                            updateCell(
                                                                                variable,
                                                                                year,
                                                                                null,
                                                                                "NOT_AVAILABLE",
                                                                            );
                                                                            return;
                                                                        }

                                                                        if (
                                                                            selected ===
                                                                            "NOT_APPLICABLE"
                                                                        ) {
                                                                            updateCell(
                                                                                variable,
                                                                                year,
                                                                                null,
                                                                                "NOT_APPLICABLE",
                                                                            );
                                                                            return;
                                                                        }

                                                                        updateCell(
                                                                            variable,
                                                                            year,
                                                                            selected ===
                                                                                "true",
                                                                        );
                                                                    }}
                                                                >
                                                                    <MenuItem value="true">
                                                                        Yes
                                                                    </MenuItem>

                                                                    <MenuItem value="false">
                                                                        No
                                                                    </MenuItem>

                                                                    <MenuItem value="NOT_AVAILABLE">
                                                                        Not Available
                                                                    </MenuItem>

                                                                    <MenuItem value="NOT_APPLICABLE">
                                                                        Not Applicable
                                                                    </MenuItem>
                                                                </Select>
                                                            </FormControl>
                                                        </TableCell>
                                                    );
                                                }

                                                return (
                                                    <TableCell
                                                        key={year}
                                                        align="center"
                                                        sx={{
                                                            width: 110,
                                                            minWidth: 110,
                                                            p: 0.5,
                                                        }}
                                                    >
                                                        <TextField
                                                            size="small"
                                                            fullWidth
                                                            type={getInputType(
                                                                variable,
                                                            )}
                                                            value={getInputValue(
                                                                variable,
                                                                year,
                                                                cell,
                                                            )}
                                                            onChange={(
                                                                event,
                                                            ) => {
                                                                const rawValue =
                                                                    event.target.value;

                                                                const normalizedText =
                                                                    rawValue
                                                                        .trim()
                                                                        .toUpperCase();

                                                                const key =
                                                                    makeCellKey(
                                                                        variable.id,
                                                                        year,
                                                                    );

                                                                setInputValues(
                                                                    (current) => ({
                                                                        ...current,
                                                                        [key]:
                                                                            rawValue,
                                                                    }),
                                                                );

                                                                if (
                                                                    normalizedText ===
                                                                    "NA"
                                                                ) {
                                                                    updateCell(
                                                                        variable,
                                                                        year,
                                                                        null,
                                                                        "NOT_AVAILABLE",
                                                                        cell.selectedUnit ??
                                                                            getDefaultUnit(
                                                                                variable,
                                                                            ),
                                                                    );
                                                                    return;
                                                                }

                                                                if (
                                                                    normalizedText ===
                                                                    "N/A"
                                                                ) {
                                                                    updateCell(
                                                                        variable,
                                                                        year,
                                                                        null,
                                                                        "NOT_APPLICABLE",
                                                                        cell.selectedUnit ??
                                                                            getDefaultUnit(
                                                                                variable,
                                                                            ),
                                                                    );
                                                                    return;
                                                                }

                                                                const normalized =
                                                                    normalizeValue(
                                                                        variable,
                                                                        rawValue,
                                                                    );

                                                                /*
                                                                 * Keep intermediate text such as
                                                                 * "N" or "N/" visible without
                                                                 * changing the business cell yet.
                                                                 */
                                                                if (
                                                                    normalized ===
                                                                        null &&
                                                                    rawValue !==
                                                                        ""
                                                                ) {
                                                                    return;
                                                                }

                                                                updateCell(
                                                                    variable,
                                                                    year,
                                                                    normalized,
                                                                    "PROVIDED",
                                                                    cell.selectedUnit ??
                                                                        getDefaultUnit(
                                                                            variable,
                                                                        ),
                                                                );
                                                            }}
                                                            onPaste={(event) =>
                                                                handleSpreadsheetPaste(
                                                                    event,
                                                                    variable,
                                                                    year,
                                                                )
                                                            }
                                                            slotProps={{
                                                                htmlInput:
                                                                    variable.dataType ===
                                                                    "INTEGER"
                                                                        ? {
                                                                              step: 1,
                                                                          }
                                                                        : {
                                                                              step: "any",
                                                                          },
                                                            }}
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
