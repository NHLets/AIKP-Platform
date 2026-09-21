import { axiosClient } from "@/shared/api/axiosClient";

import type {
    DashboardResponse,
} from "../types/dashboard.types";

export async function getDashboard(): Promise<DashboardResponse> {

    const response =
        await axiosClient.get<DashboardResponse>(
            "/dashboard",
        );

    return response.data;
}
