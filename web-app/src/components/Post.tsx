import React, { useState } from "react";
import {
  Box,
  Avatar,
  Typography,
  IconButton,
  Snackbar,
  Alert,
} from "@mui/material";
import DeleteIcon from "@mui/icons-material/Delete";
import { deletePost } from "../services/PostService"; // Adjust import path as needed

export const Post: React.FC<{
  post: {
    id: string;
    avatarUrl: string;
    username: string;
    formattedPostedDate: string;
    content: string;
    images?: string[];
  };
  onDelete: (id: string) => Promise<void>; 
}> = (props) => {
  const {
    id,
    avatarUrl,
    username,
    formattedPostedDate,
    content,
    images = [],
  } = props.post;
  const onDelete = props.onDelete;

  const [snackbarOpen, setSnackbarOpen] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState("");
  const [snackbarSeverity, setSnackbarSeverity] = useState<"success" | "error">(
    "success"
  );

  let formattedContent;
  try {
    formattedContent = JSON.stringify(JSON.parse(content), null, 2);
  } catch (e) {
    formattedContent = content;
  }

  const getImageSrc = (image: string) => {
    const mimeType = image.startsWith("/9j/") ? "jpeg" : "png";
    return `data:image/${mimeType};base64,${image}`;
  };

  const handleDelete = () => {
    onDelete(id);
  };

  const getGridTemplateColumns = () => {
    switch (images.length) {
      case 1:
        return "1fr";
      case 2:
        return "repeat(2, 1fr)";
      case 3:
        return "repeat(3, 1fr)";
      case 4:
        return "repeat(2, 1fr)";
      default:
        return "repeat(3, 1fr)";
    }
  };

  // Close Snackbar
  const handleCloseSnackbar = () => {
    setSnackbarOpen(false);
  };

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
        position: "relative", // Allow positioning of delete button
        "&:hover .delete-button": {
          opacity: 1, // Show button on hover
        },
      }}
    >
      {/* Delete Button on the Top Right Corner */}
      <IconButton
        className="delete-button"
        onClick={handleDelete}
        sx={{
          position: "absolute",
          top: 8,
          right: 8,
          color: "#fff",
          backgroundColor: "rgba(0, 0, 0, 0.5)",
          "&:hover": {
            backgroundColor: "rgba(0, 0, 0, 0.7)",
          },
          opacity: 0, // Initially hidden
          transition: "opacity 0.3s",
        }}
      >
        <DeleteIcon />
      </IconButton>

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

        {/* Display Images in a Grid */}
        {images.length > 0 && (
          <Box
            sx={{
              display: "grid",
              gridTemplateColumns: getGridTemplateColumns(),
              gap: 1,
              marginTop: 2,
            }}
          >
            {images.map((image, index) => (
              <Box
                key={index}
                component="img"
                src={getImageSrc(image)}
                alt={`Post Image ${index + 1}`}
                sx={{
                  width: "100%",
                  height: "auto",
                  objectFit: "cover",
                  borderRadius: "8px",
                  gridColumn:
                    images.length === 3 && index === 2 ? "span 2" : undefined,
                }}
              />
            ))}
          </Box>
        )}
      </Box>

      <Snackbar
        open={snackbarOpen}
        autoHideDuration={3000}
        onClose={handleCloseSnackbar}
        anchorOrigin={{ vertical: "top", horizontal: "right" }}
      >
        <Alert
          onClose={handleCloseSnackbar}
          severity={snackbarSeverity}
          variant="filled"
        >
          {snackbarMessage}
        </Alert>
      </Snackbar>
    </Box>
  );
};
