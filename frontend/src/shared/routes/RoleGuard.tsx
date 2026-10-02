import type { ReactNode } from "react";
import { Navigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

interface RoleGuardProps {
  roles: string[];
  children: ReactNode;
}

export default function RoleGuard({ roles, children }: RoleGuardProps) {
  const { user } = useAuth();

  if (!user) {
    return <Navigate to="/login" replace />;
  }

  if (!roles.includes(user.role)) {
    return (
      <div role="alert" style={{ padding: "2rem" }}>
        Access denied.
      </div>
    );
  }

  return <>{children}</>;
}
