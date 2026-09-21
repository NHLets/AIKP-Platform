import {
    Card,
    CardContent,
    Grid,
    Typography,
} from "@mui/material";

import { PageHeader } from "@/shared/components/page-header";

const sectors = [
    "Power",
    "Transport",
    "ICT",
    "Water and Sanitation",
];

export default function SectorListPage() {
    return (
        <>
            <PageHeader
                title="Sectors"
                subtitle="Reference sectors used across the platform."
            />

            <Grid container spacing={2}>
                {sectors.map((sector) => (
                    <Grid
                        key={sector}
                        size={{
                            xs: 12,
                            sm: 6,
                            md: 4,
                        }}
                    >
                        <Card>
                            <CardContent>
                                <Typography
                                    variant="subtitle1"
                                    sx={{
                                        fontWeight: 600,
                                    }}
                                >
                                    {sector}
                                </Typography>
                            </CardContent>
                        </Card>
                    </Grid>
                ))}
            </Grid>
        </>
    );
}
