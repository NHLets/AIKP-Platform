import { useEffect, useState } from "react";

interface CampaignDashboard{

  campaign:string;

  referenceYear:number;

  countries:number;

  questionnaires:number;

  deadline:string;

}

export default function CampaignDashboardPage(){

  const [dashboard] = useState<CampaignDashboard>({
    campaign:"AIKP 2026",
    referenceYear:2025,
    countries:23,
    questionnaires:4,
    deadline:"30 Oct 2026"
  });

  useEffect(()=>{},[]);

  return(

    <div style={{padding:24}}>

      <h1>Campaign Dashboard</h1>

      <h2>{dashboard.campaign}</h2>

      <div
        style={{
          display:"grid",
          gridTemplateColumns:"repeat(2,1fr)",
          gap:16,
          marginTop:24
        }}
      >

        <div>
          <strong>Countries</strong>
          <div>{dashboard.countries}</div>
        </div>

        <div>
          <strong>Questionnaires</strong>
          <div>{dashboard.questionnaires}</div>
        </div>

        <div>
          <strong>Reference Year</strong>
          <div>{dashboard.referenceYear}</div>
        </div>

        <div>
          <strong>Deadline</strong>
          <div>{dashboard.deadline}</div>
        </div>

      </div>

    </div>

  );

}
