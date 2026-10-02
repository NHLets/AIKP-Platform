import axios from 'axios';
import type { DashboardOverview } from "../types/dashboard";

const api = axios.create({
  baseURL: '/api',
});

export async function getDashboardOverview(
  campaignId: number,
  referenceYear: number
): Promise<DashboardOverview> {

  const { data } = await api.get(
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
