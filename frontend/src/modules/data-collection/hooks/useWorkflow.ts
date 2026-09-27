import { useMutation, useQueryClient } from "@tanstack/react-query";

import {
    startDataCollection,
    submitDataCollection,
    validateDataCollection,
    rejectDataCollection,
} from "../api/workflowApi";

export function useStartCollection() {
    const qc = useQueryClient();

    return useMutation({
        mutationFn: startDataCollection,
        onSuccess: (_, id) => {
            qc.invalidateQueries({
                queryKey: ["data-collection", id],
            });
        },
    });
}

export function useSubmitCollection() {
    const qc = useQueryClient();

    return useMutation({
        mutationFn: submitDataCollection,
        onSuccess: (_, id) => {
            qc.invalidateQueries({
                queryKey: ["data-collection", id],
            });
        },
    });
}

export function useValidateCollection() {
    const qc = useQueryClient();

    return useMutation({
        mutationFn: validateDataCollection,
        onSuccess: (_, id) => {
            qc.invalidateQueries({
                queryKey: ["data-collection", id],
            });
        },
    });
}

export function useRejectCollection() {
    const qc = useQueryClient();

    return useMutation({
        mutationFn: rejectDataCollection,
        onSuccess: (_, id) => {
            qc.invalidateQueries({
                queryKey: ["data-collection", id],
            });
        },
    });
}
