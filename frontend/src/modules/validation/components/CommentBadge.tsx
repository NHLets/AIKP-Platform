import { Badge } from "@mui/material";

interface Props {
  count: number;
}

export default function CommentBadge({ count }: Props) {
  if (count <= 0) return null;

  return (
    <Badge
      badgeContent={count}
      color="primary"
      sx={{
        position: "absolute",
        top: 4,
        right: 4,
      }}
    />
  );
}
