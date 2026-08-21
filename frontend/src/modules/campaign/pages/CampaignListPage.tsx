import { useEffect, useState } from "react";

import AddIcon from "@mui/icons-material/Add";
import RefreshIcon from "@mui/icons-material/Refresh";

import {
    Alert,
    Box,
    Button,
    Chip,
    CircularProgress,
    IconButton,
    Paper,
    Stack,
    Tooltip,
    Typography,
} from "@mui/material";

import { Link as RouterLink } from "react-router-dom";

import { getCampaigns } from "../api/campaignApi";

import CreateCampaignDialog from "../components/CreateCampaignDialog";

import type {
    CampaignStatus,
    CampaignSummary,
} from "../types/campaign.types";

function getStatusColor(
    status: CampaignStatus,
): "default" | "primary" | "success" | "warning" | "error" {
    switch (status) {
        case "ACTIVE":
            return "success";

        case "PLANNED":
            return "primary";

        case "COMPLETED":
            return "default";

        case "ARCHIVED":
            return "error";

        case "DRAFT":
        default:
            return "warning";
    }
}

export default function CampaignListPage() {
    const [campaigns, setCampaigns] =
        useState<CampaignSummary[]>([]);

    const [loading, setLoading] =
        useState(true);

    const [error, setError] =
        useState<string | null>(null);

    const [createDialogOpen, setCreateDialogOpen] =
        useState(false);

    async function loadCampaigns() {
        try {
            setLoading(true);
            setError(null);

            const data = await getCampaigns();

            setCampaigns(data);
        } catch (error) {
            console.error(
                "Failed to load campaigns:",
                error,
            );

            setError(
                "Unable to load campaigns. Please verify that the backend is running.",
            );
        } finally {
            setLoading(false);
        }
    }

    useEffect(() => {
        void loadCampaigns();
    }, []);

    function handleCreateCampaign() {
        setCreateDialogOpen(true);
    }

    function handleCampaignCreated() {
        void loadCampaigns();
    }

    return (
        <Stack spacing={3}>
            <Box
                sx={{
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "space-between",
                }}
            >
                <Box>
                    <Typography
                        variant="h4"
                        component="h1"
                        sx={{
                            fontWeight: 700,
                        }}
                    >
                        Campaign Management
                    </Typography>

                    <Typography
                        color="text.secondary"
                        sx={{
                            mt: 0.5,
                        }}
                    >
                        Manage AIKP data collection campaigns and monitor their lifecycle.
                    </Typography>
                </Box>

                <Stack
                    direction="row"
                    spacing={1}
                    sx={{
                        alignItems: "center",
                    }}
                >
                    <Tooltip title="Refresh campaigns">
                        <IconButton
                            onClick={() => {
                                void loadCampaigns();
                            }}
                            disabled={loading}
                        >
                            <RefreshIcon />
                        </IconButton>
                    </Tooltip>

                    <Button
                        variant="contained"
                        startIcon={<AddIcon />}
                        onClick={handleCreateCampaign}
                    >
                        New Campaign
                    </Button>
                </Stack>
            </Box>

            {error && (
                <Alert
                    severity="error"
                    action={
                        <Button
                            color="inherit"
                            size="small"
                            onClick={() => {
                                void loadCampaigns();
                            }}
                        >
                            Retry
                        </Button>
                    }
                >
                    {error}
                </Alert>
            )}

            <Paper
                variant="outlined"
                sx={{
                    overflow: "hidden",
                }}
            >
                {loading && (
                    <Box
                        sx={{
                            py: 8,
                            display: "flex",
                            justifyContent: "center",
                        }}
                    >
                        <CircularProgress />
                    </Box>
                )}

                {!loading &&
                    !error &&
                    campaigns.length === 0 && (
                        <Box
                            sx={{
                                py: 8,
                                textAlign: "center",
                            }}
                        >
                            <Typography
                                variant="h6"
                                sx={{
                                    mb: 1,
                                }}
                            >
                                No campaigns found
                            </Typography>

                            <Typography
                                color="text.secondary"
                            >
                                Create the first AIKP data collection campaign to get started.
                            </Typography>
                        </Box>
                    )}

                {!loading &&
                    !error &&
                    campaigns.length > 0 && (
                        <Box
                            sx={{
                                width: "100%",
                                overflowX: "auto",
                            }}
                        >
                            <Box
                                component="table"
                                sx={{
                                    width: "100%",
                                    borderCollapse: "collapse",

                                    "& th": {
                                        textAlign: "left",
                                        p: 2,
                                        bgcolor: "action.hover",
                                        fontWeight: 600,
                                        borderBottom: 1,
                                        borderColor: "divider",
                                    },

                                    "& td": {
                                        p: 2,
                                        borderBottom: 1,
                                        borderColor: "divider",
                                    },

                                    "& tbody tr:last-child td": {
                                        borderBottom: 0,
                                    },

                                    "& tbody tr:hover": {
                                        bgcolor: "action.hover",
                                    },
                                }}
                            >
                                <thead>
                                    <tr>
                                        <th>Code</th>
                                        <th>Campaign</th>
                                        <th>Period</th>
                                        <th>Status</th>
                                    </tr>
                                </thead>

                                <tbody>
                                    {campaigns.map(
                                        (campaign) => (
                                            <tr
                                                key={
                                                    campaign.id
                                                }
                                            >
                                                <td>
                                                    <Typography
                                                        sx={{
                                                            fontWeight: 600,
                                                        }}
                                                    >
                                                        {
                                                            campaign.code
                                                        }
                                                    </Typography>
                                                </td>

                                                <td>
                                                    <Typography
                                                        component={
                                                            RouterLink
                                                        }
                                                        to={`/campaigns/${campaign.id}`}
                                                        sx={{
                                                            fontWeight: 600,
                                                            color: "primary.main",
                                                            textDecoration:
                                                                "none",

                                                            "&:hover": {
                                                                textDecoration:
                                                                    "underline",
                                                            },
                                                        }}
                                                    >
                                                        {
                                                            campaign.name
                                                        }
                                                    </Typography>
                                                </td>

                                                <td>
                                                    <Typography>
                                                        {
                                                            campaign.startDate
                                                        }
                                                    </Typography>

                                                    <Typography
                                                        variant="body2"
                                                        color="text.secondary"
                                                    >
                                                        to{" "}
                                                        {
                                                            campaign.endDate
                                                        }
                                                    </Typography>
                                                </td>

                                                <td>
                                                    <Chip
                                                        label={
                                                            campaign.status
                                                        }
                                                        color={getStatusColor(
                                                            campaign.status,
                                                        )}
                                                        size="small"
                                                    />
                                                </td>
                                            </tr>
                                        ),
                                    )}
                                </tbody>
                            </Box>
                        </Box>
                    )}
            </Paper>

            <CreateCampaignDialog
                open={createDialogOpen}
                onClose={() => {
                    setCreateDialogOpen(false);
                }}
                onCreated={handleCampaignCreated}
            />
        </Stack>
    );
}