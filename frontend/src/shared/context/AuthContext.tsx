import {
  createContext,
  useContext,
  useMemo,
  useState,
} from "react";
import type { ReactNode } from "react";

import type { LoginResponse } from "../../types/auth";
import {
  removeToken,
  setToken,
} from "../auth/tokenProvider";

const AUTH_USER_KEY = "AIKP_AUTH_USER";

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

    const token =
      (response as any).accessToken ??
      (response as any).token ??
      (response as any).jwt;

    if (token) {
      setToken(token);
    }
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
