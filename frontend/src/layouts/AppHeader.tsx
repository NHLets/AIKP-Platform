import { AppBar, Toolbar, Typography } from "@mui/material";

export default function AppHeader() {

    return (

        <AppBar position="sticky" elevation={1}>

            <Toolbar>

                <Typography
                    variant="h6"
                    component="div"
                    sx={{ fontWeight: 600 }}
                >
                    AIKP Platform
                </Typography>

            </Toolbar>

        </AppBar>

    );

}