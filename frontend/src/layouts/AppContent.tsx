import type { ReactNode } from "react";
import { Container } from "@mui/material";

interface AppContentProps {
    children: ReactNode;
}

export default function AppContent({
    children,
}: AppContentProps) {

    return (

        <Container
            maxWidth="xl"
            sx={{
                py: 4,
            }}
        >
            {children}
        </Container>

    );

}