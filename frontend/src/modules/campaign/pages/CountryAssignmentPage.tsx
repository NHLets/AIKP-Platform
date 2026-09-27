import { useState } from "react";

interface Country{

  code:string;

  name:string;

  selected:boolean;

}

export default function CountryAssignmentPage(){

  const [countries,setCountries] = useState<Country[]>([

    {code:"CMR",name:"Cameroon",selected:true},

    {code:"MDG",name:"Madagascar",selected:true},

    {code:"BFA",name:"Burkina Faso",selected:false},

    {code:"MOZ",name:"Mozambique",selected:true},

    {code:"TCD",name:"Chad",selected:false}

  ]);

  function toggleCountry(code:string){

    setCountries(previous=>

      previous.map(country=>

        country.code===code

          ? {...country,selected:!country.selected}

          : country

      )

    );

  }

  const selectedCount =
    countries.filter(country=>country.selected).length;

  return(

    <div style={{padding:24}}>

      <h1>Country Assignment</h1>

      <p>{selectedCount} countries selected</p>

      {countries.map(country=>(

        <div
          key={country.code}
          style={{
            display:"flex",
            alignItems:"center",
            gap:12,
            marginBottom:12
          }}
        >

          <input
            type="checkbox"
            checked={country.selected}
            onChange={()=>toggleCountry(country.code)}
          />

          <span>{country.name}</span>

        </div>

      ))}

    </div>

  );

}
