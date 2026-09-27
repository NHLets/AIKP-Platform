import { useState } from "react";

interface OrganizationAssignment{

  country:string;

  organization:string;

}

export default function OrganizationAssignmentPage(){

  const [assignments,setAssignments] =
    useState<OrganizationAssignment[]>([

      {country:"Cameroon",organization:"ENEO"},

      {country:"Madagascar",organization:"JIRAMA"},

      {country:"Burkina Faso",organization:"SONABEL"},

      {country:"Mozambique",organization:"EDM"}

    ]);

  const organizations = [
    "ENEO",
    "JIRAMA",
    "SONABEL",
    "EDM",
    "SNE"
  ];

  function updateOrganization(
    country:string,
    organization:string
  ){

    setAssignments(previous=>

      previous.map(item=>

        item.country===country
          ? {...item,organization}
          : item

      )

    );

  }

  return(

    <div style={{padding:24}}>

      <h1>Organization Assignment</h1>

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

          </tr>

        </thead>

        <tbody>

          {assignments.map(item=>(

            <tr key={item.country}>

              <td>{item.country}</td>

              <td>

                <select
                  value={item.organization}
                  onChange={(e)=>

                    updateOrganization(
                      item.country,
                      e.target.value
                    )

                  }
                >

                  {organizations.map(org=>(

                    <option key={org} value={org}>
                      {org}
                    </option>

                  ))}

                </select>

              </td>

            </tr>

          ))}

        </tbody>

      </table>

    </div>

  );

}
