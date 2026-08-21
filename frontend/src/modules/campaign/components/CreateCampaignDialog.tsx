
import axios from "axios";
import {
    Alert,
    Box,
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
    Stack,
    TextField,
} from "@mui/material";

import { useState } from "react";

import { createCampaign } from "../api/campaignApi";

interface CreateCampaignDialogProps {
    open: boolean;
    onClose: () => void;
    onCreated: () => void;
}

interface FormState {
    code: string;
    name: string;
    description: string;
    startDate: string;
    endDate: string;
}

const initialFormState: FormState = {
    code: "",
    name: "",
    description: "",
    startDate: "",
    endDate: "",
};

export default function CreateCampaignDialog({
    open,
    onClose,
    onCreated,
}: CreateCampaignDialogProps) {
    const [form, setForm] =
        useState<FormState>(initialFormState);

    const [submitting, setSubmitting] =
        useState(false);

    const [error, setError] =
        useState<string | null>(null);

    function updateField(
        field: keyof FormState,
        value: string,
    ) {
        setForm((current) => ({
            ...current,
            [field]: value,
        }));
    }

    function handleClose() {
        if (submitting) {
            return;
        }

        setForm(initialFormState);
        setError(null);

        onClose();
    }

    async function handleSubmit(
        event: React.FormEvent<HTMLFormElement>,
    ) {
        event.preventDefault();

        try {
            setSubmitting(true);
            setError(null);

            await createCampaign({
                code: form.code.trim(),
                name: form.name.trim(),
                description: form.description.trim(),
                startDate: form.startDate,
                endDate: form.endDate,
            });

            setForm(initialFormState);

            onCreated();

            onClose();
        } catch (error) {
    console.error(
        "Failed to create campaign:",
        error,
    );

    if (axios.isAxiosError(error)) {
        console.error(
            "Axios error message:",
            error.message,
        );

        console.error(
            "Axios error code:",
            error.code,
        );

        console.error(
            "Axios response:",
            error.response?.data,
        );

        console.error(
            "Axios response status:",
            error.response?.status,
        );

        console.error(
            "Axios request:",
            error.request,
        );

        setError(
            error.response?.data?.detail ??
            error.message ??
            "Unable to create campaign.",
        );
    } else {
        setError(
            error instanceof Error
                ? error.message
                : "Unable to create campaign.",
        );
    }
}
    }

    return (
        <Dialog
            open={open}
            onClose={handleClose}
            fullWidth
            maxWidth="sm"
        >
            <Box
                component="form"
                onSubmit={handleSubmit}
            >
                <DialogTitle>
                    Create New Campaign
                </DialogTitle>

                <DialogContent>
                    <Stack
                        spacing={2}
                        sx={{
                            pt: 1,
                        }}
                    >
                        {error && (
                            <Alert severity="error">
                                {error}
                            </Alert>
                        )}

                        <TextField
                            label="Campaign Code"
                            value={form.code}
                            onChange={(event) =>
                                updateField(
                                    "code",
                                    event.target.value,
                                )
                            }
                            required
                            fullWidth
                            autoFocus
                        />

                        <TextField
                            label="Campaign Name"
                            value={form.name}
                            onChange={(event) =>
                                updateField(
                                    "name",
                                    event.target.value,
                                )
                            }
                            required
                            fullWidth
                        />

                        <TextField
                            label="Description"
                            value={form.description}
                            onChange={(event) =>
                                updateField(
                                    "description",
                                    event.target.value,
                                )
                            }
                            required
                            fullWidth
                            multiline
                            minRows={3}
                        />

                        <TextField
                            label="Start Date"
                            type="date"
                            value={form.startDate}
                            onChange={(event) =>
                                updateField(
                                    "startDate",
                                    event.target.value,
                                )
                            }
                            required
                            fullWidth
                            slotProps={{
                                inputLabel: {
                                    shrink: true,
                                },
                            }}
                        />

                        <TextField
                            label="End Date"
                            type="date"
                            value={form.endDate}
                            onChange={(event) =>
                                updateField(
                                    "endDate",
                                    event.target.value,
                                )
                            }
                            required
                            fullWidth
                            slotProps={{
                                inputLabel: {
                                    shrink: true,
                                },
                            }}
                        />
                    </Stack>
                </DialogContent>

                <DialogActions
                    sx={{
                        px: 3,
                        pb: 2,
                    }}
                >
                    <Button
                        onClick={handleClose}
                        disabled={submitting}
                    >
                        Cancel
                    </Button>

                    <Button
                        type="submit"
                        variant="contained"
                        disabled={submitting}
                    >
                        {submitting
                            ? "Creating..."
                            : "Create Campaign"}
                    </Button>
                </DialogActions>
            </Box>
        </Dialog>
    );
}