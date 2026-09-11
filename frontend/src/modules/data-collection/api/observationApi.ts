import { axiosClient } from "@/shared/api/axiosClient";

import type {
    CreateDataCollectionObservationRequest,
    DataCollectionObservation,
    UpdateDataCollectionObservationRequest,
} from "../types/observation.types";

const OBSERVATIONS_ENDPOINT =
    "http://localhost:8080/api/data-collection-observations";

export async function createDataCollectionObservation(
    request: CreateDataCollectionObservationRequest,
): Promise<DataCollectionObservation> {
    const response =
        await axiosClient.post<DataCollectionObservation>(
            OBSERVATIONS_ENDPOINT,
            request,
        );

    return response.data;
}

export async function createNotAvailableObservation(
    request: CreateDataCollectionObservationRequest,
): Promise<DataCollectionObservation> {
    const response =
        await axiosClient.post<DataCollectionObservation>(
            `${OBSERVATIONS_ENDPOINT}/not-available`,
            request,
        );

    return response.data;
}

export async function createNotApplicableObservation(
    request: CreateDataCollectionObservationRequest,
): Promise<DataCollectionObservation> {
    const response =
        await axiosClient.post<DataCollectionObservation>(
            `${OBSERVATIONS_ENDPOINT}/not-applicable`,
            request,
        );

    return response.data;
}

export async function updateDataCollectionObservation(
    id: string,
    request: UpdateDataCollectionObservationRequest,
): Promise<DataCollectionObservation> {
    const response =
        await axiosClient.put<DataCollectionObservation>(
            `${OBSERVATIONS_ENDPOINT}/${id}`,
            request,
        );

    return response.data;
}

export async function deleteDataCollectionObservation(
    id: string,
): Promise<void> {
    await axiosClient.delete(
        `${OBSERVATIONS_ENDPOINT}/${id}`,
    );
}

export async function getDataCollectionObservations(
    dataCollectionId: string,
): Promise<DataCollectionObservation[]> {
    const response =
        await axiosClient.get<DataCollectionObservation[]>(
            `${OBSERVATIONS_ENDPOINT}/collection/${dataCollectionId}`,
        );

    return response.data;
}

export async function getDataCollectionObservation(
    id: string,
): Promise<DataCollectionObservation> {
    const response =
        await axiosClient.get<DataCollectionObservation>(
            `${OBSERVATIONS_ENDPOINT}/${id}`,
        );

    return response.data;
}

export async function getDataCollectionObservationByLookup(
    dataCollectionId: string,
    questionnaireVariableId: string,
    referenceYear: number,
): Promise<DataCollectionObservation | null> {
    try {
        const response =
            await axiosClient.get<DataCollectionObservation>(
                `${OBSERVATIONS_ENDPOINT}/lookup`,
                {
                    params: {
                        dataCollectionId,
                        questionnaireVariableId,
                        referenceYear,
                    },
                },
            );

        return response.data;
    } catch (error: unknown) {
        if (
            typeof error === "object" &&
            error !== null &&
            "response" in error &&
            typeof error.response === "object" &&
            error.response !== null &&
            "status" in error.response &&
            error.response.status === 404
        ) {
            return null;
        }

        throw error;
    }
}
