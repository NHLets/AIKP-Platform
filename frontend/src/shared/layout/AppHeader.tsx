import MenuIcon from "@mui/icons-material/Menu";
import NotificationsNoneOutlinedIcon from "@mui/icons-material/NotificationsNoneOutlined";
import AccountCircleOutlinedIcon from "@mui/icons-material/AccountCircleOutlined";

import {
    AppBar,
    IconButton,
    Toolbar,
    Tooltip,
    Typography,
} from "@mui/material";

interface AppHeaderProps {
    title?: string;
    onMenuClick?: () => void;
}

export default function AppHeader({
    title = "AIKP Platform",
    onMenuClick,
}: AppHeaderProps) {
    return (
        <AppBar
            position="sticky"
            elevation={1}
        >
            <Toolbar
                sx={{
                    minHeight: {
                        xs: 64,
                        md: 72,
                    },
                }}
            >
                <IconButton
                    edge="start"
                    color="inherit"
                    aria-label="Open navigation menu"
                    onClick={onMenuClick}
                    sx={{
                        mr: 2,
                        display: {
                            xs: "inline-flex",
                            md: "none",
                        },
                    }}
                >
                    <MenuIcon />
                </IconButton>

                <Typography
                    variant="h6"
                    component="div"
                    sx={{
                        fontWeight: 700,
                        letterSpacing: 0.3,
                        flexGrow: 1,
                    }}
                >
                    {title}
                </Typography>

                <Tooltip title="Notifications">
                    <IconButton
                        color="inherit"
                        aria-label="Notifications"
                    >
                        <NotificationsNoneOutlinedIcon />
                    </IconButton>
                </Tooltip>

                <Tooltip title="User account">
                    <IconButton
                        color="inherit"
                        aria-label="User account"
                        sx={{ ml: 1 }}
                    >
                        <AccountCircleOutlinedIcon />
                    </IconButton>
                </Tooltip>
            </Toolbar>
        </AppBar>
    );
}