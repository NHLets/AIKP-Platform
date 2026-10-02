import axios from "axios";
import TokenProvider from "../auth/tokenProvider";

export const axiosClient = axios.create({
  baseURL: "/api",
  timeout: 10000,
  headers: {
    "Content-Type": "application/json",
  },
});

axiosClient.interceptors.request.use((config) => {
  const token = TokenProvider.getToken();

  if (token) {
    config.headers = config.headers ?? {};
    config.headers.Authorization = `Bearer ${token}`;
  }

  return config;
});



axiosClient.interceptors.response.use(
  (response) => response,

  (error) => {

    if (error.response?.status === 401) {
      TokenProvider.removeToken();
      localStorage.removeItem("AIKP_AUTH_USER");

      if (window.location.pathname !== "/login") {
        window.location.replace("/login");
      }
    }

    return Promise.reject(error);
  }
);

export default axiosClient;
