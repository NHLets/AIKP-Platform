import { axiosClient } from "@/shared/api/axiosClient";

export async function startDataCollection(id: string) {
    const response = await axiosClient.post(
        `/data-collections/${id}/start`,
    );
    return response.data;
}

export async function submitDataCollection(id: string) {
    const response = await axiosClient.post(
        `/data-collections/${id}/submit`,
    );
    return response.data;
}

export async function validateDataCollection(id: string) {
    const response = await axiosClient.post(
        `/data-collections/${id}/validate`,
    );
    return response.data;
}

export async function rejectDataCollection(id: string) {
    const response = await axiosClient.post(
        `/data-collections/${id}/reject`,
    );
    return response.data;
}
