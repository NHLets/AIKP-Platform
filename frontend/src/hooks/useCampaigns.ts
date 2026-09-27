import { useQuery } from "@tanstack/react-query";
import campaignApi from "../api/campaignApi";

export function useCampaigns() {

  return useQuery({
    queryKey: ["campaigns"],
    queryFn: campaignApi.getAll,
    staleTime: 5 * 60 * 1000,
    gcTime: 10 * 60 * 1000,
    refetchOnWindowFocus: false,
  });

}
