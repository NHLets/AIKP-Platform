import type {
  UserSummary,
  OrganizationSummary,
  PersonSummary
} from "../types/admin";

const API = "/api/v1";

async function get<T>(url: string): Promise<T> {
  const response = await fetch(url, {
    credentials: "include",
    headers: {
      "Content-Type": "application/json"
    }
  });

  if (!response.ok) {
    throw new Error(await response.text());
  }

  return response.json();
}

export const adminApi = {

  getUsers: () =>
    get<UserSummary[]>(`${API}/users`),

  getOrganizations: () =>
    get<OrganizationSummary[]>(`${API}/organizations`),

  getPersons: () =>
    get<PersonSummary[]>(`${API}/persons`),

  activateUser: (id: string) =>
    fetch(`${API}/users/${id}/activate`, { method: "POST" }),

  deactivateUser: (id: string) =>
    fetch(`${API}/users/${id}/deactivate`, { method: "POST" }),

  inviteUser: (userId: string) =>
    fetch(`${API}/invitations`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ userId })
    })
};
