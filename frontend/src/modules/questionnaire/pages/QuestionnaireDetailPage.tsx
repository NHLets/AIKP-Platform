import {
    useEffect,
    useState,
} from "react";
import {
    useNavigate,
    useParams,
} from "react-router-dom";

import {
    Alert,
    Box,
    Button,
    Card,
    CardContent,
    CircularProgress,
    Divider,
    Dialog,
    DialogActions,
    DialogContent,
    DialogContentText,
    DialogTitle,
    Stack,
    Typography,
} from "@mui/material";

import {
    getQuestionnaire,
    submitQuestionnaireForReview,
    approveQuestionnaire,
    publishQuestionnaire,
    activateQuestionnaire,
    deactivateQuestionnaire,
    archiveQuestionnaire,
    deleteQuestionnaire,
} from "../api/questionnaireApi";
import { getQuestionnaireGroups } from "../api/questionnaireGroupApi";

import type {
    Questionnaire,
} from "../types/questionnaire.types";
import type {
    QuestionnaireGroup,
} from "../types/questionnaireGroup.types";

export default function QuestionnaireDetailPage() {
    const navigate = useNavigate();
    const { id } = useParams();

    const [
        questionnaire,
        setQuestionnaire,
    ] = useState<Questionnaire | null>(null);

    const [groups, setGroups] =
        useState<QuestionnaireGroup[]>([]);

    const [isGroupsLoading, setIsGroupsLoading] =
        useState(false);

    const [groupsError, setGroupsError] =
        useState<string | null>(null);

    const [isLoading, setIsLoading] =
        useState(true);

    const [error, setError] =
        useState<string | null>(null);

    const [
        isDeleteDialogOpen,
        setIsDeleteDialogOpen,
    ] = useState(false);

    const [isDeleting, setIsDeleting] =
        useState(false);

    const [
        isSubmittingForReview,
        setIsSubmittingForReview,
    ] = useState(false);

    const [
        isApproving,
        setIsApproving,
    ] = useState(false);

    const [
        isPublishing,
        setIsPublishing,
    ] = useState(false);

    const [
        isUpdatingActivation,
        setIsUpdatingActivation,
    ] = useState(false);

    const [
        isArchiving,
        setIsArchiving,
    ] = useState(false);

    useEffect(() => {
        async function loadQuestionnaire() {
            if (!id) {
                setError(
                    "Questionnaire identifier is missing.",
                );
                setIsLoading(false);
                return;
            }

            try {
                setIsLoading(true);
                setError(null);

                const data =
                    await getQuestionnaire(id);

                setQuestionnaire(data);
            } catch (error) {
                console.error(
                    "Failed to load questionnaire:",
                    error,
                );

                setError(
                    "Unable to load questionnaire.",
                );
            } finally {
                setIsLoading(false);
            }
        }

        void loadQuestionnaire();
    }, [id]);

    useEffect(() => {
        async function loadGroups() {
            if (!id) {
                return;
            }

            try {
                setIsGroupsLoading(true);
                setGroupsError(null);

                const data =
                    await getQuestionnaireGroups(id);

                const sortedGroups = [...data].sort(
                    (a, b) =>
                        a.displayOrder - b.displayOrder,
                );

                setGroups(sortedGroups);
            } catch (error) {
                console.error(
                    "Failed to load questionnaire groups:",
                    error,
                );

                setGroupsError(
                    "Unable to load questionnaire groups.",
                );
            } finally {
                setIsGroupsLoading(false);
            }
        }

        void loadGroups();
    }, [id]);

    async function handleDelete() {
        if (!id) {
            return;
        }

        try {
            setIsDeleting(true);
            setError(null);

            await deleteQuestionnaire(id);

            navigate("/questionnaires");
        } catch (error) {
            console.error(
                "Failed to delete questionnaire:",
                error,
            );

            setError(
                "Unable to delete questionnaire.",
            );

            setIsDeleteDialogOpen(false);
        } finally {
            setIsDeleting(false);
        }
    }

    async function handleSubmitForReview() {
        if (!id || !questionnaire) {
            return;
        }

        try {
            setIsSubmittingForReview(true);
            setError(null);

            const updatedQuestionnaire =
                await submitQuestionnaireForReview(
                    id,
                );

            setQuestionnaire(
                updatedQuestionnaire,
            );
        } catch (error) {
            console.error(
                "Failed to submit questionnaire for review:",
                error,
            );

            setError(
                "Unable to submit questionnaire for review.",
            );
        } finally {
            setIsSubmittingForReview(false);
        }
    }

    async function handleApprove() {
        if (!id || !questionnaire) {
            return;
        }

        try {
            setIsApproving(true);
            setError(null);

            const updatedQuestionnaire =
                await approveQuestionnaire(id);

            setQuestionnaire(
                updatedQuestionnaire,
            );
        } catch (error) {
            console.error(
                "Failed to approve questionnaire:",
                error,
            );

            setError(
                "Unable to approve questionnaire.",
            );
        } finally {
            setIsApproving(false);
        }
    }

    async function handlePublish() {
        if (!id || !questionnaire) {
            return;
        }

        try {
            setIsPublishing(true);
            setError(null);

            const updatedQuestionnaire =
                await publishQuestionnaire(id);

            setQuestionnaire(
                updatedQuestionnaire,
            );
        } catch (error) {
            console.error(
                "Failed to publish questionnaire:",
                error,
            );

            setError(
                "Unable to publish questionnaire.",
            );
        } finally {
            setIsPublishing(false);
        }
    }

    async function handleActivationChange() {
        if (!id || !questionnaire) {
            return;
        }

        try {
            setIsUpdatingActivation(true);
            setError(null);

            const updatedQuestionnaire =
                questionnaire.active
                    ? await deactivateQuestionnaire(id)
                    : await activateQuestionnaire(id);

            setQuestionnaire(updatedQuestionnaire);
        } catch (error) {
            console.error(
                "Failed to update questionnaire activation:",
                error,
            );

            setError(
                "Unable to update questionnaire activation.",
            );
        } finally {
            setIsUpdatingActivation(false);
        }
    }

    async function handleArchive() {
        if (!id || !questionnaire) {
            return;
        }

        try {
            setIsArchiving(true);
            setError(null);

            const updatedQuestionnaire =
                await archiveQuestionnaire(id);

            setQuestionnaire(updatedQuestionnaire);
        } catch (error) {
            console.error(
                "Failed to archive questionnaire:",
                error,
            );

            setError(
                "Unable to archive questionnaire.",
            );
        } finally {
            setIsArchiving(false);
        }
    }

    if (isLoading) {
        return (
            <Box
                sx={{
                    display: "flex",
                    justifyContent: "center",
                    py: 8,
                }}
            >
                <CircularProgress />
            </Box>
        );
    }

    if (error) {
        return (
            <Box>
                <Button
                    onClick={() =>
                        navigate("/questionnaires")
                    }
                    sx={{
                        mb: 3,
                    }}
                >
                    Back to Questionnaires
                </Button>

                <Alert severity="error">
                    {error}
                </Alert>
            </Box>
        );
    }

    if (!questionnaire) {
        return null;
    }

    return (
        <Box>
            <Button
                onClick={() =>
                    navigate("/questionnaires")
                }
                sx={{
                    mb: 3,
                }}
            >
                Back to Questionnaires
            </Button>

            <Typography
                variant="h4"
                sx={{
                    fontWeight: 700,
                    mb: 1,
                }}
            >
                {questionnaire.name}
            </Typography>

            <Stack
                direction={{
                    xs: "column",
                    sm: "row",
                }}
                spacing={2}
                sx={{
                    justifyContent: "space-between",
                    alignItems: {
                        xs: "stretch",
                        sm: "center",
                    },
                    mb: 4,
                }}
            >
                <Typography color="text.secondary">
                    Questionnaire details
                </Typography>

                <Stack
                    direction="row"
                    spacing={2}
                >
                    <Button
                        variant="outlined"
                        onClick={() =>
                            navigate(
                                `/questionnaires/${questionnaire.id}/edit`
                            )
                        }
                    >
                        Edit
                    </Button>

                    <Button
                        variant="outlined"
                        color="error"
                        onClick={() =>
                            setIsDeleteDialogOpen(true)
                        }
                    >
                        Delete
                    </Button>

                    {questionnaire.status === "DRAFT" && (
                        <Button
                            variant="contained"
                            onClick={
                                handleSubmitForReview
                            }
                            disabled={
                                isSubmittingForReview
                            }
                        >
                            {isSubmittingForReview
                                ? "Submitting..."
                                : "Submit for Review"}
                        </Button>
                    )}

                    {questionnaire.status === "UNDER_REVIEW" && (
                        <Button
                            variant="contained"
                            onClick={handleApprove}
                            disabled={isApproving}
                        >
                            {isApproving
                                ? "Approving..."
                                : "Approve"}
                        </Button>
                    )}

                    {questionnaire.status === "APPROVED" && (
                        <Button
                            variant="contained"
                            onClick={handlePublish}
                            disabled={isPublishing}
                        >
                            {isPublishing
                                ? "Publishing..."
                                : "Publish"}
                        </Button>
                    )}

                    {questionnaire.status === "PUBLISHED" && (
                        <>
                            <Button
                                variant="outlined"
                                onClick={
                                    handleActivationChange
                                }
                                disabled={
                                    isUpdatingActivation
                                }
                            >
                                {isUpdatingActivation
                                    ? "Updating..."
                                    : questionnaire.active
                                        ? "Deactivate"
                                        : "Activate"}
                            </Button>

                            <Button
                                variant="outlined"
                                color="error"
                                onClick={handleArchive}
                                disabled={isArchiving}
                            >
                                {isArchiving
                                    ? "Archiving..."
                                    : "Archive"}
                            </Button>
                        </>
                    )}
                </Stack>
            </Stack>

            {error && (
                <Alert
                    severity="error"
                    sx={{
                        mb: 3,
                    }}
                >
                    {error}
                </Alert>
            )}

            <Card>
                <CardContent>
                    <Stack spacing={3}>
                        <Box>
                            <Typography
                                variant="overline"
                                color="text.secondary"
                            >
                                Code
                            </Typography>

                            <Typography variant="body1">
                                {questionnaire.code}
                            </Typography>
                        </Box>

                        <Divider />

                        <Box>
                            <Typography
                                variant="overline"
                                color="text.secondary"
                            >
                                Description
                            </Typography>

                            <Typography variant="body1">
                                {
                                    questionnaire.description
                                }
                            </Typography>
                        </Box>

                        <Divider />

                        <Stack
                            direction={{
                                xs: "column",
                                sm: "row",
                            }}
                            spacing={6}
                        >
                            <Box>
                                <Typography
                                    variant="overline"
                                    color="text.secondary"
                                >
                                    Version
                                </Typography>

                                <Typography variant="body1">
                                    {
                                        questionnaire.version
                                    }
                                </Typography>
                            </Box>

                            <Box>
                                <Typography
                                    variant="overline"
                                    color="text.secondary"
                                >
                                    Default Language
                                </Typography>

                                <Typography variant="body1">
                                    {
                                        questionnaire.defaultLanguage
                                    }
                                </Typography>
                            </Box>

                            <Box>
                                <Typography
                                    variant="overline"
                                    color="text.secondary"
                                >
                                    Render Type
                                </Typography>

                                <Typography variant="body1">
                                    {
                                        questionnaire.renderType
                                    }
                                </Typography>
                            </Box>
                        </Stack>

                        <Divider />

                        <Stack
                            direction={{
                                xs: "column",
                                sm: "row",
                            }}
                            spacing={6}
                        >
                            <Box>
                                <Typography
                                    variant="overline"
                                    color="text.secondary"
                                >
                                    Status
                                </Typography>

                                <Typography variant="body1">
                                    {
                                        questionnaire.status
                                    }
                                </Typography>
                            </Box>

                            <Box>
                                <Typography
                                    variant="overline"
                                    color="text.secondary"
                                >
                                    Active
                                </Typography>

                                <Typography variant="body1">
                                    {
                                        questionnaire.active
                                            ? "Yes"
                                            : "No"
                                    }
                                </Typography>
                            </Box>
                        </Stack>
                    </Stack>
                </CardContent>
            </Card>

            <Card sx={{ mt: 3 }}>
                <CardContent>
                    <Typography
                        variant="h6"
                        sx={{ fontWeight: 600, mb: 2 }}
                    >
                        Groups
                    </Typography>

                    {isGroupsLoading && (
                        <Box
                            sx={{
                                display: "flex",
                                justifyContent: "center",
                                py: 3,
                            }}
                        >
                            <CircularProgress size={28} />
                        </Box>
                    )}

                    {!isGroupsLoading && groupsError && (
                        <Alert severity="error">
                            {groupsError}
                        </Alert>
                    )}

                    {!isGroupsLoading &&
                        !groupsError &&
                        groups.length === 0 && (
                            <Typography color="text.secondary">
                                No groups defined for this questionnaire.
                            </Typography>
                        )}

                    {!isGroupsLoading &&
                        !groupsError &&
                        groups.length > 0 && (
                            <Stack spacing={2}>
                                {groups.map((group) => (
                                    <Box key={group.id}>
                                        <Stack
                                            direction={{
                                                xs: "column",
                                                sm: "row",
                                            }}
                                            spacing={{
                                                xs: 0.5,
                                                sm: 3,
                                            }}
                                        >
                                            <Box sx={{ minWidth: 220 }}>
                                                <Typography
                                                    variant="subtitle1"
                                                    sx={{ fontWeight: 600 }}
                                                >
                                                    {group.name}
                                                </Typography>

                                                <Typography
                                                    variant="body2"
                                                    color="text.secondary"
                                                >
                                                    {group.code}
                                                </Typography>
                                            </Box>

                                            <Box>
                                                <Typography
                                                    variant="body2"
                                                    sx={{ fontWeight: 500 }}
                                                >
                                                    {group.groupType}
                                                </Typography>

                                                {group.description && (
                                                    <Typography
                                                        variant="body2"
                                                        color="text.secondary"
                                                    >
                                                        {group.description}
                                                    </Typography>
                                                )}
                                            </Box>
                                        </Stack>

                                        <Divider sx={{ mt: 2 }} />
                                    </Box>
                                ))}
                            </Stack>
                        )}
                </CardContent>
            </Card>

            <Dialog
                open={isDeleteDialogOpen}
                onClose={() => {
                    if (!isDeleting) {
                        setIsDeleteDialogOpen(false);
                    }
                }}
            >
                <DialogTitle>
                    Delete Questionnaire
                </DialogTitle>

                <DialogContent>
                    <DialogContentText>
                        Are you sure you want to delete
                        "{questionnaire.name}"?
                        This action cannot be undone.
                    </DialogContentText>
                </DialogContent>

                <DialogActions>
                    <Button
                        onClick={() =>
                            setIsDeleteDialogOpen(false)
                        }
                        disabled={isDeleting}
                    >
                        Cancel
                    </Button>

                    <Button
                        color="error"
                        variant="contained"
                        onClick={handleDelete}
                        disabled={isDeleting}
                    >
                        {isDeleting
                            ? "Deleting..."
                            : "Delete"}
                    </Button>
                </DialogActions>
            </Dialog>

        </Box>
    );
}
