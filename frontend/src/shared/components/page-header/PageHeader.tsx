import { Stack, Typography } from "@mui/material";
import type { PageHeaderProps } from "./PageHeader.types";

export default function PageHeader({
    title,
    subtitle,
}: PageHeaderProps) {

    return (

        <Stack spacing={1} sx={{ mb: 4 }}>

            <Typography variant="h4">
                {title}
            </Typography>

            {subtitle && (

                <Typography
                    variant="body1"
                    color="text.secondary"
                >
                    {subtitle}
                </Typography>

            )}

        </Stack>

    );

}