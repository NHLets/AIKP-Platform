import axiosClient from "../shared/api/axiosClient";
import { LoginRequest, LoginResponse } from "../types/auth";

export const authApi = {

  login: async (
    request: LoginRequest
  ): Promise<LoginResponse> => {

    const response = await axiosClient.post(
      "/auth/login",
      request
    );

    return response.data as LoginResponse;
  }

};

export default authApi;
