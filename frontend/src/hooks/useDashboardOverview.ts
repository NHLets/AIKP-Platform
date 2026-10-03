import { useQuery } from '@tanstack/react-query';
import { getDashboardOverview } from '../api/dashboardApi';

export function useDashboardOverview(
  campaignId: string,
  referenceYear: number
) {
  return useQuery({
    queryKey: ['dashboard-overview', campaignId, referenceYear],
    queryFn: () => getDashboardOverview(campaignId, referenceYear),
    staleTime: 5 * 60 * 1000,
    gcTime: 10 * 60 * 1000,
    refetchOnWindowFocus: false,
  });
}
