import { render, screen } from "@testing-library/react";
import { describe, expect, it } from "vitest";

import Heatmap from "./Heatmap";
import type { ValidationHeatmap } from "../../types/dashboard";

describe("Heatmap", () => {
  const data: ValidationHeatmap[] = [
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
  ];

  it("renders the validation heatmap title", () => {
    render(<Heatmap data={data} />);

    expect(screen.getByText("Validation Heatmap")).toBeInTheDocument();
  });

  it("renders the expected table headers", () => {
    render(<Heatmap data={data} />);

    expect(screen.getByText("Questionnaire")).toBeInTheDocument();
    expect(screen.getByText("Variable")).toBeInTheDocument();
    expect(screen.getByText("Comments")).toBeInTheDocument();
  });

  it("renders questionnaire, variable and comment counts", () => {
    render(<Heatmap data={data} />);

    expect(screen.getAllByText("PW_B")).toHaveLength(2);
    expect(screen.getByText("b001")).toBeInTheDocument();
    expect(screen.getByText("b002")).toBeInTheDocument();
    expect(screen.getByText("2")).toBeInTheDocument();
    expect(screen.getByText("1")).toBeInTheDocument();
  });

  it("applies the heatmap intensity based on the maximum count", () => {
    render(<Heatmap data={data} />);

    const countTwo = screen.getByText("2");
    const countOne = screen.getByText("1");

    expect(countTwo).toHaveStyle({
      backgroundColor: "rgba(25,118,210,1.00)",
    });

    expect(countOne).toHaveStyle({
      backgroundColor: "rgba(25,118,210,0.50)",
    });
  });

  it("renders the empty state when there are no validation comments", () => {
    render(<Heatmap data={[]} />);

    expect(
      screen.getByText("No validation comments found.")
    ).toBeInTheDocument();
  });
});
