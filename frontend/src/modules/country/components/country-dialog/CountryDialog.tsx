import {
    Dialog,
    DialogContent,
    DialogTitle,
} from "@mui/material";

import {CountryForm} from "../country-form";

import { useCreateCountry } from "../../hooks/useCreateCountry";
import { useUpdateCountry } from "../../hooks/useUpdateCountry";

import type { CountryDialogProps } from "./CountryDialog.types";

export default function CountryDialog({
    open,
    onClose,
    mode,
    country,
}: CountryDialogProps) {

    const createCountry = useCreateCountry({
        onSuccess: () => {
            onClose();
        },
    });

    const updateCountry = useUpdateCountry({
        onSuccess: () => {
            onClose();
        },
    });

    const isCreate = mode === "create";

    const submitting = isCreate
        ? createCountry.isPending
        : updateCountry.isPending;

    const handleSubmit = (values: any) => {

        if (isCreate) {

            createCountry.mutate(values);

            return;
        }

        updateCountry.mutate({
            id: country!.id,
            request: values,
        });
    };

    return (

        <Dialog
            open={open}
            onClose={submitting ? undefined : onClose}
            fullWidth
            maxWidth="sm"
        >

            <DialogTitle>
                {isCreate ? "New Country" : "Edit Country"}
            </DialogTitle>

            <DialogContent>

                <CountryForm
                    initialValues={country}
                    submitLabel={
                        isCreate
                            ? "Create"
                            : "Update"
                    }
                    submitting={submitting}
                    onCancel={onClose}
                    onSubmit={handleSubmit}
                />

            </DialogContent>

        </Dialog>

    );

}