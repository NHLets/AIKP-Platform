import {
    createBrowserRouter,
    Navigate,
} from "react-router-dom";

import HomePage from "@/pages/home/HomePage";
import CountryListPage from "@/modules/country/pages/CountryListPage";
import SectorListPage from "@/modules/sector/pages/SectorListPage";
import CampaignListPage from "@/modules/campaign/pages/CampaignListPage";
import CampaignDetailPage from "@/modules/campaign/pages/CampaignDetailPage";
import SubmissionListPage from "@/modules/submission/pages/SubmissionListPage";
import DataCollectionDetailPage from "@/modules/data-collection/pages/DataCollectionDetailPage";
import CreateDataCollectionPage from "@/modules/data-collection/pages/CreateDataCollectionPage";
import DataCollectionListPage from "@/modules/data-collection/pages/DataCollectionListPage";
import ValidationListPage from "@/modules/validation/pages/ValidationListPage";
import ValidationReviewPage from "@/modules/validation/pages/ValidationReviewPage";
import QuestionnaireListPage from "@/modules/questionnaire/pages/QuestionnaireListPage";
import CreateQuestionnairePage from "@/modules/questionnaire/pages/CreateQuestionnairePage";
import QuestionnaireDetailPage from "@/modules/questionnaire/pages/QuestionnaireDetailPage";
import EditQuestionnairePage from "@/modules/questionnaire/pages/EditQuestionnairePage";

import UserManagementPage from "@/modules/admin/pages/UserManagementPage";
import OrganizationManagementPage from "@/modules/admin/pages/OrganizationManagementPage";
import PersonManagementPage from "@/modules/admin/pages/PersonManagementPage";
import InvitationPage from "@/modules/admin/pages/InvitationPage";

import DashboardPage from "../../pages/DashboardPage";

import { AppLayout } from "@/shared/layout";
import RoleGuard from "@/shared/routes/RoleGuard";

export const router = createBrowserRouter([
    
  {
    path: "/dashboard",
    element: (
          <RoleGuard roles={["ADMIN","COORDINATOR","VALIDATOR","DATA_PROVIDER"]}>
            <DashboardPage />
          </RoleGuard>
        ),
  },
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
                element: (
          <RoleGuard roles={["ADMIN","COORDINATOR"]}>
            <CampaignListPage />
          </RoleGuard>
        ),
            },
            {
                path: "campaigns/:id",
                element: <CampaignDetailPage />,
            },
            {
                path: "questionnaires",
                element: <QuestionnaireListPage />,
            },
            {
                path: "questionnaires/new",
                element: <CreateQuestionnairePage />,
            },
            {
                path: "questionnaires/:id",
                element: <QuestionnaireDetailPage />,
            },
            {
                path: "questionnaires/:id/edit",
                element: <EditQuestionnairePage />,
            },
            {
                path: "submissions",
                element: <SubmissionListPage />,
            },
            {
                path: "data-collections",
                element: <DataCollectionListPage />,
            },
            {
                path: "data-collections/new",
                element: <CreateDataCollectionPage />,
            },
            {
                path: "data-collections/:id",
                element: <DataCollectionDetailPage />,
            },
            {
                path: "validation",
                element: (
          <RoleGuard roles={["ADMIN","COORDINATOR","VALIDATOR"]}>
            <ValidationListPage />
          </RoleGuard>
        ),
            },
            {
                path: "validation/:id",
                element: <ValidationReviewPage />,
            },
            {
                path: "countries",
                element: <CountryListPage />,
            },
            {
                path: "sectors",
                element: <SectorListPage />,
            },

      {
        path: "users",
        element: (
          <RoleGuard roles={["ADMIN"]}>
            <UserManagementPage />
          </RoleGuard>
        ),
      },
      {
        path: "organizations",
        element: (
          <RoleGuard roles={["ADMIN"]}>
            <OrganizationManagementPage />
          </RoleGuard>
        ),
      },
      {
        path: "persons",
        element: (
          <RoleGuard roles={["ADMIN"]}>
            <PersonManagementPage />
          </RoleGuard>
        ),
      },
      {
        path: "invitations",
        element: (
          <RoleGuard roles={["ADMIN"]}>
            <InvitationPage />
          </RoleGuard>
        ),
      },

        ],
    },
]);