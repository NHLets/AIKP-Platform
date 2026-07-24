import { createBrowserRouter } from "react-router-dom";

import HomePage from "@/pages/home/HomePage";
import { AppLayout } from "@/shared/layout";

export const router = createBrowserRouter([
    {
        path: "/",
        element: (
            <AppLayout>
                <HomePage />
            </AppLayout>
        ),
    },
]);