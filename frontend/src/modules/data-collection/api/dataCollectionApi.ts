import { axiosClient } from "@/shared/api/axiosClient";

import type {
    DataCollection,
    DataCollectionSummary,
} from "../types/dataCollection.types";

const DATA_COLLECTIONS_ENDPOINT =
    "http://localhost:8080/api/data-collections";


export interface CreateDataCollectionRequest {
    campaignId: string;
    countryId: string;
    questionnaireId: string;
    responsibleOrganizationId: string;
    operatorOrganizationId: string;
    dataCollectorId: string;
}

export async function createDataCollection(
    request: CreateDataCollectionRequest,
): Promise<DataCollection> {
    const response = await axiosClient.post<DataCollection>(
        DATA_COLLECTIONS_ENDPOINT,
        request,
    );

    return response.data;
}

export async function getDataCollections(): Promise<
    DataCollectionSummary[]
> {
    const response = await axiosClient.get<
        DataCollectionSummary[]
    >(DATA_COLLECTIONS_ENDPOINT);

    return response.data;
}

export async function getDataCollectionById(
    id: string,
): Promise<DataCollection> {
    const response = await axiosClient.get<DataCollection>(
        `${DATA_COLLECTIONS_ENDPOINT}/${id}`,
    );

    return response.data;
}

export async function startDataCollection(
    id: string,
): Promise<void> {
    await axiosClient.post(
        `${DATA_COLLECTIONS_ENDPOINT}/${id}/start`,
    );
}

export async function submitDataCollection(
    id: string,
): Promise<void> {
    await axiosClient.post(
        `${DATA_COLLECTIONS_ENDPOINT}/${id}/submit`,
    );
}

export async function validateDataCollection(
    id: string,
): Promise<void> {
    await axiosClient.post(
        `${DATA_COLLECTIONS_ENDPOINT}/${id}/validate`,
    );
}

export async function rejectDataCollection(
    id: string,
): Promise<void> {
    await axiosClient.post(
        `${DATA_COLLECTIONS_ENDPOINT}/${id}/reject`,
    );
}

export async function cancelDataCollection(
    id: string,
): Promise<void> {
    await axiosClient.post(
        `${DATA_COLLECTIONS_ENDPOINT}/${id}/cancel`,
    );
}


/* ============================================================
   Legacy compatibility (M16–M18)
   ============================================================ */

export const approveDataCollection = validateDataCollection;
