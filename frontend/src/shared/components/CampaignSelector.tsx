import {
  FormControl,
  InputLabel,
  MenuItem,
  Select,
} from "@mui/material";

import { useCampaign } from "../context/CampaignContext";
import { useCampaigns } from "../../hooks/useCampaigns";

export default function CampaignSelector() {
  const { campaignId, setCampaignId } = useCampaign();
  const { data } = useCampaigns();

  const campaigns = Array.isArray(data) ? data : [];

  return (
    <FormControl size="small" sx={{ minWidth: 240 }}>
      <InputLabel>Campaign</InputLabel>

      <Select
        value={
          campaigns.some((campaign) => campaign.id === campaignId)
            ? campaignId
            : ""
        }
        displayEmpty
        label="Campaign"
        onChange={(event) => setCampaignId(event.target.value)}
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
