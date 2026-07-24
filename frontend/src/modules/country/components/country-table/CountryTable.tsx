import {
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableRow,
    Chip,
} from "@mui/material";

import {CountryRowActions} from "../country-row-actions";

import type { CountryTableProps } from "./CountryTable.types";

export default function CountryTable({
    countries,
    onEdit,
    onActivate,
    onDeactivate,
    onDelete,
}: CountryTableProps) {

    return (
        <Table size="small">

            <TableHead>

                <TableRow>

                    <TableCell>ISO2</TableCell>

                    <TableCell>ISO3</TableCell>

                    <TableCell>Numeric</TableCell>

                    <TableCell>Name</TableCell>

                    <TableCell>Official Name</TableCell>

                    <TableCell>Status</TableCell>

                    <TableCell align="right">Actions</TableCell>

                </TableRow>

            </TableHead>

            <TableBody>

                {countries.map(country => (

                    <TableRow key={country.id} hover>

                        <TableCell>{country.iso2Code}</TableCell>

                        <TableCell>{country.iso3Code}</TableCell>

                        <TableCell>{country.numericCode}</TableCell>

                        <TableCell>{country.name}</TableCell>

                        <TableCell>{country.officialName}</TableCell>

                        <TableCell>

                            <Chip
                                label={country.active ? "Active" : "Inactive"}
                                color={country.active ? "success" : "default"}
                                size="small"
                            />

                        </TableCell>

                        <TableCell align="right">

                            <CountryRowActions
                                active={country.active}
                                onEdit={() => onEdit(country)}
                                onActivate={() => onActivate(country)}
                                onDeactivate={() => onDeactivate(country)}
                                onDelete={() => onDelete(country)}
                            />

                        </TableCell>

                    </TableRow>

                ))}

            </TableBody>

        </Table>
    );

}