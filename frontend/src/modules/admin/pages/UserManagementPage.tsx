import { useCallback, useEffect, useMemo, useState } from "react";
import {
  Alert,
  Box,
  Button,
  Chip,
  CircularProgress,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
  Paper,
  Stack,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  TextField,
  Typography,
} from "@mui/material";
import SearchOutlinedIcon from "@mui/icons-material/SearchOutlined";
import RefreshOutlinedIcon from "@mui/icons-material/RefreshOutlined";
import AddOutlinedIcon from "@mui/icons-material/AddOutlined";
import PersonOutlineOutlinedIcon from "@mui/icons-material/PersonOutlineOutlined";
import { adminApi } from "../services/adminApi";
import type { ActiveRole, CreateUserPayload } from "../services/adminApi";
import type { UserSummary } from "../types/admin";

function statusColor(status: string): "success" | "default" | "warning" | "error" {
  switch (status.toUpperCase()) {
    case "ACTIVE":
      return "success";
    case "SUSPENDED":
    case "LOCKED":
      return "error";
    case "INACTIVE":
    case "PENDING":
      return "warning";
    default:
      return "default";
  }
}

export default function UserManagementPage() {
  const [users, setUsers] = useState<UserSummary[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [search, setSearch] = useState("");
  const [statusFilter, setStatusFilter] = useState("ALL");
  const [selectedUser, setSelectedUser] = useState<UserSummary | null>(null);
  const [actionLoading, setActionLoading] = useState(false);
  const [notice, setNotice] = useState("");
  const [createDialogOpen, setCreateDialogOpen] = useState(false);
  const [createLoading, setCreateLoading] = useState(false);
  const [rolesLoading, setRolesLoading] = useState(false);
  const [roles, setRoles] = useState<ActiveRole[]>([]);
  const [createError, setCreateError] = useState("");
  const [newUser, setNewUser] = useState<CreateUserPayload>({
    username: "",
    email: "",
    fullName: "",
    password: "",
    roleId: "",
  });

  const loadUsers = useCallback(async () => {
    setLoading(true);
    setError("");

    try {
      const data = await adminApi.getUsers();
      setUsers(data);
    } catch (err) {
      setError(
        err instanceof Error && err.message
          ? err.message
          : "Unable to load users. Please try again.",
      );
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    void loadUsers();
  }, [loadUsers]);

  const openCreateDialog = async () => {
    setCreateError("");
    setError("");
    setNotice("");
    setNewUser({
      username: "",
      email: "",
      fullName: "",
      password: "",
      roleId: "",
    });
    setCreateDialogOpen(true);
    setRolesLoading(true);

    try {
      const data = await adminApi.getActiveRoles();
      setRoles(data);
      if (data.length > 0) {
        setNewUser((current) => ({ ...current, roleId: data[0].id }));
      }
    } catch (err) {
      setCreateError(
        err instanceof Error && err.message
          ? err.message
          : "Unable to load active roles.",
      );
    } finally {
      setRolesLoading(false);
    }
  };

  const handleCreateUser = async () => {
    setCreateError("");

    if (
      newUser.username.trim().length < 3 ||
      newUser.username.trim().length > 50
    ) {
      setCreateError("Username must contain between 3 and 50 characters.");
      return;
    }
    if (!newUser.fullName.trim() || newUser.fullName.trim().length < 2 ||
        newUser.fullName.trim().length > 150) {
      setCreateError("Full name must contain between 2 and 150 characters.");
      return;
    }
    if (!newUser.email.trim()) {
      setCreateError("Email is required.");
      return;
    }
    if (newUser.password.length < 8 || newUser.password.length > 128) {
      setCreateError("Password must contain between 8 and 128 characters.");
      return;
    }
    if (!newUser.roleId) {
      setCreateError("Please select an active role.");
      return;
    }

    setCreateLoading(true);
    setError("");
    setNotice("");

    try {
      await adminApi.createUser({
        ...newUser,
        username: newUser.username.trim(),
        fullName: newUser.fullName.trim(),
        email: newUser.email.trim(),
      });
      setCreateDialogOpen(false);
      setNotice(`User ${newUser.username.trim()} created successfully.`);
      await loadUsers();
    } catch (err) {
      setCreateError(
        err instanceof Error && err.message
          ? err.message
          : "The user could not be created.",
      );
    } finally {
      setCreateLoading(false);
    }
  };

  const statuses = useMemo(
    () => [...new Set(users.map((user) => user.status).filter(Boolean))].sort(),
    [users],
  );

  const filteredUsers = useMemo(() => {
    const term = search.trim().toLowerCase();

    return users.filter((user) => {
      const matchesSearch =
        !term ||
        [user.fullName, user.username, user.email, user.id]
          .some((value) => (value ?? "").toLowerCase().includes(term));

      const matchesStatus =
        statusFilter === "ALL" || user.status === statusFilter;

      return matchesSearch && matchesStatus;
    });
  }, [users, search, statusFilter]);

  const handleStatusChange = async () => {
    if (!selectedUser) return;

    setActionLoading(true);
    setError("");
    setNotice("");

    try {
      if (selectedUser.status.toUpperCase() === "ACTIVE") {
        await adminApi.deactivateUser(selectedUser.id);
        setNotice(`User ${selectedUser.username} deactivated successfully.`);
      } else {
        await adminApi.activateUser(selectedUser.id);
        setNotice(`User ${selectedUser.username} activated successfully.`);
      }

      setSelectedUser(null);
      await loadUsers();
    } catch (err) {
      setError(
        err instanceof Error && err.message
          ? err.message
          : "The user status could not be updated.",
      );
    } finally {
      setActionLoading(false);
    }
  };

  const activeCount = users.filter(
    (user) => user.status.toUpperCase() === "ACTIVE",
  ).length;

  return (
    <Box sx={{ p: { xs: 2, md: 3 }, maxWidth: 1440, mx: "auto" }}>
      <Stack
        direction={{ xs: "column", sm: "row" }}
        spacing={2}
        sx={{
          justifyContent: "space-between",
          alignItems: { xs: "stretch", sm: "center" },
          mb: 3,
        }}
      >
        <Box>
          <Typography variant="h4" sx={{ fontWeight: 700 }}>
            User Management
          </Typography>
          <Typography color="text.secondary" sx={{ mt: 0.5 }}>
            View and manage AIKP platform user accounts.
          </Typography>
        </Box>

        <Stack direction="row" spacing={1} sx={{ flexWrap: "wrap" }}>
          <Button
            variant="contained"
            startIcon={<AddOutlinedIcon />}
            onClick={() => void openCreateDialog()}
            disabled={createLoading || actionLoading}
          >
            Create User
          </Button>
          <Button
            variant="outlined"
            startIcon={<RefreshOutlinedIcon />}
            onClick={() => void loadUsers()}
            disabled={loading || actionLoading || createLoading}
          >
            Refresh
          </Button>
        </Stack>
      </Stack>

      {error && (
        <Alert
          severity="error"
          sx={{ mb: 2, whiteSpace: "pre-wrap", overflowWrap: "anywhere" }}
          action={
            <Button color="inherit" size="small" onClick={() => void loadUsers()}>
              Retry
            </Button>
          }
        >
          {error}
        </Alert>
      )}

      {notice && (
        <Alert severity="success" onClose={() => setNotice("")} sx={{ mb: 2 }}>
          {notice}
        </Alert>
      )}

      <Stack direction={{ xs: "column", sm: "row" }} spacing={2} sx={{ mb: 3 }}>
        <Paper variant="outlined" sx={{ p: 2, flex: 1 }}>
          <Stack direction="row" spacing={1.5} sx={{ alignItems: "center" }}>
            <PersonOutlineOutlinedIcon color="action" />
            <Box>
              <Typography variant="body2" color="text.secondary">
                Total users
              </Typography>
              <Typography variant="h5" sx={{ fontWeight: 700 }}>
                {users.length}
              </Typography>
            </Box>
          </Stack>
        </Paper>

        <Paper variant="outlined" sx={{ p: 2, flex: 1 }}>
          <Typography variant="body2" color="text.secondary">
            Active users
          </Typography>
          <Typography
            variant="h5"
            sx={{ fontWeight: 700, color: "success.main" }}
          >
            {activeCount}
          </Typography>
        </Paper>

        <Paper variant="outlined" sx={{ p: 2, flex: 1 }}>
          <Typography variant="body2" color="text.secondary">
            Displayed results
          </Typography>
          <Typography variant="h5" sx={{ fontWeight: 700 }}>
            {filteredUsers.length}
          </Typography>
        </Paper>
      </Stack>

      <Paper variant="outlined">
        <Stack
          direction={{ xs: "column", sm: "row" }}
          spacing={2}
          sx={{ p: 2 }}
        >
          <TextField
            fullWidth
            size="small"
            label="Search users"
            placeholder="Name, username, email or ID"
            value={search}
            onChange={(event) => setSearch(event.target.value)}
            slotProps={{
              input: {
                startAdornment: <SearchOutlinedIcon color="action" sx={{ mr: 1 }} />,
              },
            }}
          />

          <TextField
            select
            size="small"
            label="Status"
            value={statusFilter}
            onChange={(event) => setStatusFilter(event.target.value)}
            sx={{ minWidth: { sm: 190 } }}
            slotProps={{ select: { native: true } }}
          >
            <option value="ALL">All statuses</option>
            {statuses.map((status) => (
              <option key={status} value={status}>
                {status}
              </option>
            ))}
          </TextField>
        </Stack>

        {loading ? (
          <Stack spacing={2} sx={{ alignItems: "center", py: 8 }}>
            <CircularProgress />
            <Typography color="text.secondary">Loading users…</Typography>
          </Stack>
        ) : (
          <TableContainer>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell>User</TableCell>
                  <TableCell>Username</TableCell>
                  <TableCell>Email</TableCell>
                  <TableCell>Status</TableCell>
                  <TableCell align="right">Actions</TableCell>
                </TableRow>
              </TableHead>

              <TableBody>
                {filteredUsers.map((user) => {
                  const isActive = user.status.toUpperCase() === "ACTIVE";

                  return (
                    <TableRow key={user.id} hover>
                      <TableCell>
                        <Typography sx={{ fontWeight: 600 }}>
                          {user.fullName || "—"}
                        </Typography>
                        <Typography variant="caption" color="text.secondary">
                          {user.id}
                        </Typography>
                      </TableCell>
                      <TableCell>{user.username || "—"}</TableCell>
                      <TableCell>{user.email || "—"}</TableCell>
                      <TableCell>
                        <Chip
                          size="small"
                          label={user.status || "UNKNOWN"}
                          color={statusColor(user.status || "")}
                          variant="outlined"
                        />
                      </TableCell>
                      <TableCell align="right">
                        <Button
                          size="small"
                          variant="outlined"
                          color={isActive ? "warning" : "success"}
                          onClick={() => setSelectedUser(user)}
                          disabled={actionLoading}
                        >
                          {isActive ? "Deactivate" : "Activate"}
                        </Button>
                      </TableCell>
                    </TableRow>
                  );
                })}

                {filteredUsers.length === 0 && (
                  <TableRow>
                    <TableCell colSpan={5}>
                      <Typography
                        color="text.secondary"
                        sx={{ textAlign: "center", py: 5 }}
                      >
                        {users.length === 0
                          ? "No users found."
                          : "No users match your search or status filter."}
                      </Typography>
                    </TableCell>
                  </TableRow>
                )}
              </TableBody>
            </Table>
          </TableContainer>
        )}
      </Paper>

      <Dialog
        open={createDialogOpen}
        onClose={() => !createLoading && setCreateDialogOpen(false)}
        fullWidth
        maxWidth="sm"
      >
        <DialogTitle>Create user</DialogTitle>
        <DialogContent>
          <DialogContentText sx={{ mb: 2 }}>
            Enter the account details and select an active role.
          </DialogContentText>

          {createError && (
            <Alert severity="error" sx={{ mb: 2, whiteSpace: "pre-wrap", overflowWrap: "anywhere" }}>
              {createError}
            </Alert>
          )}

          <Stack spacing={2} sx={{ pt: 1 }}>
            <TextField
              label="Full name"
              value={newUser.fullName}
              onChange={(event) =>
                setNewUser((current) => ({ ...current, fullName: event.target.value }))
              }
              slotProps={{ htmlInput: { maxLength: 150 } }}
              required
              fullWidth
              autoComplete="name"
              disabled={createLoading}
            />
            <TextField
              label="Username"
              value={newUser.username}
              onChange={(event) =>
                setNewUser((current) => ({ ...current, username: event.target.value }))
              }
              slotProps={{ htmlInput: { minLength: 3, maxLength: 50 } }}
              required
              fullWidth
              autoComplete="username"
              disabled={createLoading}
            />
            <TextField
              label="Email"
              type="email"
              value={newUser.email}
              onChange={(event) =>
                setNewUser((current) => ({ ...current, email: event.target.value }))
              }
              required
              fullWidth
              autoComplete="email"
              disabled={createLoading}
            />
            <TextField
              label="Temporary password"
              type="password"
              value={newUser.password}
              onChange={(event) =>
                setNewUser((current) => ({ ...current, password: event.target.value }))
              }
              slotProps={{ htmlInput: { minLength: 8, maxLength: 128 } }}
              helperText="Use at least 8 characters. Share the password securely with the user."
              required
              fullWidth
              autoComplete="new-password"
              disabled={createLoading}
            />
            <TextField
              select
              label="Role"
              value={newUser.roleId}
              onChange={(event) =>
                setNewUser((current) => ({ ...current, roleId: event.target.value }))
              }
              required
              fullWidth
              disabled={createLoading || rolesLoading || roles.length === 0}
              slotProps={{ select: { native: true } }}
            >
              <option value="" disabled>Select a role</option>
              {roles.map((role) => (
                <option key={role.id} value={role.id}>
                  {role.name}
                </option>
              ))}
            </TextField>
            {rolesLoading && (
              <Typography variant="body2" color="text.secondary">
                Loading active roles…
              </Typography>
            )}
            {!rolesLoading && roles.length === 0 && (
              <Alert severity="warning">
                No active roles are available. Check role configuration or permissions.
              </Alert>
            )}
          </Stack>
        </DialogContent>
        <DialogActions>
          <Button
            onClick={() => setCreateDialogOpen(false)}
            disabled={createLoading}
          >
            Cancel
          </Button>
          <Button
            variant="contained"
            onClick={() => void handleCreateUser()}
            disabled={createLoading || rolesLoading || roles.length === 0}
          >
            {createLoading ? "Creating…" : "Create user"}
          </Button>
        </DialogActions>
      </Dialog>

      <Dialog
        open={selectedUser !== null}
        onClose={() => !actionLoading && setSelectedUser(null)}
      >
        <DialogTitle>
          {selectedUser?.status.toUpperCase() === "ACTIVE"
            ? "Deactivate user?"
            : "Activate user?"}
        </DialogTitle>
        <DialogContent>
          <DialogContentText>
            {selectedUser?.status.toUpperCase() === "ACTIVE"
              ? `Are you sure you want to deactivate ${selectedUser?.username}?`
              : `Are you sure you want to activate ${selectedUser?.username}?`}
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button
            onClick={() => setSelectedUser(null)}
            disabled={actionLoading}
          >
            Cancel
          </Button>
          <Button
            onClick={() => void handleStatusChange()}
            disabled={actionLoading}
            variant="contained"
            color={
              selectedUser?.status.toUpperCase() === "ACTIVE"
                ? "warning"
                : "success"
            }
          >
            {actionLoading ? "Please wait…" : "Confirm"}
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
}
