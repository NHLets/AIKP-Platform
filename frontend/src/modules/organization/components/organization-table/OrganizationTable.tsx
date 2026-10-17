import {
    Chip,
    IconButton,
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableRow,
    Tooltip,
} from "@mui/material";
import DeleteIcon from "@mui/icons-material/Delete";

import type { OrganizationSummary } from "../../types";

interface OrganizationTableProps {
    organizations: OrganizationSummary[];
    countries: Array<{
        id: string;
        name: string;
        iso2Code?: string;
        iso3Code?: string;
    }>;
    onDelete: (organization: OrganizationSummary) => void;
}

function formatOrganizationType(type: string): string {
    return type
        .toLowerCase()
        .split("_")
        .map((word) => word.charAt(0).toUpperCase() + word.slice(1))
        .join(" ");
}

export default function OrganizationTable({
    organizations,
    countries,
    onDelete,
}: OrganizationTableProps) {
    const countryById = new Map(
        countries.map((country) => [country.id, country]),
    );

    return (
        <Table size="small">
            <TableHead>
                <TableRow>
                    <TableCell>Code</TableCell>
                    <TableCell>Organization</TableCell>
                    <TableCell>Type</TableCell>
                    <TableCell>Country</TableCell>
                    <TableCell>Status</TableCell>
                    <TableCell align="right">Actions</TableCell>
                </TableRow>
            </TableHead>

            <TableBody>
                {organizations.map((organization) => {
                    const country = countryById.get(organization.countryId);

                    return (
                        <TableRow key={organization.id} hover>
                            <TableCell>{organization.code}</TableCell>
                            <TableCell>{organization.name}</TableCell>
                            <TableCell>
                                {formatOrganizationType(organization.type)}
                            </TableCell>
                            <TableCell>
                                {country?.name ?? organization.countryId}
                            </TableCell>
                            <TableCell>
                                <Chip
                                    label={
                                        organization.active
                                            ? "Active"
                                            : "Inactive"
                                    }
                                    color={
                                        organization.active
                                            ? "success"
                                            : "default"
                                    }
                                    size="small"
                                />
                            </TableCell>
                            <TableCell align="right">
                                <Tooltip title="Delete organization">
                                    <IconButton
                                        color="error"
                                        size="small"
                                        aria-label={`Delete ${organization.name}`}
                                        onClick={() => onDelete(organization)}
                                    >
                                        <DeleteIcon />
                                    </IconButton>
                                </Tooltip>
                            </TableCell>
                        </TableRow>
                    );
                })}
            </TableBody>
        </Table>
    );
}
