import { SnackbarProvider as NotistackProvider } from "notistack";

interface SnackbarProviderProps {
    children: React.ReactNode;
}

export default function SnackbarProvider({
    children,
}: SnackbarProviderProps) {

    return (
        <NotistackProvider
            maxSnack={3}
            autoHideDuration={4000}
            anchorOrigin={{
                vertical: "bottom",
                horizontal: "right",
            }}
            preventDuplicate
        >
            {children}
        </NotistackProvider>
    );

}