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
    Grid,
    LinearProgress,
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
                    <DashboardCard
                        title="Active Campaign"
                        value="1"
                        description="Current data collection campaign"
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
                        value="23"
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
                        value="68%"
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
                        value="12"
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
                                            52%
                                        </Typography>
                                    </Stack>

                                    <LinearProgress
                                        variant="determinate"
                                        value={52}
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
                                            16%
                                        </Typography>
                                    </Stack>

                                    <LinearProgress
                                        variant="determinate"
                                        value={16}
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
                                            32%
                                        </Typography>
                                    </Stack>

                                    <LinearProgress
                                        variant="determinate"
                                        value={32}
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
        </Box>
    );
}