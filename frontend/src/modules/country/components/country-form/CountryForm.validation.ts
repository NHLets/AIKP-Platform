import { z } from "zod";

export const countrySchema = z.object({

    iso2Code: z
        .string()
        .trim()
        .length(2, "ISO2 code must contain exactly 2 characters"),

    iso3Code: z
        .string()
        .trim()
        .length(3, "ISO3 code must contain exactly 3 characters"),

    numericCode: z
        .string()
        .regex(/^\d{3}$/, "Numeric code must contain exactly 3 digits"),

    name: z
        .string()
        .trim()
        .min(2, "Country name is required"),

    officialName: z
        .string()
        .trim()
        .min(2, "Official name is required"),

    active: z.boolean()

});

export type CountryFormData = z.infer<typeof countrySchema>;