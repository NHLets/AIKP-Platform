import { useEffect, useState } from "react";

interface ReviewObservation{

  id:string;

  variable:string;

  value:string;

  status:"Pending"|"Validated"|"Rejected";

}

export default function ObservationReviewPage(){

  const [observations,setObservations] = useState<ReviewObservation[]>([]);

  const [selectedId,setSelectedId] = useState<string>("");

  const [comment,setComment] = useState("");

  const [decision,setDecision] =
    useState<"PENDING"|"APPROVED"|"REJECTED">("PENDING");

  const [severity,setSeverity] =
    useState<"LOW"|"MEDIUM"|"HIGH">("LOW");



  useEffect(()=>{

    setObservations([

      {
        id:"1",
        variable:"Installed Capacity",
        value:"520 MW",
        status:"Pending"
      },
      {
        id:"2",
        variable:"Energy Generated",
        value:"1842 GWh",
        status:"Validated"
      },
      {
        id:"3",
        variable:"SAIDI",
        value:"18.4",
        status:"Pending"
      }

    ]);

  },[]);



  function approveObservation(){
    setDecision("APPROVED");
  }

  function rejectObservation(){
    setDecision("REJECTED");
  }

  return(

    <div style={{padding:24}}>

      <h1>Observation Review Panel</h1>

      <table style={{width:"100%",borderCollapse:"collapse"}}>

        <thead>

          <tr>

            <th>Variable</th>

            <th>Value</th>

            <th>Status</th>

          </tr>

        </thead>

        <tbody>

          {observations.map(obs=>(

            <tr
              key={obs.id}
              onClick={()=>setSelectedId(obs.id)}
              style={{
                cursor:"pointer",
                background:selectedId===obs.id?"#EFF6FF":"white"
              }}
            >

              <td>{obs.variable}</td>

              <td>{obs.value}</td>

              <td>{obs.status}</td>

            </tr>

          ))}

        </tbody>

      </table>



      <div
        style={{
          marginTop:24,
          padding:16,
          border:"1px solid #D1D5DB",
          borderRadius:8
        }}
      >

        <h3>Comment & Severity</h3>

        <textarea
          rows={4}
          value={comment}
          onChange={(e)=>setComment(e.target.value)}
          placeholder="Enter technical review comment..."
          style={{
            width:"100%",
            marginTop:12,
            marginBottom:16
          }}
        />

        <div
          style={{
            display:"flex",
            gap:12,
            alignItems:"center"
          }}
        >

          <label>Severity</label>

          <select
            value={severity}
            onChange={(e)=>
              setSeverity(
                e.target.value as "LOW"|"MEDIUM"|"HIGH"
              )
            }
          >

            <option value="LOW">LOW</option>

            <option value="MEDIUM">MEDIUM</option>

            <option value="HIGH">HIGH</option>

          </select>

        </div>

        <div
          style={{
            marginTop:16,
            padding:12,
            background:"#F9FAFB",
            borderRadius:6
          }}
        >

          <strong>Preview</strong>

          <div>Severity : {severity}</div>

          <div>Comment : {comment || "-"}</div>

        </div>


        <div
          style={{
            display:"flex",
            gap:12,
            marginTop:20
          }}
        >

          <button
            onClick={approveObservation}
            style={{
              background:"#16A34A",
              color:"white",
              border:"none",
              padding:"10px 16px",
              borderRadius:6
            }}
          >
            Approve
          </button>

          <button
            onClick={rejectObservation}
            style={{
              background:"#DC2626",
              color:"white",
              border:"none",
              padding:"10px 16px",
              borderRadius:6
            }}
          >
            Reject
          </button>

        </div>

        <div
          style={{
            marginTop:16,
            padding:12,
            borderRadius:6,
            background:
              decision==="APPROVED"
                ? "#D1FAE5"
                : decision==="REJECTED"
                  ? "#FEE2E2"
                  : "#FEF3C7"
          }}
        >

          <strong>Decision</strong>

          <div>{decision}</div>

        </div>


      </div>

    </div>

  );

}
