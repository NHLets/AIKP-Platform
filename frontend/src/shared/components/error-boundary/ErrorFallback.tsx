import ReportProblemOutlinedIcon from "@mui/icons-material/ReportProblemOutlined";
import Box from "@mui/material/Box";
import Button from "@mui/material/Button";
import Paper from "@mui/material/Paper";
import Stack from "@mui/material/Stack";
import Typography from "@mui/material/Typography";

import type { ErrorFallbackProps } from "./ErrorBoundary.types";

export default function ErrorFallback({
    error,
    onReset,
}: ErrorFallbackProps) {
    return (
        <Box
            sx={{
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
                minHeight: "100vh",
                p: 3,
            }}
        >
            <Paper
                elevation={3}
                sx={{
                    p: 4,
                }}
            >
                <Stack
                    spacing={3}
                    sx={{
                        alignItems: "center",
                    }}
                >
                    <ReportProblemOutlinedIcon
                        color="error"
                        sx={{ fontSize: 64 }}
                    />

                    <Typography variant="h5">
                        Something went wrong
                    </Typography>

                    <Typography
                        variant="body1"
                        color="text.secondary"
                    >
                        An unexpected error occurred.
                    </Typography>

                    {import.meta.env.DEV && (
                        <Paper
                            variant="outlined"
                            sx={{
                                width: "100%",
                                p: 2,
                            }}
                        >
                            <Typography
                                variant="body2"
                                component="pre"
                                sx={{
                                    m: 0,
                                    whiteSpace: "pre-wrap",
                                }}
                            >
                                {error.message}
                            </Typography>
                </Paper>
        )}

                    <Button
                        variant="contained"
                        onClick={onReset}
                    >
                        Try Again
                    </Button>
                </Stack>
            </Paper>
        </Box>
    );
}