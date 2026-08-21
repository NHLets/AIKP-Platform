import MenuIcon from "@mui/icons-material/Menu";
import NotificationsOutlinedIcon from "@mui/icons-material/NotificationsOutlined";
import AccountCircleOutlinedIcon from "@mui/icons-material/AccountCircleOutlined";

import {
    AppBar,
    Avatar,
    Badge,
    Box,
    Chip,
    IconButton,
    Toolbar,
    Tooltip,
    Typography,
} from "@mui/material";

export default function AppHeader() {
    return (
        <AppBar
            position="fixed"
            elevation={0}
            sx={{
                zIndex: (theme) => theme.zIndex.drawer + 1,
                bgcolor: "background.paper",
                color: "text.primary",
                borderBottom: 1,
                borderColor: "divider",
            }}
        >
            <Toolbar
                sx={{
                    minHeight: 72,
                    px: {
                        xs: 2,
                        md: 3,
                    },
                }}
            >
                <Box
                    sx={{
                        display: "flex",
                        alignItems: "center",
                        gap: 2,
                        flexGrow: 1,
                    }}
                >
                    <IconButton
                        edge="start"
                        aria-label="Open navigation menu"
                    >
                        <MenuIcon />
                    </IconButton>

                    <Box
                        sx={{
                            display: "flex",
                            flexDirection: "column",
                        }}
                    >
                        <Typography
                            variant="h6"
                            component="div"
                            sx={{
                                fontWeight: 700,
                                lineHeight: 1.2,
                            }}
                        >
                            AIKP Platform
                        </Typography>

                        <Typography
                            variant="caption"
                            color="text.secondary"
                        >
                            Africa Infrastructure Knowledge Platform
                        </Typography>
                    </Box>

                    <Chip
                        label="Data Collection 2026"
                        size="small"
                        sx={{
                            ml: 2,
                            fontWeight: 600,
                        }}
                    />
                </Box>

                <Box
                    sx={{
                    display: "flex",
                    alignItems: "center",
                    gap: 1,
                    }}
                >

                    <Tooltip title="Notifications">
                        <IconButton
                            aria-label="Notifications"
                        >
                            <Badge
                                badgeContent={3}
                                color="error"
                            >
                                <NotificationsOutlinedIcon />
                            </Badge>
                        </IconButton>
                    </Tooltip>

                    <Tooltip title="User account">
                        <IconButton
                            aria-label="User account"
                        >
                            <Avatar
                                sx={{
                                    width: 34,
                                    height: 34,
                                }}
                            >
                                <AccountCircleOutlinedIcon />
                            </Avatar>
                        </IconButton>
                    </Tooltip>
                </Box>
            </Toolbar>
        </AppBar>
    );
}