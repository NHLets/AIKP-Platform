import { useState } from "react";

import {
    IconButton,
    Menu,
    MenuItem,
} from "@mui/material";

import MoreVertIcon from "@mui/icons-material/MoreVert";

import type { CountryRowActionsProps } from "./CountryRowActions.types";

export default function CountryRowActions({
    active,
    onEdit,
    onActivate,
    onDeactivate,
    onDelete,
}: CountryRowActionsProps) {

    const [anchorEl, setAnchorEl] =
        useState<HTMLElement | null>(null);

    const open = Boolean(anchorEl);

    const closeMenu = () => setAnchorEl(null);

    return (
        <>
            <IconButton
                size="small"
                onClick={(event) => setAnchorEl(event.currentTarget)}
            >
                <MoreVertIcon />
            </IconButton>

            <Menu
                anchorEl={anchorEl}
                open={open}
                onClose={closeMenu}
            >
                <MenuItem
                    onClick={() => {

                        closeMenu();

                        onEdit();

                    }}
                >
                    Edit
                </MenuItem>

                <MenuItem
                    onClick={() => {

                        closeMenu();

                        if (active) {
                            onDeactivate?.();
                        } else {
                            onActivate?.();
                        }

                    }}
                >
                    {active ? "Deactivate" : "Activate"}
                </MenuItem>

                <MenuItem
                    onClick={() => {

                        closeMenu();

                        onDelete?.();

                    }}
                >
                    Delete
                </MenuItem>

            </Menu>
        </>
    );

}