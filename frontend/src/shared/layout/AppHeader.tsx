import MenuIcon from "@mui/icons-material/Menu";
import AccountCircleIcon from "@mui/icons-material/AccountCircle";
import {
    AppBar,
    Box,
    IconButton,
    Toolbar,
    Tooltip,
    Typography,
} from "@mui/material";

import type { AppHeaderProps } from "./Layout.types";

export default function AppHeader({
    title = "AIKP Portal",
}: AppHeaderProps) {
    return (
        <AppBar
            position="sticky"
            elevation={1}
        >
            <Toolbar>
                <IconButton
                    edge="start"
                    color="inherit"
                    aria-label="Open navigation menu"
                    sx={{ mr: 2 }}
                >
                    <MenuIcon />
                </IconButton>

                <Typography
                    variant="h6"
                    component="h1"
                    sx={{ flexGrow: 1 }}
                >
                    {title}
                </Typography>

                <Box>
                    <Tooltip title="User account">
                        <IconButton
                            color="inherit"
                            aria-label="User account"
                        >
                            <AccountCircleIcon />
                        </IconButton>
                    </Tooltip>
                </Box>
            </Toolbar>
        </AppBar>
    );
}