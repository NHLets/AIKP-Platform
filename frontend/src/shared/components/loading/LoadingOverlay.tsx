import Backdrop from "@mui/material/Backdrop";
import CircularProgress from "@mui/material/CircularProgress";

export interface LoadingOverlayProps {
    open: boolean;
}

export default function LoadingOverlay({
    open,
}: LoadingOverlayProps) {
    return (
        <Backdrop
            open={open}
            sx={(theme) => ({
                color: "#fff",
                zIndex: theme.zIndex.modal + 1,
            })}
        >
            <CircularProgress color="inherit" />
        </Backdrop>
    );
}