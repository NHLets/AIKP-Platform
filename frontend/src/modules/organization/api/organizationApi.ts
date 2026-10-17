import { axiosClient } from "@/shared/api/axiosClient";

import type {
    CreateOrganizationRequest,
    OrganizationSummary,
} from "../types";

export const organizationApi = {
    async getAll(): Promise<OrganizationSummary[]> {
        const response = await axiosClient.get<OrganizationSummary[]>(
            "/v1/organizations",
        );

        return response.data;
    },

    async create(
        request: CreateOrganizationRequest,
    ): Promise<OrganizationSummary> {
        const response = await axiosClient.post<OrganizationSummary>(
            "/v1/organizations",
            request,
        );

        return response.data;
    },

    async delete(id: string): Promise<void> {
        await axiosClient.delete(`/v1/organizations/${id}`);
    },
};
