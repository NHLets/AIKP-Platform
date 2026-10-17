import {
    Card,
    CardContent,
    Chip,
    Grid,
    Typography,
} from "@mui/material";

import { PageHeader } from "@/shared/components/page-header";

import { SECTOR_OPTIONS } from "@/shared/context/SectorContext";

export default function SectorCoveragePage() {
    return (
        <>
            <PageHeader
                title="AIKP Sector Coverage"
                subtitle="Overview of the sectors covered by the AIKP Platform."
            />

            <Grid container spacing={2}>
                {SECTOR_OPTIONS.map((sector) => (
                    <Grid
                        key={sector.code}
                        size={{
                            xs: 12,
                            sm: 6,
                            md: 3,
                        }}
                    >
                        <Card
                            sx={{
                                height: "100%",
                                border:
                                    sector.status === "OPERATIONAL"
                                        ? 1
                                        : undefined,
                                borderColor:
                                    sector.status === "OPERATIONAL"
                                        ? "primary.main"
                                        : undefined,
                            }}
                        >
                            <CardContent>
                                <Typography
                                    variant="h6"
                                    sx={{
                                        fontWeight: 600,
                                        mb: 1.5,
                                    }}
                                >
                                    {sector.label}
                                </Typography>

                                <Chip
                                    label={
                                        sector.status === "OPERATIONAL"
                                            ? "Operational"
                                            : "In development"
                                    }
                                    color={
                                        sector.status === "OPERATIONAL"
                                            ? "success"
                                            : "default"
                                    }
                                    variant={
                                        sector.status === "OPERATIONAL"
                                            ? "filled"
                                            : "outlined"
                                    }
                                    size="small"
                                />
                            </CardContent>
                        </Card>
                    </Grid>
                ))}
            </Grid>
        </>
    );
}
