import Box from "@mui/material/Box";
import Divider from "@mui/material/Divider";
import Typography from "@mui/material/Typography";

import type { AppFooterProps } from "./Layout.types";

export default function AppFooter({
    version = "0.1.0",
}: AppFooterProps) {
    return (
        <>
            <Divider />

            <Box
                component="footer"
                sx={{
                    py: 2,
                    px: 3,
                    textAlign: "center",
                }}
            >
                <Typography
                    variant="body2"
                    color="text.secondary"
                >
                    AIKP Portal © {new Date().getFullYear()} · Version {version}
                </Typography>
            </Box>
        </>
    );
}