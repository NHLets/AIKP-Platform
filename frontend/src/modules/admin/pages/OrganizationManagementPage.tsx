import { useEffect, useState } from "react";
import { adminApi } from "../services/adminApi";
import type { OrganizationSummary } from "../types/admin";

export default function OrganizationManagementPage() {
  const [organizations, setOrganizations] = useState<OrganizationSummary[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    adminApi.getOrganizations()
      .then((data) => {
        setOrganizations(data);
      })
      .finally(() => {
        setLoading(false);
      });
  }, []);

  if (loading) {
    return <div>Loading organizations...</div>;
  }

  return (
    <div style={{ padding: 24 }}>
      <h2>Organization Management</h2>

      <table width="100%" cellPadding={8}>
        <thead>
          <tr>
            <th align="left">Organization</th>
            <th align="left">Type</th>
            <th align="left">Country</th>
            <th align="left">Status</th>
          </tr>
        </thead>
        <tbody>
          {organizations.map((org) => (
            <tr key={org.id}>
              <td>{org.name}</td>
              <td>{org.type}</td>
              <td>{org.country}</td>
              <td>{org.status}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
