import {
    useMutation,
    useQueryClient,
    type MutationFunction,
    type QueryKey,
} from "@tanstack/react-query";

import type { MutationOptions } from "./MutationOptions";

export interface ApiMutationConfig<
    TVariables,
    TData = unknown,
    TError = unknown,
> extends MutationOptions<TData, TError> {

    mutationFn: MutationFunction<TData, TVariables>;

}

export function useApiMutation<
    TVariables,
    TData = unknown,
    TError = unknown,
>(
    config: ApiMutationConfig<
        TVariables,
        TData,
        TError
    >,
) {

    const queryClient = useQueryClient();

    return useMutation({

        mutationFn: config.mutationFn,

        onSuccess: async (data) => {

            if (config.invalidateQueries) {

                await Promise.all(

                    config.invalidateQueries.map(
                        (queryKey: QueryKey) =>
                            queryClient.invalidateQueries({
                                queryKey,
                            }),
                    ),

                );

            }

            config.onSuccess?.(data);

        },

        onError: error => {

            config.onError?.(error as TError);

        },

        onSettled: () => {

            config.onSettled?.();

        },

    });

}