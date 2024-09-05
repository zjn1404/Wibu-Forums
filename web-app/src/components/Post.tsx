import { Box, Avatar, Typography } from "@mui/material";
import React from "react";

export const Post: React.FC<{
  post: {
    avatarUrl: string;
    username: string;
    formattedPostedDate: string;
    postedDate: string;
    content: string;
    image?: string; // base64 encoded image (optional)
  };
}> = (props) => {
  const { avatarUrl, username, formattedPostedDate, content, image } =
    props.post;

  let formattedContent;
  try {
    formattedContent = JSON.stringify(JSON.parse(content), null, 2);
  } catch (e) {
    formattedContent = content;
  }

  // Check if the image is defined and determine its type
  const imageSrc = image
    ? `data:image/${image.startsWith("/9j/") ? "jpeg" : "png"};base64,${image}`
    : "";

  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: "row",
        alignItems: "flex-start",
        justifyContent: "flex-start",
        width: "100%",
        padding: 2,
        marginBottom: 2,
        border: "1px solid #e0e0e0",
        borderRadius: "8px",
        boxShadow: 1,
        backgroundColor: "#ffffff",
        overflow: "visible",
        boxSizing: "border-box",
      }}
    >
      <Avatar
        src={avatarUrl || `${process.env.PUBLIC_URL}/logo/logo.jpeg`}
        sx={{ marginRight: 2, width: 56, height: 56 }}
      />
      <Box
        sx={{
          flex: 1,
          display: "flex",
          flexDirection: "column",
          justifyContent: "space-between",
          overflow: "visible",
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
            fontSize: 15,
            lineHeight: "1.5",
            color: "#333",
            whiteSpace: "pre-wrap",
            overflow: "auto",
            maxHeight: "200px",
            padding: "4px",
            borderRadius: "4px",
          }}
        >
          {formattedContent}
        </Typography>

        {/* Image below content */}
        {image && (
          <Box
            sx={{
              display: "flex",
              justifyContent: "center",
              alignItems: "center",
              width: "100%",
              height: "auto", // Adjust height if needed
              marginTop: 2,
            }}
          >
            <Box
              component="img"
              src={imageSrc}
              alt="Post Image"
              sx={{
                maxWidth: "100%", 
                maxHeight: "300px", 
                objectFit: "contain",
                borderRadius: "8px",
              }}
            />
          </Box>
        )}
      </Box>
    </Box>
  );
};
