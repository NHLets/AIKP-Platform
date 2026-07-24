import Alert from "@mui/material/Alert";
import MuiSnackbar from "@mui/material/Snackbar";

import type { SnackbarMessage } from "./Snackbar.types";

export interface SnackbarAlertProps {
    notification: SnackbarMessage | null;

    open: boolean;

    onClose: () => void;

    onExited?: () => void;
}

export function SnackbarAlert({
    notification,
    open,
    onClose,
    onExited,
}: SnackbarAlertProps) {
    if (!notification) {
        return null;
    }

    return (
        <MuiSnackbar
            open={open}
            autoHideDuration={notification.autoHideDuration}
            onClose={(_, reason) => {
                if (reason === "clickaway") {
                    return;
                }

                onClose();
            }}
            slotProps={{
                transition: {
                    onExited,
                },
            }}
            anchorOrigin={{
                vertical: "bottom",
                horizontal: "right",
            }}
        >
            <Alert
                onClose={onClose}
                severity={notification.severity}
                variant="filled"
                elevation={6}
            >
                {notification.message}
            </Alert>
        </MuiSnackbar>
    );
}