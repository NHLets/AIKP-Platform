export interface LoadingContextValue {
    /**
     * True when at least one loading operation is running.
     */
    loading: boolean;

    /**
     * Increment the loading counter.
     */
    show(): void;

    /**
     * Decrement the loading counter.
     */
    hide(): void;

    /**
     * Execute an asynchronous action while automatically
     * displaying and hiding the global loading overlay.
     */
    run<T>(action: () => Promise<T>): Promise<T>;
}