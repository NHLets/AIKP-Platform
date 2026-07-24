export interface ConfirmDialogProps {

    open: boolean;

    title: string;

    message: string;

    confirmLabel?: string;

    cancelLabel?: string;

    loading?: boolean;

    danger?: boolean;

    onCancel: () => void;

    onConfirm: () => void;

}