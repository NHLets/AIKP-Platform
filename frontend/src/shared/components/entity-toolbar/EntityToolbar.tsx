import { Box, Button, TextField } from "@mui/material";
import AddIcon from "@mui/icons-material/Add";

import type { EntityToolbarProps } from "./EntityToolbar.types";

export default function EntityToolbar({
    searchPlaceholder,
    createLabel,
    onSearch,
    onCreate,
    createIcon,
}: EntityToolbarProps) {

    return (

        <Box
            sx={{
                display: "flex",
                justifyContent: "space-between",
                alignItems: "center",
                gap: 2,
                mb: 3,
            }}
        >

            <TextField
                placeholder={searchPlaceholder}
                size="small"
                sx={{ width: 340 }}
                onChange={(event) => onSearch?.(event.target.value)}
            />

            <Button
                variant="contained"
                startIcon={createIcon ?? <AddIcon />}
                onClick={onCreate}
            >
                {createLabel}
            </Button>

        </Box>

    );

}