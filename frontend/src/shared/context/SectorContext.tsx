import {
    createContext,
    useContext,
    useMemo,
    useState,
    type ReactNode,
} from "react";

export type Sector =
    | "POWER"
    | "TRANSPORT"
    | "ICT"
    | "WATER_SANITATION";

export interface SectorOption {
    code: Sector;
    label: string;
    status: "OPERATIONAL" | "IN_DEVELOPMENT";
}

export const SECTOR_OPTIONS: SectorOption[] = [
    {
        code: "POWER",
        label: "Power",
        status: "OPERATIONAL",
    },
    {
        code: "TRANSPORT",
        label: "Transport",
        status: "IN_DEVELOPMENT",
    },
    {
        code: "ICT",
        label: "ICT",
        status: "IN_DEVELOPMENT",
    },
    {
        code: "WATER_SANITATION",
        label: "Water & Sanitation",
        status: "IN_DEVELOPMENT",
    },
];

interface SectorContextValue {
    currentSector: Sector;
    setCurrentSector: (sector: Sector) => void;
    currentSectorOption: SectorOption;
}

const SectorContext = createContext<SectorContextValue | undefined>(
    undefined,
);

interface SectorProviderProps {
    children: ReactNode;
}

export function SectorProvider({
    children,
}: SectorProviderProps) {
    const [currentSector, setCurrentSector] =
        useState<Sector>("POWER");

    const currentSectorOption = useMemo(
        () =>
            SECTOR_OPTIONS.find(
                (sector) => sector.code === currentSector,
            ) ?? SECTOR_OPTIONS[0],
        [currentSector],
    );

    const value = useMemo(
        () => ({
            currentSector,
            setCurrentSector,
            currentSectorOption,
        }),
        [currentSector, currentSectorOption],
    );

    return (
        <SectorContext.Provider value={value}>
            {children}
        </SectorContext.Provider>
    );
}

export function useSector() {
    const context = useContext(SectorContext);

    if (!context) {
        throw new Error(
            "useSector must be used within a SectorProvider.",
        );
    }

    return context;
}
