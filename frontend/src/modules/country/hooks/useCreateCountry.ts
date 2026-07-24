import { useMutation, useQueryClient } from "@tanstack/react-query";

import { countryApi } from "../api/countryApi";

import type {
    CountryResponse,
    CreateCountryRequest,
} from "../types";

export interface UseCreateCountryOptions {

    onSuccess?: (country: CountryResponse) => void;

    onError?: (error: Error) => void;

}

export function useCreateCountry(
    options?: UseCreateCountryOptions,
) {

    const queryClient = useQueryClient();

    return useMutation<
        CountryResponse,
        Error,
        CreateCountryRequest
    >({

        mutationFn: countryApi.create,

        onSuccess: async (country) => {

            await queryClient.invalidateQueries({
                queryKey: ["countries"],
            });

            options?.onSuccess?.(country);

        },

        onError: (error) => {

            console.error(error);

            options?.onError?.(error);

        },

    });

}