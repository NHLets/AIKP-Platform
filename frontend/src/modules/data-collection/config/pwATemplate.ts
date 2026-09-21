export const PW_A_REFERENCE_YEARS = [
    2025,
    2024,
    2023,
    2022,
    2021,
    2020,
    2019,
    2018,
    2017,
    2016,
    2015,
] as const;

export const PW_A_BOOLEAN_VARIABLES = new Set([
    "D001",
    "D002",
    "D003",
    "D004",
    "D005",
    "D006",
    "D011",
    "D029",
    "D030",
    "D036",
    "D037",
    "D038",
    "D039",
    "D044",
]);

export interface PwAChoiceOption {
    value: string;
    label: string;
}

export const PW_A_CHOICE_OPTIONS: Record<
    string,
    PwAChoiceOption[]
> = {
    D007: [
        {
            value: "0",
            label: "Central",
        },
        {
            value: "1",
            label: "Regional",
        },
        {
            value: "2",
            label: "Local/Municipal",
        },
    ],

    D012: [
        {
            value: "0",
            label: "Same company",
        },
        {
            value: "1",
            label: "Single buyer model",
        },
        {
            value: "2",
            label: "Wholesale competition",
        },
        {
            value: "3",
            label: "Retail competition",
        },
    ],

    D031: [
        {
            value: "0",
            label: "None",
        },
        {
            value: "1",
            label: "Price cap",
        },
        {
            value: "2",
            label: "Rate of return",
        },
        {
            value: "3",
            label: "Other",
        },
    ],

    D040: [
        {
            value: "0",
            label: "Full subsidy",
        },
        {
            value: "1",
            label: "Full capital subsidy",
        },
        {
            value: "2",
            label: "Partial capital subsidy",
        },
        {
            value: "3",
            label: "No subsidy",
        },
    ],
};

export function isPwATemplate(code: string): boolean {
    return code.toUpperCase() === "PW_A";
}
