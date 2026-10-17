import { axiosClient } from "@/shared/api/axiosClient";

const api = axiosClient;

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

export async function createDataCollectionObservation(
  request: CreateObservationRequest,
) {
  const { data } = await api.post("/data-collection-observations", {
    dataCollectionId: request.dataCollectionId,
    questionnaireVariableId: request.questionnaireVariableId,
    referenceYear: request.referenceYear,
    numericValue: request.numericValue,
    textValue: request.textValue,
    booleanValue: request.booleanValue,
    dateValue: request.dateValue,
    selectedUnit: request.selectedUnit,
    comment: request.comment,
  });

  return data;
}

export async function getDataCollectionObservations(
  dataCollectionId: string,
) {
  const { data } = await api.get(
    `/data-collection-observations/collection/${dataCollectionId}`,
  );

  return data;
}

export async function updateDataCollectionObservation(
  id: string,
  request: CreateObservationRequest,
) {
  const { data } = await api.put(
    `/data-collection-observations/${id}`,
    {
      numericValue: request.numericValue,
      textValue: request.textValue,
      booleanValue: request.booleanValue,
      dateValue: request.dateValue,
      selectedUnit: request.selectedUnit,
      comment: request.comment,
    },
  );

  return data;
}

export async function deleteDataCollectionObservation(id: string) {
  const { data } = await api.delete(
    `/data-collection-observations/${id}`,
  );

  return data;
}

export async function createNotApplicableObservation(
  request: CreateObservationRequest,
) {
  const { data } = await api.post(
    "/data-collection-observations/not-applicable",
    {
      dataCollectionId: request.dataCollectionId,
      questionnaireVariableId: request.questionnaireVariableId,
      referenceYear: request.referenceYear,
      numericValue: null,
      textValue: null,
      booleanValue: null,
      dateValue: null,
      selectedUnit: request.selectedUnit,
      comment: request.comment,
    },
  );

  return data;
}

export async function createNotAvailableObservation(
  request: CreateObservationRequest,
) {
  const { data } = await api.post(
    "/data-collection-observations/not-available",
    {
      dataCollectionId: request.dataCollectionId,
      questionnaireVariableId: request.questionnaireVariableId,
      referenceYear: request.referenceYear,
      numericValue: null,
      textValue: null,
      booleanValue: null,
      dateValue: null,
      selectedUnit: request.selectedUnit,
      comment: request.comment,
    },
  );

  return data;
}
