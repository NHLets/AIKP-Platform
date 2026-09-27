import {
  FormControl,
  InputLabel,
  MenuItem,
  Select
} from "@mui/material";

import { useCampaign } from "../context/CampaignContext";

const YEARS = [2024, 2025, 2026, 2027, 2028];

export default function ReferenceYearSelector() {

  const {
    referenceYear,
    setReferenceYear
  } = useCampaign();

  return (
    <FormControl size="small" sx={{ minWidth: 160 }}>
      <InputLabel>Year</InputLabel>

      <Select
        value={referenceYear}
        label="Year"
        onChange={(e) => setReferenceYear(Number(e.target.value))}
      >
        {YEARS.map((year) => (
          <MenuItem key={year} value={year}>
            {year}
          </MenuItem>
        ))}
      </Select>
    </FormControl>
  );
}
