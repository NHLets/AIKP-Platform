import {
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    DialogContentText,
    DialogTitle,
} from "@mui/material";

import type { ConfirmDialogProps } from "./ConfirmDialog.types";

export default function ConfirmDialog({
    open,
    title,
    message,
    confirmLabel = "Confirm",
    cancelLabel = "Cancel",
    loading = false,
    danger = false,
    onCancel,
    onConfirm,
}: ConfirmDialogProps) {

    return (
        <Dialog
            open={open}
            onClose={loading ? undefined : onCancel}
            maxWidth="xs"
            fullWidth
        >
            <DialogTitle>
                {title}
            </DialogTitle>

            <DialogContent>
                <DialogContentText>
                    {message}
                </DialogContentText>
            </DialogContent>

            <DialogActions>

                <Button
                    onClick={onCancel}
                    disabled={loading}
                >
                    {cancelLabel}
                </Button>

                <Button
                    onClick={onConfirm}
                    disabled={loading}
                    variant="contained"
                    color={danger ? "error" : "primary"}
                >
                    {confirmLabel}
                </Button>

            </DialogActions>

        </Dialog>
    );

}