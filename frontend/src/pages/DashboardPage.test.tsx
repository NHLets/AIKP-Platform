import { render, screen } from "@testing-library/react";
import { describe, expect, it, vi } from "vitest";

import DashboardPage from "./DashboardPage";

const mockUseCampaign = vi.fn();
const mockUseDashboardOverview = vi.fn();

vi.mock("../shared/context/CampaignContext", () => ({
  useCampaign: () => mockUseCampaign(),
}));

vi.mock("../hooks/useDashboardOverview", () => ({
  useDashboardOverview: (...args: unknown[]) =>
    mockUseDashboardOverview(...args),
}));

vi.mock("../components/dashboard/KpiCards", () => ({
  default: ({ data }: { data: unknown }) => (
    <div data-testid="kpi-cards">
      KPI {JSON.stringify(data)}
    </div>
  ),
}));

vi.mock("../components/dashboard/SeverityChart", () => ({
  default: ({ data }: { data: unknown }) => (
    <div data-testid="severity-chart">
      Severity {JSON.stringify(data)}
    </div>
  ),
}));

vi.mock("../components/dashboard/TrendChart", () => ({
  default: ({ data }: { data: unknown }) => (
    <div data-testid="trend-chart">
      Trend {JSON.stringify(data)}
    </div>
  ),
}));

vi.mock("../components/dashboard/Heatmap", () => ({
  default: ({ data }: { data: unknown }) => (
    <div data-testid="heatmap">
      Heatmap {JSON.stringify(data)}
    </div>
  ),
}));

vi.mock("../shared/components/CampaignSelector", () => ({
  default: () => <div data-testid="campaign-selector">Campaign Selector</div>,
}));

vi.mock("../shared/components/ReferenceYearSelector", () => ({
  default: () => (
    <div data-testid="reference-year-selector">
      Reference Year Selector
    </div>
  ),
}));

describe("DashboardPage", () => {
  const dashboardData = {
    kpiSummary: {
      totalComments: 3,
      criticalComments: 1,
      affectedVariables: 2,
      affectedQuestionnaires: 1,
    },
    severityDistribution: [
      {
        severity: "CRITICAL",
        count: 1,
      },
      {
        severity: "HIGH",
        count: 1,
      },
      {
        severity: "MEDIUM",
        count: 1,
      },
    ],
    validationTrend: [
      {
        period: "2026-01",
        validated: 1,
        rejected: 1,
      },
      {
        period: "2026-02",
        validated: 1,
        rejected: 0,
      },
    ],
    validationHeatmap: [
      {
        questionnaire: "PW_B",
        variable: "b001",
        count: 2,
      },
      {
        questionnaire: "PW_B",
        variable: "b002",
        count: 1,
      },
    ],
  };

  it("shows a loading state while analytics data is loading", () => {
    mockUseCampaign.mockReturnValue({
      campaignId: "e834a2a1-52a8-4d89-8795-f94b4f699a04",
      referenceYear: 2026,
    });

    mockUseDashboardOverview.mockReturnValue({
      data: undefined,
      isLoading: true,
      error: null,
    });

    render(<DashboardPage />);

    expect(screen.getByRole("progressbar")).toBeInTheDocument();
  });

  it("shows an error state when analytics data cannot be loaded", () => {
    mockUseCampaign.mockReturnValue({
      campaignId: "e834a2a1-52a8-4d89-8795-f94b4f699a04",
      referenceYear: 2026,
    });

    mockUseDashboardOverview.mockReturnValue({
      data: undefined,
      isLoading: false,
      error: new Error("API error"),
    });

    render(<DashboardPage />);

    expect(
      screen.getByText("Unable to load dashboard.")
    ).toBeInTheDocument();
  });

  it("renders the analytics dashboard with API data", () => {
    mockUseCampaign.mockReturnValue({
      campaignId: "e834a2a1-52a8-4d89-8795-f94b4f699a04",
      referenceYear: 2026,
    });

    mockUseDashboardOverview.mockReturnValue({
      data: dashboardData,
      isLoading: false,
      error: null,
    });

    render(<DashboardPage />);

    expect(
      screen.getByText("AIKP Analytics Dashboard")
    ).toBeInTheDocument();

    expect(screen.getByTestId("campaign-selector")).toBeInTheDocument();
    expect(
      screen.getByTestId("reference-year-selector")
    ).toBeInTheDocument();

    expect(screen.getByTestId("kpi-cards")).toHaveTextContent(
      "totalComments"
    );

    expect(screen.getByTestId("severity-chart")).toHaveTextContent(
      "CRITICAL"
    );

    expect(screen.getByTestId("trend-chart")).toHaveTextContent(
      "validated"
    );

    expect(screen.getByTestId("heatmap")).toHaveTextContent(
      "PW_B"
    );
  });

  it("passes the selected campaign and reference year to the analytics hook", () => {
    const campaignId =
      "e834a2a1-52a8-4d89-8795-f94b4f699a04";

    mockUseCampaign.mockReturnValue({
      campaignId,
      referenceYear: 2026,
    });

    mockUseDashboardOverview.mockReturnValue({
      data: dashboardData,
      isLoading: false,
      error: null,
    });

    render(<DashboardPage />);

    expect(mockUseDashboardOverview).toHaveBeenCalledWith(
      campaignId,
      2026
    );
  });
});
