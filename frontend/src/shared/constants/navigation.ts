import PublicIcon from "@mui/icons-material/Public";
import BusinessIcon from "@mui/icons-material/Business";
import AccountBalanceIcon from "@mui/icons-material/AccountBalance";

export interface NavigationItem {
    label: string;
    path: string;
    icon: React.ElementType;
    disabled?: boolean;
}

export interface NavigationGroup {
    title: string;
    items: NavigationItem[];
}

export const navigation: NavigationGroup[] = [
    {
        title: "Reference Data",
        items: [
            {
                label: "Countries",
                path: "/countries",
                icon: PublicIcon,
            },
            {
                label: "Organizations",
                path: "/organizations",
                icon: BusinessIcon,
                disabled: true,
            },
            {
                label: "Institutions",
                path: "/institutions",
                icon: AccountBalanceIcon,
                disabled: true,
            },
        ],
    },
];