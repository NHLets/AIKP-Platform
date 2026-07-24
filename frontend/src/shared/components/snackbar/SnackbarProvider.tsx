import { useCallback, useEffect, useMemo, useState } from "react";

import { SnackbarAlert } from "./SnackbarAlert";
import { SnackbarContext } from "./SnackbarContext";
import type {
    SnackbarContextValue,
    SnackbarMessage,
    SnackbarProviderProps,
    SnackbarSeverity,
} from "./Snackbar.types";

const DEFAULT_AUTO_HIDE_DURATION = 4000;

export function SnackbarProvider({
    children,
}: SnackbarProviderProps) {

    const [queue, setQueue] = useState<SnackbarMessage[]>([]);

    const [current, setCurrent] = useState<SnackbarMessage | null>(null);

    const [open, setOpen] = useState(false);

    const enqueue = useCallback(
        (
            severity: SnackbarSeverity,
            message: string,
            autoHideDuration = DEFAULT_AUTO_HIDE_DURATION,
        ) => {

            const notification: SnackbarMessage = {
                id: crypto.randomUUID(),
                severity,
                message,
                autoHideDuration,
            };

            setQueue(previous => [...previous, notification]);

        },
        [],
    );

    useEffect(() => {

        if (current !== null) {
            return;
        }

        if (queue.length === 0) {
            return;
        }

        setCurrent(queue[0]);

        setQueue(previous => previous.slice(1));

        setOpen(true);

    }, [current, queue]);

    const handleClose = useCallback(() => {

        setOpen(false);

    }, []);

    const handleExited = useCallback(() => {

        setCurrent(null);

    }, []);

    const value = useMemo<SnackbarContextValue>(
        () => ({
            success: (message, duration) =>
                enqueue("success", message, duration),

            error: (message, duration) =>
                enqueue("error", message, duration),

            warning: (message, duration) =>
                enqueue("warning", message, duration),

            info: (message, duration) =>
                enqueue("info", message, duration),
        }),
        [enqueue],
    );

    return (
        <SnackbarContext.Provider value={value}>

            {children}

            <SnackbarAlert
                notification={current}
                open={open}
                onClose={handleClose}
                onExited={handleExited}
            />

        </SnackbarContext.Provider>
    );

}