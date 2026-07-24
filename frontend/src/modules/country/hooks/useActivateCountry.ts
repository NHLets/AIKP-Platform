import { useMutation, useQueryClient } from "@tanstack/react-query";

import { countryApi } from "../api/countryApi";

export interface UseActivateCountryOptions {

    onSuccess?: () => void;

    onError?: (error: Error) => void;

}

export function useActivateCountry(
    options?: UseActivateCountryOptions,
) {

    const queryClient = useQueryClient();

    return useMutation<void, Error, string>({

        mutationFn: (id) => countryApi.activate(id),

        onSuccess: async () => {

            await queryClient.invalidateQueries({
                queryKey: ["countries"],
            });

            options?.onSuccess?.();

        },

        onError: (error) => {

            console.error(error);

            options?.onError?.(error);

        },

    });

}