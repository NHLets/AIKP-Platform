import { useQuery } from "@tanstack/react-query";

import { countryApi } from "../api/countryApi";
import type { CountrySummary } from "../types";

export function useCountries() {

    return useQuery<CountrySummary[]>({
        queryKey: ["countries"],
        queryFn: countryApi.getAll,
    });

}