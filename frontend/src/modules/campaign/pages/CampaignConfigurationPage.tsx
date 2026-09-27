import { useState } from "react";

interface CampaignConfiguration{

  campaign:string;

  sector:"POWER";

  referenceYear:number;

  openingDate:string;

  closingDate:string;

  status:"DRAFT"|"OPEN"|"CLOSED";

}

export default function CampaignConfigurationPage(){

  const [config,setConfig] = useState<CampaignConfiguration>({

    campaign:"AIKP 2026",

    sector:"POWER",

    referenceYear:2025,

    openingDate:"2026-08-01",

    closingDate:"2026-10-30",

    status:"OPEN"

  });

  function update<K extends keyof CampaignConfiguration>(
    field:K,
    value:CampaignConfiguration[K]
  ){

    setConfig(previous=>({

      ...previous,

      [field]:value

    }));

  }

  return(

    <div style={{padding:24}}>

      <h1>Campaign Configuration</h1>

      <div
        style={{
          display:"grid",
          gap:16,
          maxWidth:520,
          marginTop:24
        }}
      >

        <div>

          <label>Campaign</label>

          <input
            value={config.campaign}
            onChange={(e)=>

              update("campaign",e.target.value)

            }
          />

        </div>

        <div>

          <label>Sector</label>

          <input value="POWER" disabled />

        </div>

        <div>

          <label>Reference Year</label>

          <input
            type="number"
            value={config.referenceYear}
            onChange={(e)=>

              update(
                "referenceYear",
                Number(e.target.value)
              )

            }
          />

        </div>

        <div>

          <label>Opening Date</label>

          <input
            type="date"
            value={config.openingDate}
            onChange={(e)=>

              update("openingDate",e.target.value)

            }
          />

        </div>

        <div>

          <label>Closing Date</label>

          <input
            type="date"
            value={config.closingDate}
            onChange={(e)=>

              update("closingDate",e.target.value)

            }
          />

        </div>

        <div>

          <label>Status</label>

          <select
            value={config.status}
            onChange={(e)=>

              update(
                "status",
                e.target.value as
                "DRAFT"|"OPEN"|"CLOSED"
              )

            }
          >

            <option value="DRAFT">DRAFT</option>

            <option value="OPEN">OPEN</option>

            <option value="CLOSED">CLOSED</option>

          </select>

        </div>

      </div>

    </div>

  );

}
