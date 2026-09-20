import {
    useEffect,
    useMemo,
    useState,
} from "react";
import { useNavigate } from "react-router-dom";

import {
    Alert,
    Box,
    Button,
    Chip,
    CircularProgress,
    MenuItem,
    Paper,
    Stack,
    Table,
    TableBody,
    TableCell,
    TableHead,
    TablePagination,
    TableRow,
    TextField,
    Typography,
} from "@mui/material";

import { getQuestionnaires } from
    "../api/questionnaireApi";

import type {
    QuestionnaireSummary,
} from
    "../types/questionnaire.types";

function getStatusColor(
    status: string,
):
    | "default"
    | "primary"
    | "secondary"
    | "success"
    | "warning"
    | "error" {

    switch (status) {
        case "PUBLISHED":
            return "success";

        case "APPROVED":
            return "primary";

        case "UNDER_REVIEW":
            return "warning";

        case "ARCHIVED":
            return "default";

        case "DRAFT":
        default:
            return "secondary";
    }
}

export default function QuestionnaireListPage() {
    const navigate = useNavigate();

    const [questionnaires, setQuestionnaires] =
        useState<QuestionnaireSummary[]>([]);

    const [searchTerm, setSearchTerm] =
        useState("");

    const [sortOption, setSortOption] =
        useState("code-asc");

    const [page, setPage] =
        useState(0);

    const [rowsPerPage, setRowsPerPage] =
        useState(10);

    const [isLoading, setIsLoading] =
        useState(true);

    const [error, setError] =
        useState<string | null>(null);

    async function loadQuestionnaires() {
        try {
            setIsLoading(true);
            setError(null);

            const data =
                await getQuestionnaires();

            setQuestionnaires(data);
        } catch (error) {
            console.error(
                "Failed to load questionnaires:",
                error,
            );

            setError(
                "Unable to load questionnaires.",
            );
        } finally {
            setIsLoading(false);
        }
    }

    useEffect(() => {
        void loadQuestionnaires();
    }, []);

    const filteredQuestionnaires =
        useMemo(() => {
            const normalizedSearchTerm =
                searchTerm
                    .trim()
                    .toLowerCase();

            return questionnaires
                .filter((item) => {
                    if (!normalizedSearchTerm) {
                        return true;
                    }

                    return [
                        item.code,
                        item.name,
                        item.version,
                        item.status,
                    ].some((value) =>
                        value
                            .toLowerCase()
                            .includes(
                                normalizedSearchTerm,
                            ),
                    );
                })
                .sort((a, b) => {
                    switch (sortOption) {
                        case "code-desc":
                            return b.code.localeCompare(
                                a.code,
                            );

                        case "name-asc":
                            return a.name.localeCompare(
                                b.name,
                            );

                        case "name-desc":
                            return b.name.localeCompare(
                                a.name,
                            );

                        case "version-asc":
                            return a.version.localeCompare(
                                b.version,
                            );

                        case "version-desc":
                            return b.version.localeCompare(
                                a.version,
                            );

                        case "status-asc":
                            return a.status.localeCompare(
                                b.status,
                            );

                        case "status-desc":
                            return b.status.localeCompare(
                                a.status,
                            );

                        case "code-asc":
                        default:
                            return a.code.localeCompare(
                                b.code,
                            );
                    }
                });
        }, [
            questionnaires,
            searchTerm,
            sortOption,
        ]);

    const paginatedQuestionnaires =
        filteredQuestionnaires.slice(
            page * rowsPerPage,
            page * rowsPerPage + rowsPerPage,
        );

    useEffect(() => {
        setPage(0);
    }, [
        searchTerm,
        sortOption,
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
                        Questionnaires
                    </Typography>

                    <Typography
                        color="text.secondary"
                    >
                        Manage AIKP questionnaires
                    </Typography>
                </Box>

                <Button
                    variant="contained"
                    onClick={() =>
                        navigate("/questionnaires/new")
                    }
                >
                    Create Questionnaire
                </Button>
            </Stack>

            <Stack
                direction="row"
                spacing={2}
                sx={{
                    mb: 3,
                }}
            >
                <TextField
                    fullWidth
                    label="Search questionnaires"
                    placeholder="Code, name, version or status"
                    value={searchTerm}
                    onChange={(event) =>
                        setSearchTerm(
                            event.target.value,
                        )
                    }
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
                    sx={{
                        minWidth: 220,
                    }}
                >
                    <MenuItem value="code-asc">
                        Code A–Z
                    </MenuItem>

                    <MenuItem value="code-desc">
                        Code Z–A
                    </MenuItem>

                    <MenuItem value="name-asc">
                        Name A–Z
                    </MenuItem>

                    <MenuItem value="name-desc">
                        Name Z–A
                    </MenuItem>

                    <MenuItem value="version-asc">
                        Version ascending
                    </MenuItem>

                    <MenuItem value="version-desc">
                        Version descending
                    </MenuItem>

                    <MenuItem value="status-asc">
                        Status A–Z
                    </MenuItem>

                    <MenuItem value="status-desc">
                        Status Z–A
                    </MenuItem>
                </TextField>
            </Stack>

            <Paper>
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell>
                                Code
                            </TableCell>

                            <TableCell>
                                Name
                            </TableCell>

                            <TableCell>
                                Version
                            </TableCell>

                            <TableCell>
                                Status
                            </TableCell>

                            <TableCell>
                                Active
                            </TableCell>
                        </TableRow>
                    </TableHead>

                    <TableBody>
                        {paginatedQuestionnaires.map(
                            (item) => (
                                <TableRow
                                    key={item.id}
                                    hover
                                    onClick={() =>
                                        navigate(
                                            `/questionnaires/${item.id}`,
                                        )
                                    }
                                    sx={{
                                        cursor: "pointer",
                                    }}
                                >
                                    <TableCell>
                                        {item.code}
                                    </TableCell>

                                    <TableCell>
                                        {item.name}
                                    </TableCell>

                                    <TableCell>
                                        {item.version}
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

                                    <TableCell>
                                        <Chip
                                            label={
                                                item.active
                                                    ? "Active"
                                                    : "Inactive"
                                            }
                                            color={
                                                item.active
                                                    ? "success"
                                                    : "default"
                                            }
                                            size="small"
                                        />
                                    </TableCell>
                                </TableRow>
                            ),
                        )}

                        {paginatedQuestionnaires.length ===
                            0 && (
                            <TableRow>
                                <TableCell
                                    colSpan={5}
                                    align="center"
                                >
                                    No questionnaires found.
                                </TableCell>
                            </TableRow>
                        )}
                    </TableBody>
                </Table>

                <TablePagination
                    component="div"
                    count={
                        filteredQuestionnaires.length
                    }
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
            </Paper>
        </Box>
    );
}
