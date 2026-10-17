import { getToken } from "../../../shared/auth/tokenProvider";

const API = "/api/v1/settings";

export interface PlatformSettings {
  platformName: string;
  platformDescription: string;
  updatedAt: string;
}

function authHeaders(): HeadersInit {
  const token = getToken();
  return {
    "Content-Type": "application/json",
    ...(token ? { Authorization: `Bearer ${token}` } : {}),
  };
}

async function parseResponse(response: Response): Promise<PlatformSettings> {
  if (!response.ok) {
    const message = await response.text();
    throw new Error(message || `Request failed with status ${response.status}`);
  }
  return response.json() as Promise<PlatformSettings>;
}

export const settingsApi = {
  async get(): Promise<PlatformSettings> {
    const response = await fetch(API, {
      credentials: "include",
      headers: authHeaders(),
    });
    return parseResponse(response);
  },

  async update(
    payload: Pick<PlatformSettings, "platformName" | "platformDescription">,
  ): Promise<PlatformSettings> {
    const response = await fetch(API, {
      method: "PUT",
      credentials: "include",
      headers: authHeaders(),
      body: JSON.stringify(payload),
    });
    return parseResponse(response);
  },
};
