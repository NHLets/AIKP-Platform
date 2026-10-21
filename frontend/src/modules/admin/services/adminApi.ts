import type {
  UserSummary,
  OrganizationSummary,
  PersonSummary
} from "../types/admin";
import { getToken } from "../../../shared/auth/tokenProvider";

const API = "/api/v1";

function authHeaders(): HeadersInit {
  const token = getToken();

  return {
    "Content-Type": "application/json",
    ...(token ? { Authorization: `Bearer ${token}` } : {})
  };
}

async function get<T>(url: string): Promise<T> {
  const response = await fetch(url, {
    credentials: "include",
    headers: authHeaders()
  });

  if (!response.ok) {
    const message = await response.text();
    throw new Error(message || `Request failed with status ${response.status}`);
  }

  return response.json();
}

async function postAction(url: string): Promise<void> {
  const response = await fetch(url, {
    method: "POST",
    credentials: "include",
    headers: authHeaders()
  });

  if (!response.ok) {
    const message = await response.text();
    throw new Error(message || `Request failed with status ${response.status}`);
  }
}

export interface ActiveRole {
  id: string;
  name: string;
  status: string;
  system: boolean;
}

export interface CreateUserPayload {
  username: string;
  email: string;
  fullName: string;
  password: string;
  roleId: string;
}

async function postJson<T>(url: string, body: unknown): Promise<T> {
  const response = await fetch(url, {
    method: "POST",
    credentials: "include",
    headers: authHeaders(),
    body: JSON.stringify(body)
  });

  if (!response.ok) {
    const message = await response.text();
    throw new Error(message || `Request failed with status ${response.status}`);
  }

  return response.json() as Promise<T>;
}

export const adminApi = {
  getUsers: () =>
    get<UserSummary[]>(`${API}/users`),

  getActiveRoles: () =>
    get<ActiveRole[]>(`${API}/roles/active`),

  createUser: (payload: CreateUserPayload) =>
    postJson<UserSummary>(`${API}/users`, payload),

  getOrganizations: () =>
    get<OrganizationSummary[]>(`${API}/organizations`),

  getPersons: () =>
    get<PersonSummary[]>(`${API}/persons`),

  activateUser: (id: string) =>
    postAction(`${API}/users/${id}/activate`),

  deactivateUser: (id: string) =>
    postAction(`${API}/users/${id}/deactivate`),

  deleteUser: async (id: string): Promise<void> => {
    const response = await fetch(`${API}/users/${id}`, {
      method: "DELETE",
      credentials: "include",
      headers: authHeaders(),
    });

    if (!response.ok) {
      const body = await response.text();
      let message = body;

      try {
        const problem = JSON.parse(body) as { detail?: string; message?: string };
        message = problem.detail || problem.message || body;
      } catch {
        // Keep the original response text when it is not JSON.
      }

      throw new Error(
        message || `Request failed with status ${response.status}`,
      );
    }
  },

  inviteUser: (userId: string) =>
    fetch(`${API}/invitations`, {
      method: "POST",
      credentials: "include",
      headers: authHeaders(),
      body: JSON.stringify({ userId })
    })
};
