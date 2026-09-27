import {
  FormControl,
  InputLabel,
  MenuItem,
  Select
} from "@mui/material";

import { useCampaign } from "../context/CampaignContext";
import { useCampaigns } from "../../hooks/useCampaigns";


export default function CampaignSelector() {

  const {
    campaignId,
    setCampaignId
  } = useCampaign();

  const { data: campaigns = [] } = useCampaigns();

  return (
    <FormControl size="small" sx={{ minWidth: 240 }}>
      <InputLabel>Campaign</InputLabel>

      <Select
        value={campaignId}
        label="Campaign"
        onChange={(e) => setCampaignId(Number(e.target.value))}
      >
        {campaigns.map((campaign) => (
          <MenuItem key={campaign.id} value={campaign.id}>
            {campaign.name}
          </MenuItem>
        ))}
      </Select>
    </FormControl>
  );
}
