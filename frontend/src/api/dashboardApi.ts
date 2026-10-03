import axiosClient from "../shared/api/axiosClient";
import type { DashboardOverview } from "../types/dashboard";

export async function getDashboardOverview(
  campaignId: string,
  referenceYear: number
): Promise<DashboardOverview> {

  const { data } = await axiosClient.get(
    '/dashboard/analytics/overview',
    {
      params: {
        campaignId,
        referenceYear,
      },
    }
  );

  return data;
}
