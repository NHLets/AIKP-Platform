import { useEffect, useState } from "react";
import KpiCard from "../components/KpiCard";
import ValidationAnalyticsPanel from "../components/ValidationAnalyticsPanel";

interface DashboardStats {
  pending: number;
  validated: number;
  rejected: number;
  completeness: number;
}

export default function ValidationDashboardPage() {

  const [severityStats] = useState({
    low:12,
    medium:21,
    high:10
  });

  const [decisionStats] = useState({
    approved:84,
    rejected:11,
    pending:5
  });

  const [stats] = useState<DashboardStats>({
    pending: 18,
    validated: 5,
    rejected: 43,
    completeness: 91
  });

  useEffect(() => {
    // TODO M19.2 : load dashboard statistics from backend
  }, []);

  return (
    <div style={{ padding: 24 }}>

      <h1>Validation Dashboard</h1>

      <p>
        African Development Bank · AIKP Validation Workspace
      </p>

      <div
        style={{
          display: "grid",
          gridTemplateColumns: "repeat(2, 1fr)",
          gap: 16,
          marginTop: 24,
          marginBottom: 24
        }}
      >
        <KpiCard
          color="#1976D2"title="Pending Reviews"
          value={stats.pending}
        />

        <KpiCard
          color="#2E7D32"title="Validated"
          value={stats.validated}
        />

        <KpiCard
          color="#ED6C02"title="Rejected"
          value={stats.rejected}
        />

        <KpiCard
          color="#7B1FA2"title="Average Completeness"
          value={`${stats.completeness}%`}
        />
      </div>

      <ValidationAnalyticsPanel dataCollectionId="dashboard" />


      <div
        style={{
          marginTop:32,
          padding:20,
          border:"1px solid #D1D5DB",
          borderRadius:8
        }}
      >

        <h2>Validation Analytics</h2>

        <div
          style={{
            display:"grid",
            gridTemplateColumns:"repeat(2,1fr)",
            gap:16,
            marginTop:20
          }}
        >

          <div
            style={{
              background:"#F9FAFB",
              padding:16,
              borderRadius:6
            }}
          >

            <strong>Severity Distribution</strong>

            <div style={{marginTop:12}}>LOW : {severityStats.low}</div>

            <div>MEDIUM : {severityStats.medium}</div>

            <div>HIGH : {severityStats.high}</div>

          </div>

          <div
            style={{
              background:"#F9FAFB",
              padding:16,
              borderRadius:6
            }}
          >

            <strong>Decision Summary</strong>

            <div style={{marginTop:12}}>
              Approved : {decisionStats.approved}%
            </div>

            <div>Rejected : {decisionStats.rejected}%</div>

            <div>Pending : {decisionStats.pending}%</div>

          </div>

        </div>

      </div>



    </div>
  );
}
