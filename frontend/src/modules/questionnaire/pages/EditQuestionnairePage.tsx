import {
    useEffect,
    useState,
} from "react";
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
    CircularProgress,
    MenuItem,
    Stack,
    TextField,
    Typography,
} from "@mui/material";

import {
    getQuestionnaire,
    updateQuestionnaire,
} from "../api/questionnaireApi";

import type {
    RenderType,
} from "../types/questionnaire.types";

export default function EditQuestionnairePage() {
    const navigate = useNavigate();
    const { id } = useParams();

    const [code, setCode] = useState("");
    const [name, setName] = useState("");
    const [description, setDescription] =
        useState("");
    const [version, setVersion] =
        useState("");
    const [
        defaultLanguage,
        setDefaultLanguage,
    ] = useState("");

    const [renderType, setRenderType] =
        useState<RenderType>("FORM");

    const [isLoading, setIsLoading] =
        useState(true);

    const [isSubmitting, setIsSubmitting] =
        useState(false);

    const [error, setError] =
        useState<string | null>(null);

    useEffect(() => {
        async function loadQuestionnaire() {
            if (!id) {
                setError(
                    "Questionnaire identifier is missing.",
                );
                setIsLoading(false);
                return;
            }

            try {
                setIsLoading(true);
                setError(null);

                const questionnaire =
                    await getQuestionnaire(id);

                setCode(questionnaire.code);
                setName(questionnaire.name);
                setDescription(
                    questionnaire.description,
                );
                setVersion(questionnaire.version);
                setDefaultLanguage(
                    questionnaire.defaultLanguage,
                );
                setRenderType(
                    questionnaire.renderType,
                );
            } catch (error) {
                console.error(
                    "Failed to load questionnaire:",
                    error,
                );

                setError(
                    "Unable to load questionnaire.",
                );
            } finally {
                setIsLoading(false);
            }
        }

        void loadQuestionnaire();
    }, [id]);

    async function handleSubmit(
        event: React.FormEvent<HTMLFormElement>,
    ) {
        event.preventDefault();

        if (!id) {
            return;
        }

        try {
            setIsSubmitting(true);
            setError(null);

            await updateQuestionnaire(id, {
                code: code.trim(),
                name: name.trim(),
                description: description.trim(),
                version: version.trim(),
                defaultLanguage:
                    defaultLanguage.trim(),
                renderType,
            });

            navigate(`/questionnaires/${id}`);
        } catch (error) {
            console.error(
                "Failed to update questionnaire:",
                error,
            );

            setError(
                "Unable to update questionnaire.",
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

    if (error && !id) {
        return (
            <Alert severity="error">
                {error}
            </Alert>
        );
    }

    return (
        <Box>
            <Button
                onClick={() =>
                    navigate(`/questionnaires/${id}`)
                }
                sx={{
                    mb: 3,
                }}
            >
                Back to Questionnaire
            </Button>

            <Typography
                variant="h4"
                sx={{
                    fontWeight: 700,
                    mb: 1,
                }}
            >
                Edit Questionnaire
            </Typography>

            <Typography
                color="text.secondary"
                sx={{
                    mb: 4,
                }}
            >
                Update the questionnaire information
                and rendering type.
            </Typography>

            <Card>
                <CardContent>
                    {error && (
                        <Alert
                            severity="error"
                            sx={{
                                mb: 3,
                            }}
                        >
                            {error}
                        </Alert>
                    )}

                    <Box
                        component="form"
                        onSubmit={handleSubmit}
                    >
                        <Stack spacing={3}>
                            <TextField
                                label="Code"
                                value={code}
                                onChange={(event) =>
                                    setCode(
                                        event.target.value,
                                    )
                                }
                                required
                                slotProps={{
                                    htmlInput: {
                                        maxLength: 50,
                                        pattern:
                                            "[A-Z0-9_]+",
                                    },
                                }}
                                helperText={
                                    "Uppercase letters, numbers and underscores only."
                                }
                            />

                            <TextField
                                label="Name"
                                value={name}
                                onChange={(event) =>
                                    setName(
                                        event.target.value,
                                    )
                                }
                                required
                                slotProps={{
                                    htmlInput: {
                                        maxLength: 150,
                                    },
                                }}
                            />

                            <TextField
                                label="Description"
                                value={description}
                                onChange={(event) =>
                                    setDescription(
                                        event.target.value,
                                    )
                                }
                                required
                                multiline
                                minRows={4}
                                slotProps={{
                                    htmlInput: {
                                        maxLength: 1000,
                                    },
                                }}
                            />

                            <TextField
                                label="Version"
                                value={version}
                                onChange={(event) =>
                                    setVersion(
                                        event.target.value,
                                    )
                                }
                                required
                                slotProps={{
                                    htmlInput: {
                                        maxLength: 20,
                                        pattern:
                                            "\\d+(\\.\\d+)*",
                                    },
                                }}
                                helperText={
                                    "Example: 1.0 or 2.1.3"
                                }
                            />

                            <TextField
                                label="Default Language"
                                value={defaultLanguage}
                                onChange={(event) =>
                                    setDefaultLanguage(
                                        event.target.value,
                                    )
                                }
                                required
                                slotProps={{
                                    htmlInput: {
                                        maxLength: 2,
                                        pattern:
                                            "[a-z]{2}",
                                    },
                                }}
                                helperText={
                                    "Two-letter lowercase language code, e.g. en or fr."
                                }
                            />

                            <TextField
                                select
                                label="Render Type"
                                value={renderType}
                                onChange={(event) => {
                                    const value =
                                        event.target.value;

                                    if (
                                        value === "FORM" ||
                                        value === "SPREADSHEET" ||
                                        value === "HYBRID"
                                    ) {
                                        setRenderType(value);
                                    }
                                }}
                                required
                            >
                                <MenuItem value="FORM">
                                    Form
                                </MenuItem>

                                <MenuItem value="SPREADSHEET">
                                    Spreadsheet
                                </MenuItem>

                                <MenuItem value="HYBRID">
                                    Hybrid
                                </MenuItem>
                            </TextField>

                            <Stack
                                direction="row"
                                spacing={2}
                                sx={{
                                    justifyContent:
                                        "flex-end",
                                }}
                            >
                                <Button
                                    onClick={() =>
                                        navigate(
                                            `/questionnaires/${id}`,
                                        )
                                    }
                                    disabled={
                                        isSubmitting
                                    }
                                >
                                    Cancel
                                </Button>

                                <Button
                                    type="submit"
                                    variant="contained"
                                    disabled={
                                        isSubmitting
                                    }
                                    startIcon={
                                        isSubmitting ? (
                                            <CircularProgress
                                                size={20}
                                            />
                                        ) : undefined
                                    }
                                >
                                    Save Changes
                                </Button>
                            </Stack>
                        </Stack>
                    </Box>
                </CardContent>
            </Card>
        </Box>
    );
}
