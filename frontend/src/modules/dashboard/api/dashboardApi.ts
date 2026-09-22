import { axiosClient } from "@/shared/api/axiosClient";
import { API_ROOT_URL } from "@/shared/config/env";

import type {
    DashboardResponse,
} from "../types/dashboard.types";

export async function getDashboard(): Promise<DashboardResponse> {

    const response =
        await axiosClient.get<DashboardResponse>(
            "/dashboard",
            {
                baseURL: API_ROOT_URL,
            },
        );

    return response.data;
}
