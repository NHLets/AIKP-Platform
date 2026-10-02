import { createBrowserRouter, Navigate } from "react-router-dom";

import LoginPage from "@/modules/auth/pages/LoginPage";
import DashboardPage from "@/pages/DashboardPage";
import { AppLayout } from "@/shared/layout";
import RoleGuard from "@/shared/routes/RoleGuard";

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
          <RoleGuard roles={["ADMIN","COORDINATOR","VALIDATOR","DATA_PROVIDER"]}>
            <DashboardPage />
          </RoleGuard>
        ),
      },
    ],
  },
]);
