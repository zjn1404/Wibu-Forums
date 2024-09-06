import React, { useState } from "react";
import {
  Box,
  Avatar,
  Typography,
  IconButton,
  Button,
  TextField,
} from "@mui/material";
import DeleteIcon from "@mui/icons-material/Delete";
import EditIcon from "@mui/icons-material/Edit";

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
  onUpdate: (id: string, content: string) => Promise<void>;
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
  const onUpdate = props.onUpdate;

  const [isEditing, setIsEditing] = useState(false);
  const [editedContent, setEditedContent] = useState(content);

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

  const handleEdit = () => {
    setIsEditing(true);
  };

  const handleUpdate = async () => {
    await onUpdate(id, editedContent);
    setIsEditing(false);
  };

  const handleContentChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setEditedContent(event.target.value);
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
        position: "relative",
        "&:hover .action-buttons": {
          opacity: 1,
        },
      }}
    >
      <Box
        className="action-buttons"
        sx={{
          position: "absolute",
          top: 8,
          right: 8,
          display: "flex",
          gap: 1,
          opacity: 0,
          transition: "opacity 0.3s",
        }}
      >
        <IconButton
          onClick={handleEdit}
          sx={{
            color: "#fff",
            backgroundColor: "rgba(0, 0, 0, 0.5)",
            "&:hover": {
              backgroundColor: "rgba(0, 0, 0, 0.7)",
            },
          }}
        >
          <EditIcon />
        </IconButton>
        <IconButton
          onClick={handleDelete}
          sx={{
            color: "#fff",
            backgroundColor: "rgba(0, 0, 0, 0.5)",
            "&:hover": {
              backgroundColor: "rgba(0, 0, 0, 0.7)",
            },
          }}
        >
          <DeleteIcon />
        </IconButton>
      </Box>

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

        {isEditing ? (
          <>
            <TextField
              value={editedContent}
              onChange={handleContentChange}
              multiline
              rows={4}
              sx={{ marginBottom: 2, width: "100%" }}
            />
            <Button
              variant="contained"
              className="btn btn-dark"
              onClick={handleUpdate}
              sx={{
                alignSelf: "flex-end",
                backgroundColor: "#1b1e21",
                color: "#ffffff",
                "&:hover": {
                  backgroundColor: "#343a40",
                },
              }}
            >
              Update
            </Button>
          </>
        ) : (
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
        )}

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
    </Box>
  );
};
