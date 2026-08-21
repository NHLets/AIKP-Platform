import {
    Alert,
    Box,
    Button,
    CircularProgress,
    Dialog,
    DialogActions,
    DialogContent,
    DialogContentText,
    DialogTitle,
    Divider,
    Paper,
    Typography,
} from "@mui/material";

import {
    ArrowBack,
    Archive,
    CheckCircle,
    Delete,
    Edit,
    PlayArrow,
    TaskAlt,
} from "@mui/icons-material";

import {
    Link as RouterLink,
    useNavigate,
    useParams,
} from "react-router-dom";

import {
    useEffect,
    useState,
} from "react";

import {
    activateCampaign,
    archiveCampaign,
    completeCampaign,
    deleteCampaign,
    getCampaignById,
    planCampaign,
} from "../api/campaignApi";

import type {
    Campaign,
    CampaignStatus,
} from "../types/campaign.types";


function getStatusLabel(
    status: CampaignStatus,
): string {
    switch (status) {
        case "DRAFT":
            return "Draft";

        case "PLANNED":
            return "Planned";

        case "ACTIVE":
            return "Active";

        case "COMPLETED":
            return "Completed";

        case "ARCHIVED":
            return "Archived";

        default:
            return status;
    }
}


export default function CampaignDetailPage() {
    const { id } = useParams<{
        id: string;
    }>();

    const navigate = useNavigate();

    const [campaign, setCampaign] =
        useState<Campaign | null>(null);

    const [loading, setLoading] =
        useState(true);

    const [actionLoading, setActionLoading] =
        useState(false);

    const [error, setError] =
        useState<string | null>(null);

    const [deleteDialogOpen, setDeleteDialogOpen] =
        useState(false);


    useEffect(() => {
        async function loadCampaign() {
            if (!id) {
                setError("Campaign ID is missing.");
                setLoading(false);

                return;
            }

            try {
                setLoading(true);
                setError(null);

                const response =
                    await getCampaignById(id);

                setCampaign(response);
            } catch (err) {
                console.error(
                    "Failed to load campaign:",
                    err,
                );

                setError(
                    "Unable to load campaign.",
                );
            } finally {
                setLoading(false);
            }
        }

        void loadCampaign();
    }, [id]);


    async function handlePlanCampaign() {
        if (!campaign) {
            return;
        }

        try {
            setActionLoading(true);
            setError(null);

            const updatedCampaign =
                await planCampaign(campaign.id);

            setCampaign(updatedCampaign);
        } catch (err) {
            console.error(
                "Failed to plan campaign:",
                err,
            );

            setError(
                "Unable to plan campaign.",
            );
        } finally {
            setActionLoading(false);
        }
    }


    async function handleActivateCampaign() {
        if (!campaign) {
            return;
        }

        try {
            setActionLoading(true);
            setError(null);

            const updatedCampaign =
                await activateCampaign(campaign.id);

            setCampaign(updatedCampaign);
        } catch (err) {
            console.error(
                "Failed to activate campaign:",
                err,
            );

            setError(
                "Unable to activate campaign.",
            );
        } finally {
            setActionLoading(false);
        }
    }


    async function handleCompleteCampaign() {
        if (!campaign) {
            return;
        }

        try {
            setActionLoading(true);
            setError(null);

            const updatedCampaign =
                await completeCampaign(campaign.id);

            setCampaign(updatedCampaign);
        } catch (err) {
            console.error(
                "Failed to complete campaign:",
                err,
            );

            setError(
                "Unable to complete campaign.",
            );
        } finally {
            setActionLoading(false);
        }
    }


    async function handleArchiveCampaign() {
        if (!campaign) {
            return;
        }

        try {
            setActionLoading(true);
            setError(null);

            const updatedCampaign =
                await archiveCampaign(campaign.id);

            setCampaign(updatedCampaign);
        } catch (err) {
            console.error(
                "Failed to archive campaign:",
                err,
            );

            setError(
                "Unable to archive campaign.",
            );
        } finally {
            setActionLoading(false);
        }
    }


    function handleOpenDeleteDialog() {
        setDeleteDialogOpen(true);
    }


    function handleCloseDeleteDialog() {
        if (actionLoading) {
            return;
        }

        setDeleteDialogOpen(false);
    }


    async function handleDeleteCampaign() {
        if (!campaign) {
            return;
        }

        try {
            setActionLoading(true);
            setError(null);

            await deleteCampaign(campaign.id);

            navigate("/campaigns");
        } catch (err) {
            console.error(
                "Failed to delete campaign:",
                err,
            );

            setError(
                "Unable to delete campaign.",
            );

            setDeleteDialogOpen(false);
        } finally {
            setActionLoading(false);
        }
    }


    if (loading) {
        return (
            <Box
                sx={{
                    display: "flex",
                    justifyContent: "center",
                    alignItems: "center",
                    minHeight: 400,
                }}
            >
                <CircularProgress />
            </Box>
        );
    }


    if (error && !campaign) {
        return (
            <Box>
                <Button
                    component={RouterLink}
                    to="/campaigns"
                    startIcon={<ArrowBack />}
                    sx={{
                        mb: 3,
                    }}
                >
                    Back to Campaigns
                </Button>

                <Alert severity="error">
                    {error}
                </Alert>
            </Box>
        );
    }


    if (!campaign) {
        return (
            <Box>
                <Button
                    component={RouterLink}
                    to="/campaigns"
                    startIcon={<ArrowBack />}
                    sx={{
                        mb: 3,
                    }}
                >
                    Back to Campaigns
                </Button>

                <Alert severity="warning">
                    Campaign not found.
                </Alert>
            </Box>
        );
    }


    return (
        <>
            <Box
                sx={{
                    display: "flex",
                    flexDirection: "column",
                    gap: 4,
                }}
            >
                {/* HEADER */}

                <Box
                    sx={{
                        display: "flex",
                        justifyContent: "space-between",
                        alignItems: "flex-start",
                        flexWrap: "wrap",
                        gap: 3,
                    }}
                >
                    <Box>
                        <Button
                            component={RouterLink}
                            to="/campaigns"
                            startIcon={<ArrowBack />}
                            sx={{
                                mb: 2,
                            }}
                        >
                            Back to Campaigns
                        </Button>

                        <Typography
                            variant="h3"
                            component="h1"
                            sx={{
                                fontWeight: 700,
                            }}
                        >
                            {campaign.name}
                        </Typography>

                        <Typography
                            variant="h6"
                            color="text.secondary"
                            sx={{
                                mt: 1,
                            }}
                        >
                            Campaign Code: {campaign.code}
                        </Typography>
                    </Box>


                    {/* ACTION BUTTONS */}

                    <Box
                        sx={{
                            display: "flex",
                            flexDirection: "row",
                            flexWrap: "wrap",
                            gap: 1.5,
                            justifyContent: "flex-end",
                        }}
                    >
                        <Button
                            variant="outlined"
                            startIcon={<Edit />}
                            disabled={actionLoading}
                        >
                            Edit Campaign
                        </Button>


                        {campaign.status === "DRAFT" && (
                            <Button
                                variant="contained"
                                startIcon={<TaskAlt />}
                                onClick={
                                    handlePlanCampaign
                                }
                                disabled={actionLoading}
                            >
                                {actionLoading
                                    ? "Planning..."
                                    : "Plan Campaign"}
                            </Button>
                        )}


                        {campaign.status === "PLANNED" && (
                            <Button
                                variant="contained"
                                color="success"
                                startIcon={<PlayArrow />}
                                onClick={
                                    handleActivateCampaign
                                }
                                disabled={actionLoading}
                            >
                                {actionLoading
                                    ? "Activating..."
                                    : "Activate Campaign"}
                            </Button>
                        )}


                        {campaign.status === "ACTIVE" && (
                            <Button
                                variant="contained"
                                color="warning"
                                startIcon={<CheckCircle />}
                                onClick={
                                    handleCompleteCampaign
                                }
                                disabled={actionLoading}
                            >
                                {actionLoading
                                    ? "Completing..."
                                    : "Complete Campaign"}
                            </Button>
                        )}


                        {campaign.status === "COMPLETED" && (
                            <Button
                                variant="contained"
                                color="secondary"
                                startIcon={<Archive />}
                                onClick={
                                    handleArchiveCampaign
                                }
                                disabled={actionLoading}
                            >
                                {actionLoading
                                    ? "Archiving..."
                                    : "Archive Campaign"}
                            </Button>
                        )}


                        <Button
                            variant="outlined"
                            color="error"
                            startIcon={<Delete />}
                            onClick={
                                handleOpenDeleteDialog
                            }
                            disabled={actionLoading}
                        >
                            Delete
                        </Button>
                    </Box>
                </Box>


                {/* ERROR MESSAGE */}

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


                {/* CAMPAIGN OVERVIEW */}

                <Paper
                    variant="outlined"
                    sx={{
                        p: 4,
                    }}
                >
                    <Typography
                        variant="h5"
                        component="h2"
                        sx={{
                            fontWeight: 600,
                            textAlign: "center",
                            mb: 3,
                        }}
                    >
                        Campaign Overview
                    </Typography>

                    <Divider
                        sx={{
                            mb: 4,
                        }}
                    />


                    {/* DESCRIPTION */}

                    <Box
                        sx={{
                            textAlign: "center",
                            mb: 4,
                        }}
                    >
                        <Typography
                            variant="body1"
                            color="text.secondary"
                            sx={{
                                mb: 1,
                            }}
                        >
                            Description
                        </Typography>

                        <Typography
                            variant="body1"
                        >
                            {campaign.description ||
                                "No description provided."}
                        </Typography>
                    </Box>


                    {/* CAMPAIGN INFORMATION */}

                    <Box
                        sx={{
                            display: "grid",
                            gridTemplateColumns: {
                                xs: "1fr",
                                md: "repeat(3, 1fr)",
                            },
                            gap: 4,
                            mb: 4,
                        }}
                    >
                        <Box
                            sx={{
                                textAlign: "center",
                            }}
                        >
                            <Typography
                                variant="body1"
                                color="text.secondary"
                                sx={{
                                    mb: 1,
                                }}
                            >
                                Start Date
                            </Typography>

                            <Typography
                                variant="body1"
                                sx={{
                                    fontWeight: 600,
                                }}
                            >
                                {campaign.startDate}
                            </Typography>
                        </Box>


                        <Box
                            sx={{
                                textAlign: "center",
                            }}
                        >
                            <Typography
                                variant="body1"
                                color="text.secondary"
                                sx={{
                                    mb: 1,
                                }}
                            >
                                End Date
                            </Typography>

                            <Typography
                                variant="body1"
                                sx={{
                                    fontWeight: 600,
                                }}
                            >
                                {campaign.endDate}
                            </Typography>
                        </Box>


                        <Box
                            sx={{
                                textAlign: "center",
                            }}
                        >
                            <Typography
                                variant="body1"
                                color="text.secondary"
                                sx={{
                                    mb: 1,
                                }}
                            >
                                Status
                            </Typography>

                            <Typography
                                variant="body1"
                                sx={{
                                    fontWeight: 700,
                                }}
                            >
                                {getStatusLabel(
                                    campaign.status,
                                )}
                            </Typography>
                        </Box>
                    </Box>


                    {/* ACTIVE STATUS */}

                    <Box
                        sx={{
                            textAlign: "center",
                        }}
                    >
                        <Typography
                            variant="body1"
                            color="text.secondary"
                            sx={{
                                mb: 1,
                            }}
                        >
                            Active
                        </Typography>

                        <Typography
                            variant="body1"
                            sx={{
                                fontWeight: 600,
                            }}
                        >
                            {campaign.active
                                ? "Yes"
                                : "No"}
                        </Typography>
                    </Box>
                </Paper>
            </Box>


            {/* DELETE CONFIRMATION */}

            <Dialog
                open={deleteDialogOpen}
                onClose={handleCloseDeleteDialog}
            >
                <DialogTitle>
                    Delete Campaign?
                </DialogTitle>

                <DialogContent>
                    <DialogContentText>
                        Are you sure you want to permanently
                        delete the campaign{" "}
                        <strong>
                            {campaign.name}
                        </strong>
                        ?
                    </DialogContentText>

                    <DialogContentText
                        sx={{
                            mt: 2,
                        }}
                    >
                        This action cannot be undone.
                    </DialogContentText>
                </DialogContent>

                <DialogActions>
                    <Button
                        onClick={
                            handleCloseDeleteDialog
                        }
                        disabled={actionLoading}
                    >
                        Cancel
                    </Button>

                    <Button
                        color="error"
                        variant="contained"
                        onClick={
                            handleDeleteCampaign
                        }
                        disabled={actionLoading}
                    >
                        {actionLoading
                            ? "Deleting..."
                            : "Delete Campaign"}
                    </Button>
                </DialogActions>
            </Dialog>
        </>
    );
}