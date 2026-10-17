import {
  FormControl,
  InputLabel,
  MenuItem,
  Select
} from "@mui/material";

import { useCampaign } from "../context/CampaignContext";

const MIN_YEAR = 2015;
const MAX_YEAR = 2025;
const YEARS = Array.from(
  { length: MAX_YEAR - MIN_YEAR + 1 },
  (_, index) => MIN_YEAR + index,
);

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
