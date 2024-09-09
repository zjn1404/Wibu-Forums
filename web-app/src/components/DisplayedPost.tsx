import React, { useEffect, useState } from "react";
import { Link as RouterLink } from "react-router-dom";
import {
  Box,
  Avatar,
  Typography,
  IconButton,
  Button,
  TextField,
  Link,
} from "@mui/material";
import DeleteIcon from "@mui/icons-material/Delete";
import EditIcon from "@mui/icons-material/Edit";
import { ROLE } from "../configurations/Configuration";
import { getAccessToken } from "../services/LocalStorageService";
import { parseJwt } from "../services/AuthenticationConfig/auth";
import { Comment } from "../entity/Comment";

export const DisplayedPost: React.FC<{
  post: {
    id: string;
    avatarUrl: string;
    userId: string;
    formattedPostedDate: string;
    content: string;
    images?: string[];
    comments: Comment[];
  };
  onDelete: (id: string) => Promise<void>;
  onUpdate: (id: string, content: string) => Promise<void>;
  onAddComment: (postId: string, content: string) => Promise<void>;
  onUpdateComment: (
    postId: string,
    commentId: string,
    content: string
  ) => Promise<void>;
  onDeleteComment: (postId: string, commentId: string) => Promise<void>;
  onLoadMoreComments: (postId: string) => Promise<void>;
}> = (props) => {
  const {
    id,
    avatarUrl,
    userId,
    formattedPostedDate,
    content,
    images = [],
    comments = [],
  } = props.post;
  const onDelete = props.onDelete;
  const onUpdate = props.onUpdate;
  const onAddComment = props.onAddComment;
  const onUpdateComment = props.onUpdateComment;
  const onDeleteComment = props.onDeleteComment;
  const onLoadMoreComments = props.onLoadMoreComments;

  const [isEditing, setIsEditing] = useState(false);
  const [editedContent, setEditedContent] = useState(content);
  const [newComment, setNewComment] = useState("");
  const [isEditingComment, setIsEditingComment] = useState<string | null>(null);
  const [userRole, setUserRole] = useState<string>("ROLE_USER");
  const [jwtUserId, setJwtUserId] = useState<string>("");
  const [editedComment, setEditedComment] = useState<string>("");

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

  const handleAddComment = async () => {
    if (newComment.trim()) {
      await onAddComment(id, newComment);
      setNewComment("");
    }
  };

  const handleCommentChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setNewComment(event.target.value);
  };

  const handleEditComment = (id: string) => {
    setIsEditingComment(id);
    const commentToEdit = comments.find((comment) => comment.id === id);
    if (commentToEdit) {
      setEditedComment(commentToEdit.content);
    }
  };

  const handleUpdateComment = async (postId: string, id: string) => {
    await onUpdateComment(postId, id, editedComment);
    setIsEditingComment(null);
  };

  const handleDeleteComment = async (postId: string, id: string) => {
    await onDeleteComment(postId, id);
  };

  const handleLoadMoreComments = async (postId: string) => {
    await onLoadMoreComments(postId);
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

  useEffect(() => {
    const accessToken = getAccessToken();

    if (!accessToken) {
      return () => {};
    }

    const parsedToken = parseJwt(accessToken);

    if (!parsedToken || !parsedToken.scope || !parsedToken.jti) {
      return () => {};
    }

    const roles = parsedToken.scope
      .split(",")
      .map((role: string) => role.trim())
      .filter((role: string) => role.startsWith("ROLE_"));

    const userId = parsedToken.sub;

    setUserRole(roles[0]);
    setJwtUserId(userId);
    console.log(`roles: ${userRole}, userId: ${jwtUserId}`);
    return () => {};
  });

  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: "column",
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
      {(userRole === ROLE.ADMIN || userId === jwtUserId) && (
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
      )}
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
          <Link
            component={RouterLink}
            to={userId === jwtUserId ? 'profile' : `/user/${userId}`}
            variant="body2"
            sx={{
              fontSize: 16,
              fontWeight: 600,
              whiteSpace: "nowrap",
              overflow: "hidden",
              textOverflow: "ellipsis",
              cursor: "pointer",
              textDecoration: "none",
              color: "#1b1e21",
              display: "flex",
              alignItems: "center",
            }}
          >
            <Avatar
              src={avatarUrl || ""}
              sx={{ marginRight: 2, width: 56, height: 56 }}
            />
            {userId}
          </Link>
        </Box>

        <div>
        {isEditing ? (
          <>
            <TextField
              value={editedContent}
              onChange={handleContentChange}
              multiline
              rows={4}
              sx={{ marginBottom: 2, width: "100%" }}
            />
            <Box
              sx={{
                display: "flex",
                justifyContent: "flex-end",
                gap: 1,
              }}
            >
              <Button
                variant="contained"
                onClick={handleUpdate}
                sx={{
                  backgroundColor: "#1b1e21",
                  color: "#ffffff",
                  "&:hover": {
                    backgroundColor: "#343a40",
                  },
                }}
              >
                Update
              </Button>
              <Button
                variant="outlined"
                onClick={() => setIsEditing(false)}
                sx={{
                  borderColor: "#1b1e21",
                  color: "#1b1e21",
                  "&:hover": {
                    backgroundColor: "#f8f9fa",
                    borderColor: "#343a40",
                  },
                }}
              >
                Cancel
              </Button>
            </Box>
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
        </div>

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
      {/* Display Comments */}
      <Box sx={{ marginTop: 2, width: "100%" }}>
        <Typography variant="subtitle2">Comments:</Typography>

        {comments.map((comment) => (
          <Box
            key={comment.id}
            sx={{
              position: "relative",
              padding: "5px",
              borderBottom: "1px solid #eee",
              "&:hover .comment-action-buttons": {
                opacity: 1, // Show buttons on hover
              },
            }}
          >
            {/* Action Buttons for Edit and Delete */}

            <Box
              className="comment-action-buttons"
              sx={{
                position: "absolute",
                top: 8,
                right: 8,
                display: "flex",
                gap: 1,
                opacity: 0, // Initially hidden
                transition: "opacity 0.3s", // Smooth transition
              }}
            >
              {(userRole === "ROLE_ADMIN" || comment.userId === jwtUserId) && (
                <IconButton
                  onClick={() => handleEditComment(comment.id)}
                  sx={{
                    color: "#fff",
                    backgroundColor: "rgba(0, 0, 0, 0.5)",
                    "&:hover": {
                      backgroundColor: "rgba(0, 0, 0, 0.7)",
                    },
                  }}
                >
                  <EditIcon fontSize="small" />
                </IconButton>
              )}
              {(userRole === "ROLE_ADMIN" ||
                comment.userId === jwtUserId ||
                userId === jwtUserId) && (
                <IconButton
                  onClick={() => handleDeleteComment(id, comment.id)}
                  sx={{
                    color: "#fff",
                    backgroundColor: "rgba(0, 0, 0, 0.5)",
                    "&:hover": {
                      backgroundColor: "rgba(0, 0, 0, 0.7)",
                    },
                  }}
                >
                  <DeleteIcon fontSize="small" />
                </IconButton>
              )}
            </Box>
            {/* Display Comment Details */}
            {isEditingComment === comment.id ? (
              // If editing this comment
              <Box sx={{ display: "flex", flexDirection: "column", gap: 1 }}>
                <TextField
                  value={editedComment}
                  onChange={(e) => setEditedComment(e.target.value)}
                  multiline
                  onKeyDown={(event) => {
                    if (event.key === "Enter" && !event.shiftKey) {
                      event.preventDefault();
                      handleUpdateComment(id, comment.id);
                    }
                  }}
                  rows={2}
                  sx={{ marginBottom: 1 }}
                />
                <Box
                  sx={{ display: "flex", justifyContent: "flex-end", gap: 1 }}
                >
                  <Button
                    onClick={() => handleUpdateComment(id, comment.id)}
                    variant="contained"
                    sx={{
                      backgroundColor: "#1b1e21",
                      color: "#ffffff",
                      "&:hover": {
                        backgroundColor: "#343a40",
                      },
                    }}
                  >
                    Update
                  </Button>
                  <Button
                    onClick={() => setIsEditingComment(null)}
                    variant="outlined"
                    sx={{
                      borderColor: "#1b1e21",
                      color: "#1b1e21",
                      "&:hover": {
                        backgroundColor: "#f8f9fa",
                        borderColor: "#343a40",
                      },
                    }}
                  >
                    Cancel
                  </Button>
                </Box>
              </Box>
            ) : (
              // If not editing, display the comment normally
              <>
                <Link
                  component={RouterLink}
                  to={comment.userId === jwtUserId ? '/profile' : `/user/${comment.userId}`}
                  variant="body2"
                  sx={{
                    fontWeight: "bold",
                    textDecoration: "none",
                    color: "#1b1e21",
                    cursor: "pointer",
                    display: "flex",
                    alignItems: "center",
                    marginBottom: "10px",
                  }}
                >
                  <Avatar
                    src={avatarUrl || ""}
                    sx={{ marginRight: 2, width: 30, height: 30 }}
                  />
                  {comment.userId}
                </Link>
                <Typography variant="body2" sx={{ whiteSpace: "pre-line" }}>
                  {comment.content}
                </Typography>
                <Typography
                  variant="caption"
                  color="textSecondary"
                  sx={{ display: "block" }}
                >
                  {comment.formattedPostedDate}
                </Typography>
              </>
            )}
          </Box>
        ))}
        <div className="text-center">
          <Button
            onClick={() => handleLoadMoreComments(id)}
            sx={{
              color: "#1b1e21",
              textTransform: "none",
              "&:hover": {
                color: "#343a40",
              },
            }}
          >
            Load More Comments
          </Button>
        </div>
        {/* Add New Comment */}
        <form
          onSubmit={(event) => {
            event.preventDefault();
            handleAddComment();
          }}
          style={{ display: "flex", marginTop: 2 }}
        >
          <TextField
            value={newComment}
            onChange={handleCommentChange}
            multiline
            onKeyDown={(event) => {
              if (event.key === "Enter" && !event.shiftKey) {
                event.preventDefault();
                handleAddComment();
              }
            }}
            placeholder="Add a comment..."
            sx={{ flexGrow: 1 }}
          />
          <Button
            type="submit"
            variant="contained"
            sx={{
              marginLeft: 1,
              backgroundColor: "#1b1e21",
              color: "#ffffff",
              "&:hover": {
                backgroundColor: "#343a40",
              },
            }}
          >
            Comment
          </Button>
        </form>
      </Box>
    </Box>
  );
};
