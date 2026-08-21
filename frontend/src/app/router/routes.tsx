import {
    createBrowserRouter,
    Navigate,
} from "react-router-dom";

import HomePage from "@/pages/home/HomePage";
import CountryListPage from "@/modules/country/pages/CountryListPage";
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
                path: "countries",
                element: <CountryListPage />,
            },
        ],
    },
]);