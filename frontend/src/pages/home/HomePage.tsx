import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import CampaignOutlinedIcon from "@mui/icons-material/CampaignOutlined";
import TaskAltIcon from "@mui/icons-material/TaskAlt";
import PublicOutlinedIcon from "@mui/icons-material/PublicOutlined";
import TrendingUpOutlinedIcon from "@mui/icons-material/TrendingUpOutlined";

import {
    Alert,
    Box,
    Card,
    CardContent,
    Chip,
    CircularProgress,
    Divider,
    Grid,
    LinearProgress,
    Stack,
    Typography,
} from "@mui/material";

import { useDashboard } from "@/modules/dashboard/hooks/useDashboard";
import { getCampaigns } from "@/modules/campaign/api/campaignApi";

import type {
    CampaignSummary,
} from "@/modules/campaign/types/campaign.types";

interface DashboardCardProps {
    title: string;
    value: string;
    description: string;
    icon: React.ReactNode;
}

function DashboardCard({
    title,
    value,
    description,
    icon,
}: DashboardCardProps) {
    return (
        <Card
            sx={{
                height: "100%",
            }}
        >
            <CardContent>
                <Stack
                    direction="row"
                    sx={{
                        justifyContent: "space-between",
                        alignItems: "center",
                    }}
                >
                    <Box>
                        <Typography
                            variant="body2"
                            color="text.secondary"
                        >
                            {title}
                        </Typography>

                        <Typography
                            variant="h4"
                            sx={{
                                mt: 1,
                                fontWeight: 700,
                            }}
                        >
                            {value}
                        </Typography>
                    </Box>

                    <Box
                        sx={{
                            display: "flex",
                            alignItems: "center",
                            justifyContent: "center",
                            width: 48,
                            height: 48,
                            borderRadius: 2,
                            bgcolor: "action.hover",
                        }}
                    >
                        {icon}
                    </Box>
                </Stack>

                <Typography
                    variant="body2"
                    color="text.secondary"
                    sx={{
                        mt: 2,
                    }}
                >
                    {description}
                </Typography>
            </CardContent>
        </Card>
    );
}

export default function HomePage() {
    const navigate = useNavigate();

    const {
        data: dashboard,
        isLoading,
        error,
    } = useDashboard();

    const [activeCampaign, setActiveCampaign] =
        useState<CampaignSummary | null>(null);

    useEffect(() => {
        async function loadActiveCampaign() {
            try {
                const campaigns =
                    await getCampaigns();

                const campaign =
                    campaigns.find(
                        (item) =>
                            item.status === "ACTIVE",
                    ) ?? null;

                setActiveCampaign(campaign);
            } catch (error) {
                console.error(
                    "Failed to load active campaign:",
                    error,
                );

                setActiveCampaign(null);
            }
        }

        void loadActiveCampaign();
    }, []);

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

    if (error || !dashboard) {
        return (
            <Alert severity="error">
                Unable to load dashboard data.
            </Alert>
        );
    }

    return (
        <Box>
            <Box
                sx={{
                    mb: 4,
                }}
            >
                <Typography
                    variant="h4"
                    sx={{
                        fontWeight: 700,
                    }}
                >
                    AIKP Dashboard
                </Typography>

                <Typography
                    color="text.secondary"
                    sx={{
                        mt: 1,
                    }}
                >
                    Overview of AIKP infrastructure data collection activities.
                </Typography>
            </Box>

            <Grid
                container
                spacing={3}
            >
                <Grid
                    size={{
                        xs: 12,
                        sm: 6,
                        lg: 3,
                    }}
                >
                    <Box
                        onClick={() => navigate("/campaigns")}
                        sx={{
                            cursor: "pointer",
                            height: "100%",
                        }}
                    >
                        <DashboardCard
                            title="Active Campaign"
                            value={String(dashboard.activeCampaigns)}
                            description="Current data collection campaign"
                            icon={<CampaignOutlinedIcon />}
                        />
                    </Box>
                </Grid>

                <Grid
                    size={{
                        xs: 12,
                        sm: 6,
                        lg: 3,
                    }}
                >
                    <Box
                        onClick={() => navigate("/countries")}
                        sx={{
                            cursor: "pointer",
                            height: "100%",
                        }}
                    >
                        <DashboardCard
                            title="Countries"
                            value={String(dashboard.totalCountries)}
                            description="Countries participating in AIKP"
                            icon={<PublicOutlinedIcon />}
                        />
                    </Box>
                </Grid>

                <Grid
                    size={{
                        xs: 12,
                        sm: 6,
                        lg: 3,
                    }}
                >
                    <Box
                        onClick={() => navigate("/submissions")}
                        sx={{
                            cursor: "pointer",
                            height: "100%",
                        }}
                    >
                        <DashboardCard
                            title="Collection Progress"
                            value={`${
                                dashboard.totalDataCollections === 0
                                    ? 0
                                    : Math.round(
                                        (dashboard.validatedDataCollections /
                                            dashboard.totalDataCollections) *
                                            100,
                                    )
                            }%`}
                            description="Overall data collection progress"
                            icon={<TrendingUpOutlinedIcon />}
                        />
                    </Box>
                </Grid>

                <Grid
                    size={{
                        xs: 12,
                        sm: 6,
                        lg: 3,
                    }}
                >
                    <Box
                        onClick={() => navigate("/validation")}
                        sx={{
                            cursor: "pointer",
                            height: "100%",
                        }}
                    >
                        <DashboardCard
                            title="Validated"
                            value={String(dashboard.validatedDataCollections)}
                            description="Countries fully validated"
                            icon={<TaskAltIcon />}
                        />
                    </Box>
                </Grid>
            </Grid>

            <Grid
                container
                spacing={3}
                sx={{
                    mt: 1,
                }}
            >
                <Grid
                    size={{
                        xs: 12,
                        lg: 8,
                    }}
                >
                    <Card>
                        <CardContent>
                            <Stack
                                direction="row"
                                sx={{
                                    justifyContent: "space-between",
                                    alignItems: "center",
                                    mb: 3,
                                }}
                            >
                                <Box>
                                    <Typography
                                        variant="h6"
                                        sx={{
                                            fontWeight: 600,
                                        }}
                                    >
                                        Data Collection Progress
                                    </Typography>

                                    <Typography
                                        variant="body2"
                                        color="text.secondary"
                                    >
                                        Progress by participating country
                                    </Typography>
                                </Box>

                                {activeCampaign && (
                                    <Chip
                                        label={activeCampaign.code}
                                        color="primary"
                                        variant="outlined"
                                    />
                                )}
                            </Stack>

                            <Stack spacing={3}>
                                <Box
                                    onClick={() =>
                                        navigate("/data-collections?status=DRAFT")
                                    }
                                    sx={{
                                        cursor: "pointer",
                                    }}
                                >
                                    <Stack
                                        direction="row"
                                        sx={{
                                            justifyContent: "space-between",
                                            mb: 1,
                                        }}
                                    >
                                        <Typography variant="body2">
                                            Draft
                                        </Typography>

                                        <Typography variant="body2">
                                            {`${
                                                dashboard.totalDataCollections === 0
                                                    ? 0
                                                    : Math.round(
                                                        (dashboard.draftDataCollections /
                                                            dashboard.totalDataCollections) *
                                                            100,
                                                    )
                                            }%`}
                                        </Typography>
                                    </Stack>

                                    <LinearProgress
                                        variant="determinate"
                                        value={
                                            dashboard.totalDataCollections === 0
                                                ? 0
                                                : (dashboard.draftDataCollections /
                                                    dashboard.totalDataCollections) *
                                                    100
                                        }
                                    />
                                </Box>

                                <Box
                                    onClick={() =>
                                        navigate("/data-collections?status=IN_PROGRESS")
                                    }
                                    sx={{
                                        cursor: "pointer",
                                    }}
                                >
                                    <Stack
                                        direction="row"
                                        sx={{
                                            justifyContent: "space-between",
                                            mb: 1,
                                        }}
                                    >
                                        <Typography variant="body2">
                                            In Progress
                                        </Typography>

                                        <Typography variant="body2">
                                            {`${
                                                dashboard.totalDataCollections === 0
                                                    ? 0
                                                    : Math.round(
                                                        (dashboard.inProgressDataCollections /
                                                            dashboard.totalDataCollections) *
                                                            100,
                                                    )
                                            }%`}
                                        </Typography>
                                    </Stack>

                                    <LinearProgress
                                        variant="determinate"
                                        value={
                                            dashboard.totalDataCollections === 0
                                                ? 0
                                                : (dashboard.inProgressDataCollections /
                                                    dashboard.totalDataCollections) *
                                                    100
                                        }
                                    />
                                </Box>

                                <Box
                                    onClick={() =>
                                        navigate("/submissions?status=SUBMITTED")
                                    }
                                    sx={{
                                        cursor: "pointer",
                                    }}
                                >
                                    <Stack
                                        direction="row"
                                        sx={{
                                            justifyContent: "space-between",
                                            mb: 1,
                                        }}
                                    >
                                        <Typography variant="body2">
                                            Under Validation
                                        </Typography>

                                        <Typography variant="body2">
                                            {`${
                                                dashboard.totalDataCollections === 0
                                                    ? 0
                                                    : Math.round(
                                                        (dashboard.submittedDataCollections /
                                                            dashboard.totalDataCollections) *
                                                            100,
                                                    )
                                            }%`}
                                        </Typography>
                                    </Stack>

                                    <LinearProgress
                                        variant="determinate"
                                        value={
                                            dashboard.totalDataCollections === 0
                                                ? 0
                                                : (dashboard.submittedDataCollections /
                                                    dashboard.totalDataCollections) *
                                                    100
                                        }
                                    />
                                </Box>

                                <Box
                                    onClick={() =>
                                        navigate("/data-collections?status=VALIDATED")
                                    }
                                    sx={{
                                        cursor: "pointer",
                                    }}
                                >
                                    <Stack
                                        direction="row"
                                        sx={{
                                            justifyContent: "space-between",
                                            mb: 1,
                                        }}
                                    >
                                        <Typography variant="body2">
                                            Completed
                                        </Typography>

                                        <Typography variant="body2">
                                            {`${
                                                dashboard.totalDataCollections === 0
                                                    ? 0
                                                    : Math.round(
                                                        (dashboard.validatedDataCollections /
                                                            dashboard.totalDataCollections) *
                                                            100,
                                                    )
                                            }%`}
                                        </Typography>
                                    </Stack>

                                    <LinearProgress
                                        variant="determinate"
                                        value={
                                            dashboard.totalDataCollections === 0
                                                ? 0
                                                : (dashboard.validatedDataCollections /
                                                    dashboard.totalDataCollections) *
                                                    100
                                        }
                                    />
                                </Box>

                                <Box
                                    onClick={() =>
                                        navigate("/data-collections?status=REJECTED")
                                    }
                                    sx={{
                                        cursor: "pointer",
                                    }}
                                >
                                    <Stack
                                        direction="row"
                                        sx={{
                                            justifyContent: "space-between",
                                            mb: 1,
                                        }}
                                    >
                                        <Typography variant="body2">
                                            Rejected
                                        </Typography>

                                        <Typography variant="body2">
                                            {`${
                                                dashboard.totalDataCollections === 0
                                                    ? 0
                                                    : Math.round(
                                                        (dashboard.rejectedDataCollections /
                                                            dashboard.totalDataCollections) *
                                                            100,
                                                    )
                                            }%`}
                                        </Typography>
                                    </Stack>

                                    <LinearProgress
                                        variant="determinate"
                                        value={
                                            dashboard.totalDataCollections === 0
                                                ? 0
                                                : (dashboard.rejectedDataCollections /
                                                    dashboard.totalDataCollections) *
                                                    100
                                        }
                                    />
                                <Box
                                    onClick={() =>
                                        navigate("/data-collections?status=CANCELLED")
                                    }
                                    sx={{
                                        cursor: "pointer",
                                    }}
                                >
                                    <Stack
                                        direction="row"
                                        sx={{
                                            justifyContent: "space-between",
                                            mb: 1,
                                        }}
                                    >
                                        <Typography variant="body2">
                                            Cancelled
                                        </Typography>

                                        <Typography variant="body2">
                                            {`${
                                                dashboard.totalDataCollections === 0
                                                    ? 0
                                                    : Math.round(
                                                        (dashboard.cancelledDataCollections /
                                                            dashboard.totalDataCollections) *
                                                            100,
                                                    )
                                            }%`}
                                        </Typography>
                                    </Stack>

                                    <LinearProgress
                                        variant="determinate"
                                        value={
                                            dashboard.totalDataCollections === 0
                                                ? 0
                                                : (dashboard.cancelledDataCollections /
                                                    dashboard.totalDataCollections) *
                                                    100
                                        }
                                    />
                                </Box>
                                </Box>
                            </Stack>
                        </CardContent>
                    </Card>
                </Grid>

                <Grid
                    size={{
                        xs: 12,
                        lg: 4,
                    }}
                >
                    <Card
                        onClick={() => {
                            if (activeCampaign) {
                                navigate(
                                    `/campaigns/${activeCampaign.id}`,
                                );
                            }
                        }}
                        sx={{
                            height: "100%",
                            cursor: activeCampaign
                                ? "pointer"
                                : "default",
                        }}
                    >
                        <CardContent>
                            <Typography
                                variant="h6"
                                sx={{
                                    fontWeight: 600,
                                }}
                            >
                                Active Campaign
                            </Typography>

                            <Typography
                                variant="body2"
                                color="text.secondary"
                                sx={{
                                    mt: 0.5,
                                }}
                            >
                                Current AIKP data collection cycle
                            </Typography>

                            <Divider
                                sx={{
                                    my: 3,
                                }}
                            />

                            <Typography
                                variant="subtitle1"
                                sx={{
                                    fontWeight: 600,
                                }}
                            >
                                {activeCampaign
                                    ? activeCampaign.name
                                    : "No active campaign"}
                            </Typography>

                            <Typography
                                variant="body2"
                                color="text.secondary"
                                sx={{
                                    mt: 1,
                                }}
                            >
                                {activeCampaign
                                    ? `Campaign code: ${activeCampaign.code}`
                                    : "There is currently no active data collection campaign."}
                            </Typography>

                            {activeCampaign && (
                                <Box
                                    sx={{
                                        mt: 3,
                                    }}
                                >
                                    <Chip
                                        label={
                                            activeCampaign.status
                                        }
                                        color="success"
                                        size="small"
                                    />
                                </Box>
                            )}
                        </CardContent>
                    </Card>
                </Grid>
            </Grid>
        <Card sx={{ mt: 3 }}>
            <CardContent>
                <Box sx={{ mb: 3 }}>
                    <Typography
                        variant="h6"
                        sx={{ fontWeight: 600 }}
                    >
                        Quick Access
                    </Typography>

                    <Typography
                        variant="body2"
                        color="text.secondary"
                        sx={{ mt: 0.5 }}
                    >
                        Access the main AIKP functional areas.
                    </Typography>
                </Box>

                <Grid
                    container
                    spacing={2}
                >
                    <Grid
                        size={{
                            xs: 12,
                            sm: 6,
                            md: 4,
                        }}
                    >
                        <Box
                            onClick={() => navigate("/campaigns")}
                            sx={{
                                cursor: "pointer",
                                height: "100%",
                            }}
                        >
                            <DashboardCard
                                title="Campaigns"
                                value={String(
                                    dashboard.totalCampaigns,
                                )}
                                description="Manage AIKP campaigns"
                                icon={<CampaignOutlinedIcon />}
                            />
                        </Box>
                    </Grid>

                    <Grid
                        size={{
                            xs: 12,
                            sm: 6,
                            md: 4,
                        }}
                    >
                        <Box
                            onClick={() =>
                                navigate("/questionnaires")
                            }
                            sx={{
                                cursor: "pointer",
                                height: "100%",
                            }}
                        >
                            <DashboardCard
                                title="Questionnaires"
                                value="→"
                                description="Manage questionnaires, groups and variables"
                                icon={<TaskAltIcon />}
                            />
                        </Box>
                    </Grid>

                    <Grid
                        size={{
                            xs: 12,
                            sm: 6,
                            md: 4,
                        }}
                    >
                        <Box
                            onClick={() =>
                                navigate("/data-collections")
                            }
                            sx={{
                                cursor: "pointer",
                                height: "100%",
                            }}
                        >
                            <DashboardCard
                                title="Data Collections"
                                value={String(
                                    dashboard.totalDataCollections,
                                )}
                                description="Manage data collection activities"
                                icon={<TrendingUpOutlinedIcon />}
                            />
                        </Box>
                    </Grid>

                    <Grid
                        size={{
                            xs: 12,
                            sm: 6,
                            md: 4,
                        }}
                    >
                        <Box
                            onClick={() => navigate("/submissions")}
                            sx={{
                                cursor: "pointer",
                                height: "100%",
                            }}
                        >
                            <DashboardCard
                                title="Submissions"
                                value={String(
                                    dashboard.submittedDataCollections,
                                )}
                                description="Review submitted collections"
                                icon={<TaskAltIcon />}
                            />
                        </Box>
                    </Grid>

                    <Grid
                        size={{
                            xs: 12,
                            sm: 6,
                            md: 4,
                        }}
                    >
                        <Box
                            onClick={() => navigate("/validation")}
                            sx={{
                                cursor: "pointer",
                                height: "100%",
                            }}
                        >
                            <DashboardCard
                                title="Validation"
                                value={String(
                                    dashboard.validatedDataCollections,
                                )}
                                description="Review and validate collections"
                                icon={<TaskAltIcon />}
                            />
                        </Box>
                    </Grid>

                    <Grid
                        size={{
                            xs: 12,
                            sm: 6,
                            md: 4,
                        }}
                    >
                        <Box
                            onClick={() => navigate("/countries")}
                            sx={{
                                cursor: "pointer",
                                height: "100%",
                            }}
                        >
                            <DashboardCard
                                title="Countries"
                                value={String(
                                    dashboard.totalCountries,
                                )}
                                description="View participating countries"
                                icon={<PublicOutlinedIcon />}
                            />
                        </Box>
                    </Grid>

                    <Grid
                        size={{
                            xs: 12,
                            sm: 6,
                            md: 4,
                        }}
                    >
                        <Box
                            onClick={() => navigate("/sectors")}
                            sx={{
                                cursor: "pointer",
                                height: "100%",
                            }}
                        >
                            <DashboardCard
                                title="Sectors"
                                value="→"
                                description="View infrastructure sectors"
                                icon={<TrendingUpOutlinedIcon />}
                            />
                        </Box>
                    </Grid>
                </Grid>
            </CardContent>
        </Card>

        </Box>
    );
}