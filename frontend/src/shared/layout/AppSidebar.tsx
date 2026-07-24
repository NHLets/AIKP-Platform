import ChevronLeftIcon from "@mui/icons-material/ChevronLeft";
import DashboardOutlinedIcon from "@mui/icons-material/DashboardOutlined";
import PublicOutlinedIcon from "@mui/icons-material/PublicOutlined";
import MenuBookOutlinedIcon from "@mui/icons-material/MenuBookOutlined";
import SettingsOutlinedIcon from "@mui/icons-material/SettingsOutlined";

import Box from "@mui/material/Box";
import Divider from "@mui/material/Divider";
import Drawer from "@mui/material/Drawer";
import IconButton from "@mui/material/IconButton";
import List from "@mui/material/List";
import ListItemButton from "@mui/material/ListItemButton";
import ListItemIcon from "@mui/material/ListItemIcon";
import ListItemText from "@mui/material/ListItemText";
import Toolbar from "@mui/material/Toolbar";

import type { AppSidebarProps } from "./Layout.types";

const drawerWidth = 280;

export default function AppSidebar({
    open,
    onClose,
}: AppSidebarProps) {
    return (
        <Drawer
            open={open}
            onClose={onClose}
            variant="temporary"
            ModalProps={{
                keepMounted: true,
            }}
            sx={{
                "& .MuiDrawer-paper": {
                    width: drawerWidth,
                },
            }}
        >
            <Toolbar
                sx={{
                    display: "flex",
                    justifyContent: "flex-end",
                }}
            >
                <IconButton onClick={onClose}>
                    <ChevronLeftIcon />
                </IconButton>
            </Toolbar>

            <Divider />

            <Box sx={{ overflow: "auto" }}>
                <List>
                    <ListItemButton>
                        <ListItemIcon>
                            <DashboardOutlinedIcon />
                        </ListItemIcon>
                        <ListItemText primary="Dashboard" />
                    </ListItemButton>

                    <ListItemButton>
                        <ListItemIcon>
                            <PublicOutlinedIcon />
                        </ListItemIcon>
                        <ListItemText primary="Countries" />
                    </ListItemButton>

                    <ListItemButton>
                        <ListItemIcon>
                            <MenuBookOutlinedIcon />
                        </ListItemIcon>
                        <ListItemText primary="Reference Data" />
                    </ListItemButton>

                    <ListItemButton>
                        <ListItemIcon>
                            <SettingsOutlinedIcon />
                        </ListItemIcon>
                        <ListItemText primary="Administration" />
                    </ListItemButton>
                </List>
            </Box>
        </Drawer>
    );
}