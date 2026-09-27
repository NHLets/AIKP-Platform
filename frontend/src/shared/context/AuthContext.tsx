import {
  createContext,
  useContext,
  useMemo,
  useState,
  ReactNode,
} from "react";

import { LoginResponse } from "../../types/auth";
import {
  removeToken,
} from "../auth/tokenProvider";

interface AuthContextType {
  user: LoginResponse | null;
  isAuthenticated: boolean;
  login: (user: LoginResponse) => void;
  logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({ children }: { children: ReactNode }) {

  const [user, setUser] = useState<LoginResponse | null>(() => {
    const raw = localStorage.getItem(AUTH_USER_KEY);
    return raw ? JSON.parse(raw) : null;
  });

  const login = (response: LoginResponse) => {
    setUser(response);
    localStorage.setItem(AUTH_USER_KEY, JSON.stringify(response));
  };

  const logout = () => {
    removeToken();
    localStorage.removeItem(AUTH_USER_KEY);
    setUser(null);
  };

  const value = useMemo(
    () => ({
      user,
      isAuthenticated: user !== null,
      login,
      logout,
    }),
    [user]
  );

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {

  const context = useContext(AuthContext);

  if (!context) {
    throw new Error("useAuth must be used inside AuthProvider");
  }

  return context;
}


// RBAC helper (WF-14B.7)
const role = user?.role;
const userId = user?.userId;
