import { useMutation, useQueryClient } from "@tanstack/react-query";

import { countryApi } from "../api/countryApi";

import type {
    CountryResponse,
    UpdateCountryRequest,
} from "../types";

export interface UseUpdateCountryOptions {

    onSuccess?: (country: CountryResponse) => void;

    onError?: (error: Error) => void;

}

export interface UpdateCountryVariables {

    id: string;

    request: UpdateCountryRequest;

}

export function useUpdateCountry(
    options?: UseUpdateCountryOptions,
) {

    const queryClient = useQueryClient();

    return useMutation<
        CountryResponse,
        Error,
        UpdateCountryVariables
    >({

        mutationFn: ({ id, request }) =>
            countryApi.update(id, request),

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