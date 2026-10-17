import {
  FormControl,
  InputLabel,
  MenuItem,
  Select,
} from "@mui/material";

import { useCampaign } from "../context/CampaignContext";
import { useCampaigns } from "../../hooks/useCampaigns";
import type { CampaignSummary } from "../../types/campaign";
import { useEffect } from "react";

export default function CampaignSelector() {
  const { campaignId, setCampaignId } = useCampaign();
  const { data } = useCampaigns();

  const campaigns: CampaignSummary[] = Array.isArray(data) ? data : [];

  useEffect(() => {
    if (campaigns.length === 0) {
      return;
    }

    const currentCampaignExists = campaigns.some(
      (campaign) => campaign.id === campaignId,
    );

    if (currentCampaignExists) {
      return;
    }

    const activeCampaign = campaigns.find(
      (campaign) => campaign.status === "ACTIVE",
    );

    if (activeCampaign) {
      setCampaignId(activeCampaign.id);
    }
  }, [campaigns, campaignId, setCampaignId]);

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
