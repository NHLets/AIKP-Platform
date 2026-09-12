import {
    createBrowserRouter,
    Navigate,
} from "react-router-dom";

import HomePage from "@/pages/home/HomePage";
import CountryListPage from "@/modules/country/pages/CountryListPage";
import ValidationListPage from "@/modules/validation/pages/ValidationListPage";
import ValidationReviewPage from "@/modules/validation/pages/ValidationReviewPage";
import CampaignListPage from "@/modules/campaign/pages/CampaignListPage";
import CampaignDetailPage from "@/modules/campaign/pages/CampaignDetailPage";

import { AppLayout } from "@/shared/layout";

export const router = createBrowserRouter([
    {
        path: "/",
        element: <AppLayout />,
        children: [
            {
                index: true,
                element: (
                    <Navigate
                        to="/dashboard"
                        replace
                    />
                ),
            },
            {
                path: "dashboard",
                element: <HomePage />,
            },
            {
                path: "campaigns",
                element: <CampaignListPage />,
            },
            {
                path: "campaigns/:id",
                element: <CampaignDetailPage />,
            },
            {
                path: "validation",
                element: <ValidationListPage />,
            },
            {
                path: "validation/:id",
                element: <ValidationReviewPage />,
            },
            {
                path: "countries",
                element: <CountryListPage />,
            },
        ],
    },
]);