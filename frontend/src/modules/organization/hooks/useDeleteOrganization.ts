import { useMutation, useQueryClient } from "@tanstack/react-query";

import { organizationApi } from "../api/organizationApi";

export function useDeleteOrganization() {
    const queryClient = useQueryClient();

    return useMutation<void, Error, string>({
        mutationFn: (id) => organizationApi.delete(id),

        onSuccess: async () => {
            await queryClient.invalidateQueries({
                queryKey: ["organizations"],
            });
        },
    });
}
