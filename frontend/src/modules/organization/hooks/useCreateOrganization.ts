import { useMutation, useQueryClient } from "@tanstack/react-query";

import { organizationApi } from "../api/organizationApi";
import type { CreateOrganizationRequest } from "../types";

export function useCreateOrganization() {
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: (request: CreateOrganizationRequest) =>
            organizationApi.create(request),
        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: ["organizations"],
            });
        },
    });
}
