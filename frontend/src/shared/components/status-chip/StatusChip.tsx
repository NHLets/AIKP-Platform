import Chip from "@mui/material/Chip";
import type { StatusChipProps } from "./StatusChip.types";

export default function StatusChip({
    active,
}: StatusChipProps) {

    return (
        <Chip
            size="small"
            color={active ? "success" : "default"}
            label={active ? "Active" : "Inactive"}
            variant={active ? "filled" : "outlined"}
        />
    );

}