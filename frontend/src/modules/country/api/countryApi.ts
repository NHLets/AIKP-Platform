import { axiosClient } from "../../../shared/api/axiosClient";

import type {
    CountrySummary,
    CountryResponse,
    CreateCountryRequest,
    UpdateCountryRequest,
} from "../types";

export const countryApi = {

    async getAll(): Promise<CountrySummary[]> {

        const response =
            await axiosClient.get<CountrySummary[]>("/v1/countries");

        return response.data;

    },

    async create(
        request: CreateCountryRequest,
    ): Promise<CountryResponse> {

        const response =
            await axiosClient.post<CountryResponse>(
                "/v1/countries",
                request,
            );

        return response.data;

    },

    async update(
        id: string,
        request: UpdateCountryRequest,
    ): Promise<CountryResponse> {

        const response =
            await axiosClient.put<CountryResponse>(
                `/v1/countries/${id}`,
                request,
            );

        return response.data;

    },

    async delete(id: string): Promise<void> {

        await axiosClient.delete(`/v1/countries/${id}`);

    },
    async activate(id: string): Promise<void> {

    await axiosClient.patch(
        `/v1/countries/${id}/activate`,
    );

    },

async deactivate(id: string): Promise<void> {

    await axiosClient.patch(
        `/v1/countries/${id}/deactivate`,
    );

    },
};