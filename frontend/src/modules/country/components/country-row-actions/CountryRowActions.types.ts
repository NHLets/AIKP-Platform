export interface CountryRowActionsProps {

    active: boolean;

    onEdit: () => void;

    onActivate?: () => void;

    onDeactivate?: () => void;

    onDelete?: () => void;

}