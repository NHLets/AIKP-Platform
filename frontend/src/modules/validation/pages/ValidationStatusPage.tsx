import TaskAltIcon from "@mui/icons-material/TaskAlt";
import { Box, Card, CardContent, Grid, Stack, Typography } from "@mui/material";

import { useDashboard } from "@/modules/dashboard/hooks/useDashboard";

interface StatusCardProps {
    title: string;
    value: string;
    description: string;
}

function StatusCard({
    title,
    value,
    description,
}: StatusCardProps) {
    return (
        <Card sx={{ height: "100%" }}>
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
                        <TaskAltIcon />
                    </Box>
                </Stack>

                <Typography
                    variant="body2"
                    color="text.secondary"
                    sx={{ mt: 2 }}
                >
                    {description}
                </Typography>
            </CardContent>
        </Card>
    );
}

export default function ValidationStatusPage() {
    const { data: dashboard, isLoading, error } = useDashboard();

    if (isLoading) {
        return <Box>Loading validation status...</Box>;
    }

    if (error || !dashboard) {
        return <Box>Unable to load validation status.</Box>;
    }

    return (
        <Box>
            <Box sx={{ mb: 4 }}>
                <Typography
                    variant="h4"
                    sx={{ fontWeight: 700 }}
                >
                    Validation Status
                </Typography>

                <Typography
                    variant="body1"
                    color="text.secondary"
                    sx={{ mt: 1 }}
                >
                    Country-level validation status across AIKP questionnaires.
                </Typography>
            </Box>

            <Grid container spacing={2}>
                <Grid size={{ xs: 12, sm: 6, lg: 3 }}>
                    <StatusCard
                        title="Fully Validated Countries"
                        value={String(
                            dashboard.fullyValidatedCountries,
                        )}
                        description="PW_A, PW_B and PW_C validated"
                    />
                </Grid>

                <Grid size={{ xs: 12, sm: 6, lg: 3 }}>
                    <StatusCard
                        title="F_G Not Validated"
                        value={String(
                            dashboard.countriesWithFgNotValidated,
                        )}
                        description="Countries where F_G is not validated"
                    />
                </Grid>

                <Grid size={{ xs: 12, sm: 6, lg: 3 }}>
                    <StatusCard
                        title="F_G Validated"
                        value={String(
                            dashboard.countriesWithFgValidated,
                        )}
                        description="Countries where F_G is validated"
                    />
                </Grid>

                <Grid size={{ xs: 12, sm: 6, lg: 3 }}>
                    <StatusCard
                        title="PW Not Fully Validated"
                        value={String(
                            dashboard
                                .countriesNotValidatedBecauseOnePwNotValidated,
                        )}
                        description="At least one of PW_A, PW_B or PW_C is not validated"
                    />
                </Grid>
            </Grid>
        </Box>
    );
}
