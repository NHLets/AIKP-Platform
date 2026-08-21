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
    Typography,
} from "@mui/material";

import { useEffect, useState } from "react";

import { updateCampaign } from "../api/campaignApi";

import type {
    Campaign,
} from "../types/campaign.types";

interface UpdateCampaignDialogProps {
    open: boolean;
    campaign: Campaign | null;
    onClose: () => void;
    onUpdated: (campaign: Campaign) => void;
}

interface FormState {
    name: string;
    description: string;
    startDate: string;
    endDate: string;
}

const initialFormState: FormState = {
    name: "",
    description: "",
    startDate: "",
    endDate: "",
};

export default function UpdateCampaignDialog({
    open,
    campaign,
    onClose,
    onUpdated,
}: UpdateCampaignDialogProps) {
    const [form, setForm] =
        useState<FormState>(initialFormState);

    const [submitting, setSubmitting] =
        useState(false);

    const [error, setError] =
        useState<string | null>(null);

    useEffect(() => {
        if (!campaign) {
            return;
        }

        setForm({
            name: campaign.name,
            description: campaign.description,
            startDate: campaign.startDate,
            endDate: campaign.endDate,
        });

        setError(null);
    }, [campaign, open]);

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

        setError(null);
        onClose();
    }

    async function handleSubmit(
        event: React.FormEvent<HTMLFormElement>,
    ) {
        event.preventDefault();

        if (!campaign) {
            return;
        }

        try {
            setSubmitting(true);
            setError(null);

            const updatedCampaign =
                await updateCampaign(
                    campaign.id,
                    {
                        name: form.name.trim(),
                        description:
                            form.description.trim(),
                        startDate: form.startDate,
                        endDate: form.endDate,
                    },
                );

            onUpdated(updatedCampaign);

            onClose();
        } catch (error) {
            console.error(
                "Failed to update campaign:",
                error,
            );

            setError(
                error instanceof Error
                    ? error.message
                    : "Unable to update campaign.",
            );
        } finally {
            setSubmitting(false);
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
                    Edit Campaign
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

                        <Box>
                            <Typography
                                variant="body2"
                                color="text.secondary"
                                sx={{
                                    mb: 0.5,
                                }}
                            >
                                Campaign Code
                            </Typography>

                            <Typography
                                variant="body1"
                                sx={{
                                    fontWeight: 600,
                                }}
                            >
                                {campaign?.code}
                            </Typography>
                        </Box>

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
                            autoFocus
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
                            ? "Saving..."
                            : "Save Changes"}
                    </Button>
                </DialogActions>
            </Box>
        </Dialog>
    );
}