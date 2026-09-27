import { useMemo, useState } from "react";

interface OrganizationAssignment{

  country:string;

  organization:string;

  type:"NATIONAL"|"UTILITY";

}

export default function QuestionnaireAssignmentPage(){

  const [organizations] = useState<OrganizationAssignment[]>([

    {
      country:"Cameroon",
      organization:"Ministry of Energy",
      type:"NATIONAL"
    },

    {
      country:"Cameroon",
      organization:"ENEO",
      type:"UTILITY"
    },

    {
      country:"Madagascar",
      organization:"JIRAMA",
      type:"UTILITY"
    },

    {
      country:"Burkina Faso",
      organization:"SONABEL",
      type:"UTILITY"
    }

  ]);

  const assignments = useMemo(()=>{

    return organizations.map(org=>({

      ...org,

      questionnaires:

        org.type==="NATIONAL"

          ? ["PW_A","PW_B"]

          : ["PW_C","F_G"]

    }));

  },[organizations]);

  return(

    <div style={{padding:24}}>

      <h1>Automatic Questionnaire Assignment</h1>

      <p>
        Questionnaires are assigned automatically according
        to the organization type.
      </p>

      <table
        style={{
          width:"100%",
          borderCollapse:"collapse"
        }}
      >

        <thead>

          <tr>

            <th align="left">Country</th>

            <th align="left">Organization</th>

            <th align="left">Type</th>

            <th align="left">Questionnaires</th>

          </tr>

        </thead>

        <tbody>

          {assignments.map(item=>(

            <tr key={item.country+item.organization}>

              <td>{item.country}</td>

              <td>{item.organization}</td>

              <td>{item.type}</td>

              <td>{item.questionnaires.join(", ")}</td>

            </tr>

          ))}

        </tbody>

      </table>

    </div>

  );

}
