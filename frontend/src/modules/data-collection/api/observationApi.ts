import axios from "axios";

const api = axios.create({
  baseURL: "/api/v1/observations",
});

/* ============================================================
   Legacy DTO (M16–M18 compatible)
   ============================================================ */

export interface CreateObservationRequest {
  dataCollectionId: string;
  questionnaireVariableId: string;
  referenceYear: number;

  numericValue: string | null;
  textValue: string | null;
  booleanValue: boolean | null;
  dateValue: string | null;

  selectedUnit: string | null;
  comment: string | null;
}

function extractValue(r: CreateObservationRequest): string {
  if (r.numericValue !== null) return r.numericValue;
  if (r.textValue !== null) return r.textValue;
  if (r.booleanValue !== null) return String(r.booleanValue);
  if (r.dateValue !== null) return r.dateValue;
  return "";
}

export async function createDataCollectionObservation(
  request: CreateObservationRequest,
) {
  const { data } = await api.post("", {
    dataCollectionId: request.dataCollectionId,
    variableId: request.questionnaireVariableId,
    organizationId: "00000000-0000-0000-0000-000000000001",
    value: extractValue(request),
  });

  return data;
}

export async function getDataCollectionObservations(
  dataCollectionId: string,
) {
  const { data } = await api.get(`/collection/${dataCollectionId}`);
  return data;
}

export async function updateDataCollectionObservation(
  id: string,
  request: CreateObservationRequest,
) {
  const { data } = await api.put(`/${id}`, {
    value: extractValue(request),
  });

  return data;
}

export async function deleteDataCollectionObservation(id: string) {
  const { data } = await api.delete(`/${id}`);
  return data;
}

export async function createNotApplicableObservation(
  request: CreateObservationRequest,
) {
  return createDataCollectionObservation({
    ...request,
    textValue: "NOT_APPLICABLE",
    numericValue: null,
    booleanValue: null,
    dateValue: null,
  });
}

export async function createNotAvailableObservation(
  request: CreateObservationRequest,
) {
  return createDataCollectionObservation({
    ...request,
    textValue: "NOT_AVAILABLE",
    numericValue: null,
    booleanValue: null,
    dateValue: null,
  });
}
