// @vitest-environment jsdom

import { fireEvent, render, screen, waitFor, within } from "@testing-library/react";
import { beforeEach, describe, expect, it, vi } from "vitest";

import DataCollectionListPage from "./DataCollectionListPage";

import {
    deleteDataCollection,
    getDataCollections,
} from "../api/dataCollectionApi";

import {
    getCampaigns,
    getCountries,
    getOrganizations,
    getQuestionnaires,
} from "../api/dataCollectionOptionsApi";

const navigateMock = vi.fn();

vi.mock("react-router-dom", () => ({
    useNavigate: () => navigateMock,
}));

vi.mock("../api/dataCollectionApi", () => ({
    getDataCollections: vi.fn(),
    deleteDataCollection: vi.fn(),
}));

vi.mock("../api/dataCollectionOptionsApi", () => ({
    getCampaigns: vi.fn(),
    getCountries: vi.fn(),
    getOrganizations: vi.fn(),
    getQuestionnaires: vi.fn(),
}));

const mockedGetDataCollections = vi.mocked(getDataCollections);
const mockedDeleteDataCollection = vi.mocked(deleteDataCollection);

function makeDataCollection(
    id: string,
    status:
        | "DRAFT"
        | "IN_PROGRESS"
        | "SUBMITTED"
        | "VALIDATED"
        | "REJECTED"
        | "CANCELLED",
) {
    return {
        id,
        campaignId: `campaign-${id}`,
        countryId: `country-${id}`,
        questionnaireId: `questionnaire-${id}`,
        responsibleOrganizationId: `responsible-${id}`,
        operatorOrganizationId: `operator-${id}`,
        dataCollectorId: `collector-${id}`,
        status,
    } as Awaited<ReturnType<typeof getDataCollections>>[number];
}

function mockLoadedData(
    collections = [
        makeDataCollection("draft-1", "DRAFT"),
        makeDataCollection("progress-1", "IN_PROGRESS"),
        makeDataCollection("submitted-1", "SUBMITTED"),
        makeDataCollection("validated-1", "VALIDATED"),
        makeDataCollection("rejected-1", "REJECTED"),
        makeDataCollection("cancelled-1", "CANCELLED"),
    ],
) {
    mockedGetDataCollections.mockResolvedValue(collections);

    vi.mocked(getCampaigns).mockResolvedValue([]);
    vi.mocked(getCountries).mockResolvedValue([]);
    vi.mocked(getQuestionnaires).mockResolvedValue([]);
    vi.mocked(getOrganizations).mockResolvedValue([]);
}

describe("DataCollectionListPage deletion", () => {
    beforeEach(() => {
        vi.clearAllMocks();
        mockLoadedData();
        mockedDeleteDataCollection.mockResolvedValue(undefined);
    });

    it("shows Delete only for DRAFT and IN_PROGRESS", async () => {
        render(<DataCollectionListPage />);

        await waitFor(() => {
            expect(
                screen.getAllByRole("button", {
                    name: "Delete data collection",
                }),
            ).toHaveLength(2);
        });
    });

    it("opens the delete confirmation dialog", async () => {
        render(<DataCollectionListPage />);

        const deleteButtons = await screen.findAllByRole("button", {
            name: "Delete data collection",
        });

        fireEvent.click(deleteButtons[0]);

        expect(
            screen.getByRole("heading", {
                name: "Delete Data Collection",
            }),
        ).toBeInTheDocument();

        expect(
            screen.getByText(
                "Are you sure you want to delete this Data Collection? This action cannot be undone.",
            ),
        ).toBeInTheDocument();
    });

    it("deletes the selected data collection after confirmation", async () => {
        render(<DataCollectionListPage />);

        const deleteButtons = await screen.findAllByRole("button", {
            name: "Delete data collection",
        });

        fireEvent.click(deleteButtons[0]);

        const dialog = screen.getByRole("dialog");

        const dialogDeleteButton = within(dialog).getByRole("button", {
            name: "Delete",
        });

        fireEvent.click(dialogDeleteButton);

        await waitFor(() => {
            expect(mockedDeleteDataCollection).toHaveBeenCalledWith(
                "draft-1",
            );
        });

        await waitFor(() => {
            expect(
                screen.queryByText("draft-1"),
            ).not.toBeInTheDocument();
        });
    });

    it("keeps the data collection and displays an error when deletion fails", async () => {
        mockedDeleteDataCollection.mockRejectedValueOnce(
            new Error("Deletion failed"),
        );

        render(<DataCollectionListPage />);

        const deleteButtons = await screen.findAllByRole("button", {
            name: "Delete data collection",
        });

        fireEvent.click(deleteButtons[0]);

        const dialog = screen.getByRole("dialog");

        const dialogDeleteButton = within(dialog).getByRole("button", {
            name: "Delete",
        });

        fireEvent.click(dialogDeleteButton);

        expect(
            await screen.findByText(
                "Unable to delete this data collection. Please try again.",
            ),
        ).toBeInTheDocument();

        expect(mockedDeleteDataCollection).toHaveBeenCalledWith(
            "draft-1",
        );
    });
});
