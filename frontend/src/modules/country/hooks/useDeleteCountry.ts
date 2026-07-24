import { useMutation, useQueryClient } from "@tanstack/react-query";

import { countryApi } from "../api/countryApi";

export interface UseDeleteCountryOptions {

    onSuccess?: () => void;

    onError?: (error: Error) => void;

}

export function useDeleteCountry(
    options?: UseDeleteCountryOptions,
) {

    const queryClient = useQueryClient();

    return useMutation<void, Error, string>({

        mutationFn: (id) => countryApi.delete(id),

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