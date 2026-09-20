import { useState } from "react";
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
    createQuestionnaire,
} from "../api/questionnaireApi";

import type {
    RenderType,
} from "../types/questionnaire.types";

export default function CreateQuestionnairePage() {
    const navigate = useNavigate();

    const [code, setCode] = useState("");
    const [name, setName] = useState("");
    const [description, setDescription] =
        useState("");
    const [version, setVersion] =
        useState("1.0");
    const [
        defaultLanguage,
        setDefaultLanguage,
    ] = useState("en");

    const [renderType, setRenderType] =
        useState<RenderType>("FORM");

    const [isSubmitting, setIsSubmitting] =
        useState(false);

    const [error, setError] =
        useState<string | null>(null);

    async function handleSubmit(
        event: React.FormEvent<HTMLFormElement>,
    ) {
        event.preventDefault();

        try {
            setIsSubmitting(true);
            setError(null);

            const questionnaire =
                await createQuestionnaire({
                    code: code.trim(),
                    name: name.trim(),
                    description: description.trim(),
                    version: version.trim(),
                    defaultLanguage:
                        defaultLanguage.trim(),
                    renderType,
                });

            navigate(
                `/questionnaires/${questionnaire.id}`,
            );
        } catch (error) {
            console.error(
                "Failed to create questionnaire:",
                error,
            );

            setError(
                "Unable to create questionnaire.",
            );
        } finally {
            setIsSubmitting(false);
        }
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
                Create Questionnaire
            </Typography>

            <Typography
                color="text.secondary"
                sx={{
                    mb: 4,
                }}
            >
                Define the questionnaire information
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
                                onChange={(event) =>
                                    setRenderType(
                                        event.target.value as RenderType,
                                    )
                                }
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
                                        navigate(-1)
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
                                    Create Questionnaire
                                </Button>
                            </Stack>
                        </Stack>
                    </Box>
                </CardContent>
            </Card>
        </Box>
    );
}
