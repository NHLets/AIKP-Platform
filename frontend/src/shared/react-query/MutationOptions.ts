import type { QueryKey } from "@tanstack/react-query";

export interface MutationCallbacks<TData = unknown, TError = unknown> {

    onSuccess?: (data: TData) => void;

    onError?: (error: TError) => void;

    onSettled?: () => void;

}

export interface MutationOptions<
    TData = unknown,
    TError = unknown,
> extends MutationCallbacks<TData, TError> {

    invalidateQueries?: readonly QueryKey[];

}