import { useState } from "react";
import { Navigate } from "react-router-dom";
import {
  Alert,
  Box,
  Button,
  Card,
  CardContent,
  Stack,
  TextField,
  Typography,
} from "@mui/material";

import { useAuth } from "@/shared/context/AuthContext";
import { getToken } from "@/shared/auth/tokenProvider";
import authApi from "@/api/authApi";

export default function LoginPage() {
  const { login, isAuthenticated } = useAuth();

  const [email, setEmail] = useState("admin@example.com");
  const [password, setPassword] = useState("admin");
  const [error, setError] = useState("");

  if (isAuthenticated && getToken()) {
    return <Navigate to="/dashboard" replace />;
  }

  const handleLogin = async () => {
    try {
      setError("");

      const response = await authApi.login({
        email,
        password,
      });

      login(response);

    } catch {
      setError("Invalid email or password");
    }
  };

  return (
    <Box
      sx={{
        minHeight: "100vh",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        bgcolor: "#F5F7FA",
      }}
    >
      <Card sx={{ width: 420 }}>
        <CardContent>
          <Stack spacing={3}>
            <Box>
              <Typography variant="h4" sx={{ fontWeight: 700 }}>
                AIKP Platform
              </Typography>
              <Typography color="text.secondary">
                African Infrastructure Knowledge Platform
              </Typography>
            </Box>

            {error && <Alert severity="error">{error}</Alert>}

            <TextField
              label="Email"
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              fullWidth
            />

            <TextField
              label="Password"
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              fullWidth
            />

            <Button
              variant="contained"
              size="large"
              onClick={handleLogin}
            >
              Sign In
            </Button>
          </Stack>
        </CardContent>
      </Card>
    </Box>
  );
}
