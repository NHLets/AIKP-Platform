import { useEffect } from "react";
import { Controller, useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";

import {
    Box,
    Button,
    Checkbox,
    FormControlLabel,
    Grid,
    TextField,
} from "@mui/material";

import type { CountryFormProps } from "./CountryForm.types";

import {
    countrySchema,
    type CountryFormData,
} from "./CountryForm.validation";

export default function CountryForm({
    initialValues,
    submitLabel,
    submitting = false,
    onSubmit,
    onCancel,
}: CountryFormProps) {

    const {
        control,
        handleSubmit,
        reset,
    } = useForm<CountryFormData>({

        resolver: zodResolver(countrySchema),

        defaultValues: {

            iso2Code: "",

            iso3Code: "",

            numericCode: "",

            name: "",

            officialName: "",

            active: true,

        },

    });

    useEffect(() => {

        reset({

            iso2Code: initialValues?.iso2Code ?? "",

            iso3Code: initialValues?.iso3Code ?? "",

            numericCode: initialValues?.numericCode ?? "",

            name: initialValues?.name ?? "",

            officialName: initialValues?.officialName ?? "",

            active: initialValues?.active ?? true,

        });

    }, [initialValues, reset]);

    return (

        <Box
            component="form"
            onSubmit={handleSubmit(onSubmit)}
            sx={{ mt: 2 }}
        >

            <Box
                sx={{
                    display: "flex",
                    flexDirection: "column",
                     gap: 3,
                }}
            >

                <Grid container spacing={2}>

                    <Grid size={{ xs: 12, sm: 6 }}>

                        <Controller
                            name="iso2Code"
                            control={control}
                            render={({ field, fieldState }) => (

                                <TextField
                                    {...field}
                                    label="ISO2 Code"
                                    fullWidth
                                    error={!!fieldState.error}
                                    helperText={fieldState.error?.message}
                                />

                            )}
                        />

                    </Grid>

                    <Grid size={{ xs: 12, sm: 6 }}>

                        <Controller
                            name="iso3Code"
                            control={control}
                            render={({ field, fieldState }) => (

                                <TextField
                                    {...field}
                                    label="ISO3 Code"
                                    fullWidth
                                    error={!!fieldState.error}
                                    helperText={fieldState.error?.message}
                                />

                            )}
                        />

                    </Grid>

                </Grid>

                <Controller
                    name="numericCode"
                    control={control}
                    render={({ field, fieldState }) => (

                        <TextField
                            {...field}
                            label="Numeric Code"
                            fullWidth
                            error={!!fieldState.error}
                            helperText={fieldState.error?.message}
                        />

                    )}
                />

                <Controller
                    name="name"
                    control={control}
                    render={({ field, fieldState }) => (

                        <TextField
                            {...field}
                            label="Country Name"
                            fullWidth
                            error={!!fieldState.error}
                            helperText={fieldState.error?.message}
                        />

                    )}
                />

                <Controller
                    name="officialName"
                    control={control}
                    render={({ field, fieldState }) => (

                        <TextField
                            {...field}
                            label="Official Name"
                            fullWidth
                            error={!!fieldState.error}
                            helperText={fieldState.error?.message}
                        />

                    )}
                />

                <Controller
                    name="active"
                    control={control}
                    render={({ field }) => (

                        <FormControlLabel
                            label="Active"
                            control={
                                <Checkbox
                                    checked={field.value}
                                    onChange={(_, checked) =>
                                        field.onChange(checked)
                                    }
                                />
                            }
                        />

                    )}
                />

                <Box
                    sx={{
                     display: "flex",
                    justifyContent: "flex-end",
                     gap: 2,
                    }}
                >

                    <Button
                        variant="outlined"
                        onClick={onCancel}
                        disabled={submitting}
                    >
                        Cancel
                    </Button>

                    <Button
                        type="submit"
                        variant="contained"
                        disabled={submitting}
                    >
                        {submitting
                            ? `${submitLabel}...`
                            : submitLabel}
                    </Button>

                </Box>

            </Box>

        </Box>

    );

}