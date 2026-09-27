export type ValidationSeverity = "INFO" | "WARNING" | "ERROR";

export interface ValidationComment {
  id: string;
  observationId: string;
  validatorId: string;
  comment: string;
  severity: ValidationSeverity;
  createdAt: string;
  updatedAt: string;
}

export interface CreateValidationCommentRequest {
  observationId: string;
  validatorId: string;
  comment: string;
  severity: ValidationSeverity;
}
