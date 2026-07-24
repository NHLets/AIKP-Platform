import { useCallback, useMemo, useState } from "react";

import { LoadingContext } from "./LoadingContext";
import LoadingOverlay from "./LoadingOverlay";

import type { PropsWithChildren } from "react";

export default function LoadingProvider({
    children,
}: PropsWithChildren) {
    const [counter, setCounter] = useState(0);

    const show = useCallback(() => {
        setCounter(value => value + 1);
    }, []);

    const hide = useCallback(() => {
        setCounter(value => Math.max(0, value - 1));
    }, []);

    const run = useCallback(
        async <T,>(action: () => Promise<T>): Promise<T> => {
            show();

            try {
                return await action();
            } finally {
                hide();
            }
        },
        [show, hide],
    );

    const value = useMemo(
        () => ({
            loading: counter > 0,
            show,
            hide,
            run,
        }),
        [counter, show, hide, run],
    );

    return (
        <LoadingContext.Provider value={value}>
            {children}

            <LoadingOverlay
                open={counter > 0}
            />
        </LoadingContext.Provider>
    );
}