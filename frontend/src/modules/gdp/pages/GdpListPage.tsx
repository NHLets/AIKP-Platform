import { useMemo, useState } from "react";
import { useQuery } from "@tanstack/react-query";
import {
  Alert,
  Box,
  CircularProgress,
  FormControl,
  InputLabel,
  MenuItem,
  Paper,
  Select,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Typography,
} from "@mui/material";
import { gdpApi } from "../api/gdpApi";
import { useCountries } from "@/modules/country/hooks/useCountries";

const YEARS = Array.from({ length: 11 }, (_, index) => 2015 + index);

const numberFormatter = new Intl.NumberFormat("en-US", {
  maximumFractionDigits: 0,
});

export default function GdpListPage() {
  const [year, setYear] = useState(2024);
  const [iso3, setIso3] = useState("ALL");
  const countriesQuery = useCountries();

  const query = useQuery({
    queryKey: ["gdp-reference", year, iso3],
    queryFn: () =>
      gdpApi.getAll({
        year,
        ...(iso3 !== "ALL" ? { iso3 } : {}),
      }),
  });

  const countries = useMemo(
    () =>
      (countriesQuery.data ?? [])
        .filter((country) => country.active)
        .map((country) => [country.iso3Code, country.name] as [string, string])
        .sort((a, b) => a[1].localeCompare(b[1])),
    [countriesQuery.data],
  );

  return (
    <Box sx={{ p: 3, minWidth: 0 }}>
      <Typography variant="h4" gutterBottom>
        GDP reference data
      </Typography>

      <Typography
        color="text.secondary"
        sx={{
          mb: 2,
          maxWidth: 1100,
          whiteSpace: "normal",
          lineHeight: 1.6,
        }}
      >
        Annual GDP at current market prices, expressed in current US dollars.
        Data are sourced primarily from the World Bank's World Development
        Indicators (NY.GDP.MKTP.CD), with IMF World Economic Outlook data used
        where applicable. These figures represent nominal GDP, not GDP at
        constant prices or purchasing power parity (PPP).
      </Typography>

      <Box sx={{ display: "flex", gap: 2, mb: 2, flexWrap: "wrap" }}>
        <FormControl size="small" sx={{ minWidth: 140 }}>
          <InputLabel id="gdp-year">Year</InputLabel>
          <Select
            labelId="gdp-year"
            label="Year"
            value={year}
            onChange={(event) => setYear(Number(event.target.value))}
          >
            {YEARS.map((value) => (
              <MenuItem key={value} value={value}>
                {value}
              </MenuItem>
            ))}
          </Select>
        </FormControl>

        <FormControl size="small" sx={{ minWidth: 220 }}>
          <InputLabel id="gdp-country">Country</InputLabel>
          <Select
            labelId="gdp-country"
            label="Country"
            value={iso3}
            onChange={(event) => setIso3(String(event.target.value))}
          >
            <MenuItem value="ALL">All countries</MenuItem>
            {countries.map(([code, name]) => (
              <MenuItem key={code} value={code}>
                {name}
              </MenuItem>
            ))}
          </Select>
        </FormControl>
      </Box>

      {query.isPending && <CircularProgress />}

      {query.isError && (
        <Alert severity="error">
          Unable to load GDP reference data.
        </Alert>
      )}

      {query.isSuccess && query.data.length === 0 && (
        <Alert severity="info">
          No GDP observation is available for this filter. Missing values are
          not imputed.
        </Alert>
      )}

      {query.isSuccess && query.data.length > 0 && (
        <TableContainer
          component={Paper}
          sx={{
            width: "100%",
            overflowX: "auto",
          }}
        >
          <Table size="small" sx={{ minWidth: 1050 }}>
            <TableHead>
              <TableRow>
                <TableCell>Country</TableCell>
                <TableCell>ISO3</TableCell>
                <TableCell align="right">Year</TableCell>
                <TableCell align="right">GDP (current USD)</TableCell>
                <TableCell sx={{ minWidth: 320 }}>Source</TableCell>
              </TableRow>
            </TableHead>

            <TableBody>
              {query.data.map((row) => (
                <TableRow key={`${row.iso3Code}-${row.year}`}>
                  <TableCell>{row.countryName}</TableCell>
                  <TableCell>{row.iso3Code}</TableCell>
                  <TableCell align="right">{row.year}</TableCell>
                  <TableCell align="right" sx={{ whiteSpace: "nowrap" }}>
                    {numberFormatter.format(Number(row.gdpCurrentUsd))}
                  </TableCell>
                  <TableCell
                    sx={{
                      maxWidth: 420,
                      overflowWrap: "anywhere",
                    }}
                  >
                    <a
                      href={row.sourceUrl}
                      target="_blank"
                      rel="noopener noreferrer"
                    >
                      {row.sourceName} ({row.sourceRevision})
                    </a>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      )}
    </Box>
  );
}
