import {
createContext,
  useContext,
  useState,
  useEffect,
} from "react";
import type { ReactNode } from "react";

interface CampaignContextType {
  campaignId: string;
  referenceYear: number;
  setCampaignId: (id: string) => void;
  setReferenceYear: (year: number) => void;
}

const MIN_REFERENCE_YEAR = 2015;
const MAX_REFERENCE_YEAR = 2025;

function isValidReferenceYear(value: unknown): value is number {
  return (
    typeof value === "number" &&
    Number.isInteger(value) &&
    value >= MIN_REFERENCE_YEAR &&
    value <= MAX_REFERENCE_YEAR
  );
}

const CampaignContext = createContext<CampaignContextType | undefined>(undefined);

export function CampaignProvider({ children }: { children: ReactNode }) {

  const [campaignId, setCampaignId] = useState<string>(() => {
    const saved = localStorage.getItem("campaign_context");

    if (!saved) {
      return "";
    }

    try {
      const parsed = JSON.parse(saved);
      return typeof parsed.campaignId === "string"
        ? parsed.campaignId
        : "";
    } catch {
      return "";
    }
  });
  const [referenceYear, setReferenceYearState] = useState<number>(() => {
    const saved = localStorage.getItem("campaign_context");

    if (saved) {
      try {
        const parsed: unknown = JSON.parse(saved);

        if (
          typeof parsed === "object" &&
          parsed !== null &&
          "referenceYear" in parsed &&
          isValidReferenceYear(parsed.referenceYear)
        ) {
          return parsed.referenceYear;
        }
      } catch {
        // Use the default year if saved data is invalid.
      }
    }

    return MAX_REFERENCE_YEAR;
  });

  const setReferenceYear = (year: number) => {
    if (isValidReferenceYear(year)) {
      setReferenceYearState(year);
    }
  };


  useEffect(() => {
    localStorage.setItem(
      "campaign_context",
      JSON.stringify({
        campaignId,
        referenceYear,
      })
    );
  }, [campaignId, referenceYear]);

  return (
    <CampaignContext.Provider
      value={{
        campaignId,
        referenceYear,
        setCampaignId,
        setReferenceYear,
      }}
    >
      {children}
    </CampaignContext.Provider>
  );
}

export function useCampaign() {

  const context = useContext(CampaignContext);

  if (!context) {
    throw new Error("useCampaign must be used inside CampaignProvider");
  }

  return context;
}
