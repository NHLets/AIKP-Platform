import { useQuery } from "@tanstack/react-query";

import { organizationApi } from "../api/organizationApi";
import type { OrganizationSummary } from "../types";

export function useOrganizations() {
    return useQuery<OrganizationSummary[]>({
        queryKey: ["organizations"],
        queryFn: organizationApi.getAll,
    });
}
