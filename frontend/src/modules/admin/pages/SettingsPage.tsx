import { useEffect, useState } from "react";
import {
  Alert,
  Box,
  Button,
  CircularProgress,
  Paper,
  Stack,
  TextField,
  Typography,
} from "@mui/material";
import SaveOutlinedIcon from "@mui/icons-material/SaveOutlined";
import RefreshOutlinedIcon from "@mui/icons-material/RefreshOutlined";
import { settingsApi } from "../services/settingsApi";

export default function SettingsPage() {
  const [platformName, setPlatformName] = useState("");
  const [platformDescription, setPlatformDescription] = useState("");
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState("");
  const [notice, setNotice] = useState("");

  async function loadSettings() {
    setLoading(true);
    setError("");
    setNotice("");
    try {
      const settings = await settingsApi.get();
      setPlatformName(settings.platformName);
      setPlatformDescription(settings.platformDescription);
    } catch (err) {
      setError(err instanceof Error ? err.message : "Unable to load platform settings.");
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    void loadSettings();
  }, []);

  async function saveSettings() {
    setError("");
    setNotice("");

    const name = platformName.trim();
    const description = platformDescription.trim();

    if (!name || name.length > 120) {
      setError("The platform name is required and must not exceed 120 characters.");
      return;
    }
    if (description.length > 1000) {
      setError("The description must not exceed 1,000 characters.");
      return;
    }

    setSaving(true);
    try {
      const saved = await settingsApi.update({
        platformName: name,
        platformDescription: description,
      });
      setPlatformName(saved.platformName);
      setPlatformDescription(saved.platformDescription);
      setNotice("Settings have been saved successfully.");
    } catch (err) {
      setError(err instanceof Error ? err.message : "Unable to save settings.");
    } finally {
      setSaving(false);
    }
  }

  return (
    <Box sx={{ maxWidth: 900, mx: "auto", p: { xs: 2, md: 3 } }}>
      <Stack spacing={1} sx={{ mb: 3 }}>
        <Typography variant="h4" sx={{ fontWeight: 700 }}>Settings</Typography>
        <Typography color="text.secondary">
          General configuration and metadata for the AIKP platform.
        </Typography>
      </Stack>

      {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}
      {notice && <Alert severity="success" sx={{ mb: 2 }}>{notice}</Alert>}

      <Paper variant="outlined" sx={{ p: { xs: 2, md: 3 } }}>
        <Stack spacing={2.5}>
          <Box>
            <Typography variant="h6">Platform information</Typography>
            <Typography variant="body2" color="text.secondary">
              This information is stored in the database. Displaying it on other screens
              will require specific integration on those screens.
            </Typography>
          </Box>

          {loading ? (
            <Box sx={{ display: "flex", justifyContent: "center", py: 5 }}>
              <CircularProgress />
            </Box>
          ) : (
            <>
              <TextField
                label="Platform name"
                value={platformName}
                onChange={(event) => setPlatformName(event.target.value)}
                required
                fullWidth
                slotProps={{ htmlInput: { maxLength: 120 } }}
                helperText={`${platformName.length}/120 characters`}
              />
              <TextField
                label="Platform description"
                value={platformDescription}
                onChange={(event) => setPlatformDescription(event.target.value)}
                fullWidth
                multiline
                minRows={4}
                slotProps={{ htmlInput: { maxLength: 1000 } }}
                helperText={`${platformDescription.length}/1000 characters`}
              />
              <Stack direction={{ xs: "column", sm: "row" }} spacing={1}>
                <Button
                  variant="contained"
                  startIcon={<SaveOutlinedIcon />}
                  onClick={() => void saveSettings()}
                  disabled={saving || !platformName.trim()}
                >
                  {saving ? "Saving…" : "Save"}
                </Button>
                <Button
                  variant="outlined"
                  startIcon={<RefreshOutlinedIcon />}
                  onClick={() => void loadSettings()}
                  disabled={loading || saving}
                >
                  Recharger
                </Button>
              </Stack>
            </>
          )}
        </Stack>
      </Paper>
    </Box>
  );
}
