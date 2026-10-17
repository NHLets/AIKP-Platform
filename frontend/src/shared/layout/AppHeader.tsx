import MenuIcon from "@mui/icons-material/Menu";
import NotificationsNoneOutlinedIcon from "@mui/icons-material/NotificationsNoneOutlined";
import AccountCircleOutlinedIcon from "@mui/icons-material/AccountCircleOutlined";
import { useAuth } from "@/shared/context/AuthContext";

import {
    AppBar,
    Box,
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
    const { user } = useAuth();
    const userLabel = user?.email || user?.userId || "Utilisateur";
    const userTooltip = user
        ? `${userLabel} — ${user.role}`
        : "Utilisateur non identifié";

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

                <Tooltip title={userTooltip}>
                    <IconButton
                        color="inherit"
                        aria-label={userTooltip}
                        sx={{ ml: 1, borderRadius: 2, gap: 1 }}
                    >
                        <AccountCircleOutlinedIcon />
                        <Box
                            component="span"
                            sx={{
                                display: { xs: "none", sm: "inline" },
                                textTransform: "none",
                                fontSize: "0.875rem",
                                fontWeight: 500,
                                maxWidth: 220,
                                overflow: "hidden",
                                textOverflow: "ellipsis",
                                whiteSpace: "nowrap",
                            }}
                        >
                            {userLabel}
                        </Box>
                    </IconButton>
                </Tooltip>
            </Toolbar>
        </AppBar>
    );
}