import { useMutation } from "@tanstack/react-query";
import authApi from "../api/authApi";
import { setToken } from "../shared/auth/tokenProvider";

export function useLogin() {

  return useMutation({
    mutationFn: authApi.login,

    onSuccess: (response) => {
      setToken(response.accessToken);
    },
  });

}
