import { createTheme } from "@mui/material/styles";
import { palette } from "./palette";
import { typography } from "./typography";

export const theme = createTheme({
    palette,
    typography: {
        ...typography,
        fontSize: 13,
        h1: { fontSize: "1.5rem", lineHeight: 1.25, fontWeight: 600 },
        h2: { fontSize: "1.375rem", lineHeight: 1.3, fontWeight: 600 },
        h3: { fontSize: "1.25rem", lineHeight: 1.35, fontWeight: 600 },
        h4: { fontSize: "1.125rem", lineHeight: 1.35, fontWeight: 600 },
        h5: { fontSize: "1rem", lineHeight: 1.4, fontWeight: 600 },
        h6: { fontSize: "0.875rem", lineHeight: 1.4, fontWeight: 600 },
        subtitle1: { fontSize: "0.875rem", lineHeight: 1.4 },
        subtitle2: { fontSize: "0.8125rem", lineHeight: 1.4, fontWeight: 600 },
        body1: { fontSize: "0.8125rem", lineHeight: 1.45 },
        body2: { fontSize: "0.75rem", lineHeight: 1.4 },
        caption: { fontSize: "0.6875rem", lineHeight: 1.35 },
        button: { fontSize: "0.8125rem", textTransform: "none" },
    },
});