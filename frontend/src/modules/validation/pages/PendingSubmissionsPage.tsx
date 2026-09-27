import { useEffect, useState } from "react";

interface PendingSubmission {

  id:string;

  country:string;

  organization:string;

  completeness:number;

  priority:"High"|"Medium"|"Low";

}

export default function PendingSubmissionsPage(){

  const [submissions,setSubmissions] = useState<PendingSubmission[]>([]);

  useEffect(()=>{

    setSubmissions([

      {
        id:"1",
        country:"Cameroon",
        organization:"ENEO",
        completeness:91,
        priority:"High"
      },
      {
        id:"2",
        country:"Madagascar",
        organization:"JIRAMA",
        completeness:87,
        priority:"Medium"
      },
      {
        id:"3",
        country:"Burkina Faso",
        organization:"SONABEL",
        completeness:95,
        priority:"Low"
      }

    ]);

  },[]);

  return(

    <div style={{padding:24}}>

      <h1>Pending Submissions</h1>

      <table style={{width:"100%",borderCollapse:"collapse"}}>

        <thead>

          <tr>

            <th>Country</th>

            <th>Organization</th>

            <th>Completeness</th>

            <th>Priority</th>

          </tr>

        </thead>

        <tbody>

          {submissions.map(submission=>(

            <tr key={submission.id}>

              <td>{submission.country}</td>

              <td>{submission.organization}</td>

              <td>{submission.completeness}%</td>

              <td>{submission.priority}</td>

            </tr>

          ))}

        </tbody>

      </table>

    </div>

  );

}
