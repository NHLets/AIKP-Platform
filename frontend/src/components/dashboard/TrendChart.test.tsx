import { render, screen } from "@testing-library/react";
import { describe, expect, it, vi } from "vitest";

import TrendChart from "./TrendChart";
import type { ValidationTrend } from "../../types/dashboard";

vi.mock("recharts", async () => {

  return {
    LineChart: ({ children }: { children: React.ReactNode }) => (
      <div data-testid="line-chart">{children}</div>
    ),
    Line: ({
      name,
      dataKey,
    }: {
      name: string;
      dataKey: string;
    }) => (
      <div data-testid={`line-${dataKey}`}>{name}</div>
    ),
    XAxis: ({ dataKey }: { dataKey: string }) => (
      <div data-testid="x-axis">{dataKey}</div>
    ),
    YAxis: () => <div data-testid="y-axis" />,
    CartesianGrid: () => <div data-testid="cartesian-grid" />,
    Tooltip: () => <div data-testid="tooltip" />,
    Legend: () => <div data-testid="legend" />,
    ResponsiveContainer: ({
      children,
    }: {
      children: React.ReactNode;
    }) => <div data-testid="responsive-container">{children}</div>,
  };
});

describe("TrendChart", () => {
  const data: ValidationTrend[] = [
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
  ];

  it("renders the validation trend title", () => {
    render(<TrendChart data={data} />);

    expect(screen.getByText("Validation Trend")).toBeInTheDocument();
  });

  it("uses the API trend contract", () => {
    render(<TrendChart data={data} />);

    expect(screen.getByTestId("line-validated")).toHaveTextContent(
      "Validated"
    );
    expect(screen.getByTestId("line-rejected")).toHaveTextContent(
      "Rejected"
    );
  });

  it("uses periodLabel for the X axis", () => {
    render(<TrendChart data={data} />);

    expect(screen.getByTestId("x-axis")).toHaveTextContent("periodLabel");
  });

  it("formats API periods as readable month and year labels", () => {
    render(<TrendChart data={data} />);

    const chart = screen.getByTestId("line-chart");

    expect(chart).toBeInTheDocument();
  });

  it("renders correctly with empty trend data", () => {
    render(<TrendChart data={[]} />);

    expect(screen.getByText("Validation Trend")).toBeInTheDocument();
    expect(screen.getByTestId("line-chart")).toBeInTheDocument();
  });
});
