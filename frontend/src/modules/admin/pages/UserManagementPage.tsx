import { useEffect, useState } from "react";
import { adminApi } from "../services/adminApi";
import type { UserSummary } from "../types/admin";

export default function UserManagementPage() {
  const [users, setUsers] = useState<UserSummary[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    adminApi.getUsers()
      .then((data) => {
        setUsers(data);
      })
      .finally(() => {
        setLoading(false);
      });
  }, []);

  if (loading) {
    return <div>Loading users...</div>;
  }

  return (
    <div style={{ padding: 24 }}>
      <h2>User Management</h2>

      <table width="100%" cellPadding={8}>
        <thead>
          <tr>
            <th align="left">Name</th>
            <th align="left">Email</th>
            <th align="left">Role</th>
            <th align="left">Organization</th>
            <th align="left">Status</th>
          </tr>
        </thead>

        <tbody>
          {users.map((user) => (
            <tr key={user.id}>
              <td>{user.fullName}</td>
              <td>{user.email}</td>
              <td>{user.role}</td>
              <td>{user.organization}</td>
              <td>{user.status}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
