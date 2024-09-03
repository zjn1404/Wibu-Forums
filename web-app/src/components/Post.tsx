import { Box, Avatar, Typography } from "@mui/material";
import React from "react";

export const Post: React.FC<{
  post: {
    avatarUrl: string;
    username: string;
    formattedPostedDate: string;
    postedDate: string;
    content: string;
  };
}> = (props) => {
  const { avatarUrl, username, postedDate, formattedPostedDate, content } = props.post;

  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: "row",
        alignItems: "flex-start",
        justifyContent: "flex-start",
        width: "100%", // Full width for container
        padding: 2,
        marginBottom: 2,
        border: "1px solid #e0e0e0",
        borderRadius: "8px",
        boxShadow: 1,
        backgroundColor: "#ffffff",
        overflow: "visible", // Allows content to be fully visible
        boxSizing: "border-box", // Ensures padding doesn't affect width
      }}
    >
      <Avatar
        src={avatarUrl ? avatarUrl : `${process.env.PUBLIC_URL}/logo/logo.jpeg`}
        sx={{ marginRight: 2, width: 56, height: 56 }}
      />
      <Box
        sx={{
          flex: 1,
          display: "flex",
          flexDirection: "column",
          justifyContent: "space-between",
          overflow: "visible", // Allows text content to be fully displayed
          boxSizing: "border-box",
        }}
      >
        <Box
          sx={{
            marginBottom: 1,
            width: "100%",
          }}
        >
          <Typography
            sx={{
              fontSize: 16,
              fontWeight: 600,
              marginBottom: "4px",
              whiteSpace: "nowrap",
              overflow: "hidden",
              textOverflow: "ellipsis",
            }}
          >
            {username}
          </Typography>
          <Typography
            sx={{
              fontSize: 13,
              color: "gray",
              whiteSpace: "nowrap",
              overflow: "hidden",
              textOverflow: "ellipsis",
            }}
          >
            {formattedPostedDate}
          </Typography>
        </Box>
        <Typography
          sx={{
            fontSize: 14,
            lineHeight: "1.5",
            color: "#333",
            overflow: "visible",
            whiteSpace: "pre-wrap",
          }}
        >
          {content}
        </Typography>
      </Box>
    </Box>
  );
};
