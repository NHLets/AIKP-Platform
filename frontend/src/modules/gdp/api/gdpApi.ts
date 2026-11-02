import { axiosClient } from "../../../shared/api/axiosClient";

export interface GdpRow {
  iso3Code: string;
  countryName: string;
  year: number;
  gdpCurrentUsd: number;
  sourceName: string;
  sourceRevision: string;
  sourceSeriesCode: string;
  sourceUrl: string;
}

export interface GdpFilters {
  iso3?: string;
  year?: number;
}

export const gdpApi = {
  async getAll(params?: GdpFilters): Promise<GdpRow[]> {
    const response = await axiosClient.get<GdpRow[]>("/v1/gdp", { params });
    return response.data;
  },
};
