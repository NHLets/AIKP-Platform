import { Component, type ErrorInfo, type ReactNode } from "react";

import ErrorFallback from "./ErrorFallback";
import type { ErrorBoundaryProps } from "./ErrorBoundary.types";

interface ErrorBoundaryState {
    hasError: boolean;
    error: Error | null;
}

export default class ErrorBoundary extends Component<
    ErrorBoundaryProps,
    ErrorBoundaryState
> {
    constructor(props: ErrorBoundaryProps) {
        super(props);

        this.state = {
            hasError: false,
            error: null,
        };
    }

    static getDerivedStateFromError(error: Error): ErrorBoundaryState {
        return {
            hasError: true,
            error,
        };
    }

    componentDidCatch(error: Error, errorInfo: ErrorInfo): void {
        console.error("Application error:", error, errorInfo);
    }

    private handleReset = () => {
        this.setState({
            hasError: false,
            error: null,
        });
    };

    override render(): ReactNode {
        if (this.state.hasError && this.state.error) {
            return (
                <ErrorFallback
                    error={this.state.error}
                    onReset={this.handleReset}
                />
            );
        }

        return this.props.children;
    }
}