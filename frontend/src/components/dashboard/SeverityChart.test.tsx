import { render, screen } from "@testing-library/react";
import { describe, expect, it, vi } from "vitest";

import SeverityChart from "./SeverityChart";
import type { SeverityDistribution } from "../../types/dashboard";

vi.mock("recharts", async () => {

  return {
    PieChart: ({ children }: { children: React.ReactNode }) => (
      <div data-testid="pie-chart">{children}</div>
    ),
    Pie: ({
      data,
      dataKey,
      nameKey,
      children,
    }: {
      data: SeverityDistribution[];
      dataKey: string;
      nameKey: string;
      children: React.ReactNode;
    }) => (
      <div
        data-testid="pie"
        data-count-key={dataKey}
        data-name-key={nameKey}
        data-count={data.length}
      >
        {children}
      </div>
    ),
    Cell: ({
      fill,
      children,
    }: {
      fill: string;
      children?: React.ReactNode;
    }) => (
      <div data-testid="pie-cell" data-fill={fill}>
        {children}
      </div>
    ),
    Tooltip: () => <div data-testid="tooltip" />,
    ResponsiveContainer: ({
      children,
    }: {
      children: React.ReactNode;
    }) => <div data-testid="responsive-container">{children}</div>,
    Legend: () => <div data-testid="legend" />,
  };
});

describe("SeverityChart", () => {
  const data: SeverityDistribution[] = [
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
  ];

  it("renders the validation severity title", () => {
    render(<SeverityChart data={data} />);

    expect(screen.getByText("Validation Severity")).toBeInTheDocument();
  });

  it("configures the pie chart with severity and count fields", () => {
    render(<SeverityChart data={data} />);

    const pie = screen.getByTestId("pie");

    expect(pie).toHaveAttribute("data-count-key", "count");
    expect(pie).toHaveAttribute("data-name-key", "severity");
    expect(pie).toHaveAttribute("data-count", "3");
  });

  it("maps CRITICAL, HIGH and MEDIUM to distinct colors", () => {
    render(<SeverityChart data={data} />);

    const cells = screen.getAllByTestId("pie-cell");

    expect(cells).toHaveLength(3);
    expect(cells[0]).toHaveAttribute("data-fill", "#D32F2F");
    expect(cells[1]).toHaveAttribute("data-fill", "#F57C00");
    expect(cells[2]).toHaveAttribute("data-fill", "#FBC02D");
  });

  it("uses the fallback color for an unknown severity", () => {
    const unknownData: SeverityDistribution[] = [
      {
        severity: "UNKNOWN",
        count: 1,
      },
    ];

    render(<SeverityChart data={unknownData} />);

    expect(screen.getByTestId("pie-cell")).toHaveAttribute(
      "data-fill",
      "#9E9E9E"
    );
  });

  it("renders correctly with empty severity data", () => {
    render(<SeverityChart data={[]} />);

    expect(screen.getByText("Validation Severity")).toBeInTheDocument();
    expect(screen.getByTestId("pie")).toHaveAttribute("data-count", "0");
  });
});
