import { useDashboard } from "@/modules/dashboard/hooks/useDashboard";

import {
    SECTOR_OPTIONS,
    useSector,
} from "@/shared/context/SectorContext";

import { useNavigate } from "react-router-dom";
import CampaignOutlinedIcon from "@mui/icons-material/CampaignOutlined";
import TaskAltIcon from "@mui/icons-material/TaskAlt";
import PublicOutlinedIcon from "@mui/icons-material/PublicOutlined";
import TrendingUpOutlinedIcon from "@mui/icons-material/TrendingUpOutlined";

import {
    Box,
    Card,
    CardContent,
    Chip,
    Divider,
    FormControl,
    Grid,
    InputLabel,
    LinearProgress,
    MenuItem,
    Select,
    Stack,
    Typography,
} from "@mui/material";

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
    const { currentSector, currentSectorOption, setCurrentSector } = useSector();
    const { data: dashboard, isLoading, error } = useDashboard();

    if (isLoading) {
        return <Box>Loading dashboard...</Box>;
    }

    if (error || !dashboard) {
        return (
            <Box>
                Unable to load dashboard.
            </Box>
        );
    }

    const totalCollections = dashboard.totalDataCollections;

    const completedProgress =
        totalCollections > 0
            ? (dashboard.validatedDataCollections / totalCollections) * 100
            : 0;

    const underValidationProgress =
        totalCollections > 0
            ? (dashboard.submittedDataCollections / totalCollections) * 100
            : 0;

    const inProgressProgress =
        totalCollections > 0
            ? (dashboard.inProgressDataCollections / totalCollections) * 100
            : 0;

    const draftProgress =
        totalCollections > 0
            ? (dashboard.draftDataCollections / totalCollections) * 100
            : 0;

    const collectionProgress = Math.round(
        completedProgress +
        underValidationProgress +
        inProgressProgress,
    );


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

            <Box
                sx={{
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "space-between",
                    gap: 2,
                    mb: 2,
                    flexWrap: "wrap",
                }}
            >
                <Typography
                    variant="h6"
                    sx={{
                        mt: 1,
                        fontWeight: 600,
                    }}
                >
                    {currentSectorOption.label} — Current Overview
                </Typography>

            <Box
                sx={{
                    display: "flex",
                    justifyContent: "flex-end",
                    mb: 2,
                }}
            >
                <FormControl size="small" sx={{ minWidth: 220 }}>
                    <InputLabel id="dashboard-sector-label">
                        Sector
                    </InputLabel>

                    <Select
                        labelId="dashboard-sector-label"
                        value={currentSector}
                        label="Sector"
                        onChange={(event) => {
                            const selectedSector = event.target.value;

                            const option = SECTOR_OPTIONS.find(
                                (sector) => sector.code === selectedSector,
                            );

                            if (option?.status === "OPERATIONAL") {
                                setCurrentSector(option.code);
                            }
                        }}
                        renderValue={(selected) => {
                            const option = SECTOR_OPTIONS.find(
                                (sector) => sector.code === selected,
                            );

                            return `Sector: ${option?.label ?? "Power"}`;
                        }}
                    >
                        {SECTOR_OPTIONS.map((sector) => (
                            <MenuItem
                                key={sector.code}
                                value={sector.code}
                                disabled={
                                    sector.status !== "OPERATIONAL"
                                }
                            >
                                {sector.label}
                                {sector.status === "IN_DEVELOPMENT"
                                    ? " — In development"
                                    : ""}
                            </MenuItem>
                        ))}
                    </Select>
                </FormControl>
            </Box>

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
                    <DashboardCard
                        title="Active Campaign"
                        value={String(dashboard.activeCampaigns)}
                        description="Current data collection campaign."
                        icon={<CampaignOutlinedIcon />}
                    />
                </Grid>

                <Grid
                    size={{
                        xs: 12,
                        sm: 6,
                        lg: 3,
                    }}
                >
                    <DashboardCard
                        title="Countries"
                        value={String(dashboard.totalCountries)}
                        description="Countries participating in AIKP"
                        icon={<PublicOutlinedIcon />}
                    />
                </Grid>

                <Grid
                    size={{
                        xs: 12,
                        sm: 6,
                        lg: 3,
                    }}
                >
                    <DashboardCard
                        title="Collection Progress"
                        value={`${collectionProgress.toFixed(0)}%`}
                        description="Overall data collection progress"
                        icon={<TrendingUpOutlinedIcon />}
                    />
                </Grid>

                <Grid
                    size={{
                        xs: 12,
                        sm: 6,
                        lg: 3,
                    }}
                >
                    <DashboardCard
                        title="Validated"
                        value={String(dashboard.fullyValidatedCountries)}
                        description="Countries fully validated"
                        icon={<TaskAltIcon />}
                    />
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

                                <Chip
                                    label="AIKP 2026"
                                    color="primary"
                                    variant="outlined"
                                />
                            </Stack>

                            <Stack spacing={3}>
                                <Box>
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
                                            {Math.round(completedProgress)}%
                                        </Typography>
                                    </Stack>

                                    <LinearProgress
                                        variant="determinate"
                                        value={completedProgress}
                                    />
                                </Box>

                                <Box>
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
                                            {Math.round(underValidationProgress)}%
                                        </Typography>
                                    </Stack>

                                    <LinearProgress
                                        variant="determinate"
                                        value={underValidationProgress}
                                    />
                                </Box>

                                <Box>
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
                                            {Math.round(inProgressProgress)}%
                                        </Typography>
                                    </Stack>

                                    <LinearProgress
                                        variant="determinate"
                                        value={inProgressProgress}
                                    />
                                </Box>
                                <Box>
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
                                            {Math.round(draftProgress)}%
                                        </Typography>
                                    </Stack>

                                    <LinearProgress
                                        variant="determinate"
                                        value={draftProgress}
                                    />
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
                        sx={{
                            height: "100%",
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
                                AIKP Data Collection 2026
                            </Typography>

                            <Typography
                                variant="body2"
                                color="text.secondary"
                                sx={{
                                    mt: 1,
                                }}
                            >
                                Infrastructure data collection across
                                participating countries.
                            </Typography>

                            <Box
                                sx={{
                                    mt: 3,
                                }}
                            >
                                <Chip
                                    label="ACTIVE"
                                    color="success"
                                    size="small"
                                />
                            </Box>
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

                </Grid>
            </CardContent>
        </Card>

        </Box>
    );
}