import { axiosClient } from "@/shared/api/axiosClient";
import type { QualityAssessment } from "../types/quality.types";

export async function getQualityAssessment(id: string): Promise<QualityAssessment> {
  const response = await axiosClient.get<QualityAssessment>(
    `/quality/data-collections/${id}/assessment`,
  );
  return response.data;
}

export async function runQualityAssessment(id: string): Promise<void> {
  await axiosClient.post(
    `/quality/data-collections/${id}/assessment`,
  );
}
