import { useEffect, useState } from "react";
import { adminApi } from "../services/adminApi";
import type { UserSummary } from "../types/admin";

export default function InvitationPage() {
  const [users, setUsers] = useState<UserSummary[]>([]);
  const [selectedUser, setSelectedUser] = useState("");
  const [token, setToken] = useState("");

  useEffect(() => {
    adminApi.getUsers().then(setUsers);
  }, []);

  async function sendInvitation() {
    const response = await adminApi.inviteUser(selectedUser);
    const data = await response.json();
    setToken(data.token);
  }

  return (
    <div style={{ padding: 24 }}>
      <h2>Invitation Management</h2>

      <select
        value={selectedUser}
        onChange={(e) => setSelectedUser(e.target.value)}
      >
        <option value="">Select user</option>
        {users.map((user) => (
          <option key={user.id} value={user.id}>
            {user.fullName}
          </option>
        ))}
      </select>

      <div style={{ marginTop: 16 }}>
        <button onClick={sendInvitation}>
          Send Invitation
        </button>
      </div>

      {token && (
        <div style={{ marginTop: 16 }}>
          <strong>Invitation Token</strong>
          <div>{token}</div>
        </div>
      )}
    </div>
  );
}
