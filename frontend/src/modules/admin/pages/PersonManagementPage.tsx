import { useEffect, useState } from "react";
import { adminApi } from "../services/adminApi";
import type { PersonSummary } from "../types/admin";

export default function PersonManagementPage() {
  const [persons, setPersons] = useState<PersonSummary[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    adminApi.getPersons()
      .then((data) => {
        setPersons(data);
      })
      .finally(() => {
        setLoading(false);
      });
  }, []);

  if (loading) {
    return <div>Loading persons...</div>;
  }

  return (
    <div style={{ padding: 24 }}>
      <h2>Person Management</h2>

      <table width="100%" cellPadding={8}>
        <thead>
          <tr>
            <th align="left">Name</th>
            <th align="left">Position</th>
            <th align="left">Organization</th>
            <th align="left">Email</th>
            <th align="left">Status</th>
          </tr>
        </thead>
        <tbody>
          {persons.map((person) => (
            <tr key={person.id}>
              <td>{person.fullName}</td>
              <td>{person.position}</td>
              <td>{person.organization}</td>
              <td>{person.email}</td>
              <td>{person.status}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
