import { useMemo, useState } from "react";

interface MonitoringRow{

  country:string;

  organization:string;

  progress:number;

  status:
    |"NOT_STARTED"
    |"IN_PROGRESS"
    |"SUBMITTED"
    |"VALIDATED";

  deadline:string;

}

export default function CampaignMonitoringPage(){

  const [rows] = useState<MonitoringRow[]>([

    {
      country:"Cameroon",
      organization:"ENEO",
      progress:91,
      status:"SUBMITTED",
      deadline:"2026-10-30"
    },

    {
      country:"Madagascar",
      organization:"JIRAMA",
      progress:87,
      status:"IN_PROGRESS",
      deadline:"2026-10-30"
    },

    {
      country:"Burkina Faso",
      organization:"SONABEL",
      progress:100,
      status:"VALIDATED",
      deadline:"2026-10-30"
    },

    {
      country:"Chad",
      organization:"SNE",
      progress:12,
      status:"NOT_STARTED",
      deadline:"2026-10-30"
    }

  ]);

  const today = new Date("2026-09-27");

  const monitoring = useMemo(()=>{

    return rows.map(row=>{

      const deadline = new Date(row.deadline);

      const diff = Math.ceil(

        (deadline.getTime()-today.getTime())

        /(1000*60*60*24)

      );

      return{

        ...row,

        daysLeft:diff

      };

    });

  },[rows]);

  return(

    <div style={{padding:24}}>

      <h1>Campaign Monitoring</h1>

      <table
        style={{
          width:"100%",
          borderCollapse:"collapse"
        }}
      >

        <thead>

          <tr>

            <th>Country</th>

            <th>Organization</th>

            <th>Progress</th>

            <th>Status</th>

            <th>Days Left</th>

          </tr>

        </thead>

        <tbody>

          {monitoring.map(item=>(

            <tr
              key={item.country+item.organization}
            >

              <td>{item.country}</td>

              <td>{item.organization}</td>

              <td>{item.progress}%</td>

              <td>{item.status}</td>

              <td>{item.daysLeft}</td>

            </tr>

          ))}

        </tbody>

      </table>

    </div>

  );

}
