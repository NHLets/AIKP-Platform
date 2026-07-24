import {
    Drawer,
    List,
    ListItemButton,
    ListItemIcon,
    ListItemText,
    Toolbar,
    Typography,
    Divider,
    Box,
} from "@mui/material";

import PublicIcon from "@mui/icons-material/Public";
import BusinessIcon from "@mui/icons-material/Business";
import AccountBalanceIcon from "@mui/icons-material/AccountBalance";

import type { AppDrawerProps } from "./AppDrawer.types";

const drawerWidth = 240;

export default function AppDrawer({
    width = drawerWidth,
}: AppDrawerProps) {

    return (

        <Drawer
            variant="permanent"
            sx={{
                width,
                flexShrink: 0,
                "& .MuiDrawer-paper": {
                    width,
                    boxSizing: "border-box",
                },
            }}
        >

            <Toolbar />

            <Box sx={{ overflow: "auto" }}>

                <Typography
                    variant="overline"
                    sx={{
                        px: 2,
                        py: 1,
                        display: "block",
                    }}
                >
                    Reference Data
                </Typography>

                <List>

                    <ListItemButton selected>

                        <ListItemIcon>

                            <PublicIcon />

                        </ListItemIcon>

                        <ListItemText primary="Countries" />

                    </ListItemButton>

                    <ListItemButton disabled>

                        <ListItemIcon>

                            <BusinessIcon />

                        </ListItemIcon>

                        <ListItemText primary="Organizations" />

                    </ListItemButton>

                    <ListItemButton disabled>

                        <ListItemIcon>

                            <AccountBalanceIcon />

                        </ListItemIcon>

                        <ListItemText primary="Institutions" />

                    </ListItemButton>

                </List>

                <Divider />

            </Box>

        </Drawer>

    );

}