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
  const [referenceYear, setReferenceYear] = useState(() => {
    const saved = localStorage.getItem("campaign_context");
    return saved ? JSON.parse(saved).referenceYear : 2026;
  });


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
