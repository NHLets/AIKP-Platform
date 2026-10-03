import { createBrowserRouter, Navigate } from "react-router-dom";

import LoginPage from "@/modules/auth/pages/LoginPage";

import CampaignDetailPage from "@/modules/campaign/pages/CampaignDetailPage";
import CampaignListPage from "@/modules/campaign/pages/CampaignListPage";

import CreateDataCollectionPage from "@/modules/data-collection/pages/CreateDataCollectionPage";
import DataCollectionDetailPage from "@/modules/data-collection/pages/DataCollectionDetailPage";
import DataCollectionListPage from "@/modules/data-collection/pages/DataCollectionListPage";

import CreateQuestionnairePage from "@/modules/questionnaire/pages/CreateQuestionnairePage";
import EditQuestionnairePage from "@/modules/questionnaire/pages/EditQuestionnairePage";
import QuestionnaireDetailPage from "@/modules/questionnaire/pages/QuestionnaireDetailPage";
import QuestionnaireListPage from "@/modules/questionnaire/pages/QuestionnaireListPage";

import SubmissionListPage from "@/modules/submission/pages/SubmissionListPage";

import ValidationListPage from "@/modules/validation/pages/ValidationListPage";
import ValidationReviewPage from "@/modules/validation/pages/ValidationReviewPage";

import DashboardPage from "@/pages/DashboardPage";
import HomePage from "@/pages/home/HomePage";
import { AppLayout } from "@/shared/layout";
import RoleGuard from "@/shared/routes/RoleGuard";

const ALL_ROLES = [
  "ADMIN",
  "COORDINATOR",
  "VALIDATOR",
  "DATA_PROVIDER",
];

export const router = createBrowserRouter([
  {
    path: "/login",
    element: <LoginPage />,
  },
  {
    path: "/",
    element: <AppLayout />,
    children: [
      {
        index: true,
        element: <Navigate to="/dashboard" replace />,
      },
      {
        path: "dashboard",
        element: (
          <RoleGuard roles={ALL_ROLES}>
            <HomePage />
          </RoleGuard>
        ),
      },
      {
        path: "analytics",
        element: (
          <RoleGuard roles={ALL_ROLES}>
            <DashboardPage />
          </RoleGuard>
        ),
      },

      {
        path: "data-collections",
        element: (
          <RoleGuard roles={ALL_ROLES}>
            <DataCollectionListPage />
          </RoleGuard>
        ),
      },
      {
        path: "data-collections/new",
        element: (
          <RoleGuard roles={ALL_ROLES}>
            <CreateDataCollectionPage />
          </RoleGuard>
        ),
      },
      {
        path: "data-collections/:id",
        element: (
          <RoleGuard roles={ALL_ROLES}>
            <DataCollectionDetailPage />
          </RoleGuard>
        ),
      },

      {
        path: "campaigns",
        element: (
          <RoleGuard roles={ALL_ROLES}>
            <CampaignListPage />
          </RoleGuard>
        ),
      },
      {
        path: "campaigns/:id",
        element: (
          <RoleGuard roles={ALL_ROLES}>
            <CampaignDetailPage />
          </RoleGuard>
        ),
      },

      {
        path: "questionnaires",
        element: (
          <RoleGuard roles={ALL_ROLES}>
            <QuestionnaireListPage />
          </RoleGuard>
        ),
      },
      {
        path: "questionnaires/new",
        element: (
          <RoleGuard roles={ALL_ROLES}>
            <CreateQuestionnairePage />
          </RoleGuard>
        ),
      },
      {
        path: "questionnaires/:id/edit",
        element: (
          <RoleGuard roles={ALL_ROLES}>
            <EditQuestionnairePage />
          </RoleGuard>
        ),
      },
      {
        path: "questionnaires/:id",
        element: (
          <RoleGuard roles={ALL_ROLES}>
            <QuestionnaireDetailPage />
          </RoleGuard>
        ),
      },

      {
        path: "submissions",
        element: (
          <RoleGuard roles={ALL_ROLES}>
            <SubmissionListPage />
          </RoleGuard>
        ),
      },

      {
        path: "validation",
        element: (
          <RoleGuard roles={ALL_ROLES}>
            <ValidationListPage />
          </RoleGuard>
        ),
      },
      {
        path: "validation/:id",
        element: (
          <RoleGuard roles={ALL_ROLES}>
            <ValidationReviewPage />
          </RoleGuard>
        ),
      },
    ],
  },
]);
