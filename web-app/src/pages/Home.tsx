import { useEffect, useState, useRef, useCallback } from "react";
import { useNavigate } from "react-router-dom";
import {
  Box,
  Card,
  CircularProgress,
  Typography,
  Snackbar,
  Alert,
} from "@mui/material";
import { isAuthenticated, logOut } from "../services/AuthenticationService";
import { Post } from "../components/Post";
import { getMyPosts, createPost, deletePost, updatePost } from "../services/PostService";
import { Header } from "../components/Header";
import DeleteIcon from "@mui/icons-material/Delete";

interface Post {
  id: string;
  avatarUrl: string;
  username: string;
  formattedPostedDate: string;
  postedDate: string;
  content: string;
  images: string[];
}

export const Home = () => {
  const [posts, setPosts] = useState<Post[]>([]);
  const [page, setPage] = useState(1);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(true);
  const [hasMore, setHasMore] = useState(false);
  const [newPostContent, setNewPostContent] = useState("");
  const [newPostImages, setNewPostImages] = useState<File[]>([]);
  const [imagePreviews, setImagePreviews] = useState<string[]>([]);
  const [creatingPost, setCreatingPost] = useState(false);
  const [openSnackbar, setOpenSnackbar] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState("");
  const [snackbarSeverity, setSnackbarSeverity] = useState<"success" | "error">(
    "success"
  );

  const observer = useRef<IntersectionObserver | null>(null);
  const lastPostElementRef = useRef<HTMLDivElement | null>(null);

  const navigate = useNavigate();

  useEffect(() => {
    if (!isAuthenticated()) {
      navigate("/login");
    } else {
      loadPosts(page);
    }
  }, [navigate, page]);

  const loadPosts = useCallback(
    (page: number) => {
      setLoading(true);
      getMyPosts(page)
        .then((response) => {
          setTotalPages(response.data.result.totalPages);
          const transformedPosts = response.data.result.data.map(
            (post: any) => {
              if (post.user) {
                return {
                  avatarUrl: post.user.avatarUrl ?? "",
                  username: post.user.username ?? "Unknown",
                  id: post.id,
                  formattedPostedDate: post.formattedPostedDate ?? "",
                  postedDate: post.postedDate ?? "",
                  content: post.content ?? "",
                  images: post.images ?? [],
                };
              }
              return {
                id: post.id,
                avatarUrl: "",
                username: "Unknown",
                formattedPostedDate: post.formattedPostedDate ?? "",
                postedDate: post.postedDate ?? "",
                content: post.content ?? "",
                images: post.images ?? [],
              };
            }
          );
          setPosts((prevPosts) => [...prevPosts, ...transformedPosts]);
          setHasMore(response.data.result.data.length > 0);
        })
        .catch((error) => {
          if (error.response && error.response.status === 401) {
            logOut();
            navigate("/login");
          } else {
            console.error("An error occurred while fetching posts:", error);
          }
        })
        .finally(() => {
          setLoading(false);
        });
    },
    [navigate]
  );

  useEffect(() => {
    if (!hasMore) return;

    if (observer.current) observer.current.disconnect();

    observer.current = new IntersectionObserver(
      (entries) => {
        if (entries[0].isIntersecting && page < totalPages) {
          setPage((prevPage) => prevPage + 1);
        }
      },
      { threshold: 1.0 }
    );

    if (lastPostElementRef.current) {
      observer.current.observe(lastPostElementRef.current);
    }

    return () => {
      if (observer.current) observer.current.disconnect();
    };
  }, [hasMore, page, totalPages]);

  const handleCreatePost = () => {
    if (!newPostContent) {
      setSnackbarMessage("Content is required");
      setSnackbarSeverity("error");
      setOpenSnackbar(true);
      return;
    }

    setCreatingPost(true);

    const formData = new FormData();
    formData.append("content", newPostContent);
    newPostImages.forEach((image) => formData.append("images", image));

    createPost(formData)
      .then((response) => {
        setPosts((prevPosts) => [response.data.result, ...prevPosts]);
        setNewPostContent("");
        setNewPostImages([]);
        setImagePreviews([]);
        setSnackbarMessage("Post created successfully!");
        setSnackbarSeverity("success");
      })
      .catch((error) => {
        setSnackbarMessage("An error occurred while creating the post");
        setSnackbarSeverity("error");
        console.error("An error occurred while creating the post:", error);
      })
      .finally(() => {
        setCreatingPost(false);
        setOpenSnackbar(true);
      });
  };

  const handleImageChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files) {
      const filesArray = Array.from(e.target.files);
      setNewPostImages((prevImages) => [...prevImages, ...filesArray]);

      const previewUrls = filesArray.map((file) => URL.createObjectURL(file));
      setImagePreviews((prevPreviews) => [...prevPreviews, ...previewUrls]);
    }
  };

  const removeImage = (index: number) => {
    setNewPostImages((prevImages) => prevImages.filter((_, i) => i !== index));
    setImagePreviews((prevPreviews) =>
      prevPreviews.filter((_, i) => i !== index)
    );
  };

  const handleCloseSnackbar = () => {
    setOpenSnackbar(false);
  };

  const handleDeletePost = async (id: string) => {
    try {
      const response = await deletePost(id);
      if (response) {
        setSnackbarMessage(response.data.message);
        setSnackbarSeverity("success");
        setPosts((prevPosts) => prevPosts.filter((post) => post.id !== id));
      } else {
        setSnackbarMessage("Failed to delete post");
        setSnackbarSeverity("error");
      }
    } catch (error) {
      setSnackbarMessage("An error occurred");
      setSnackbarSeverity("error");
    } finally {
      setOpenSnackbar(true);
    }
  };

  const handleUpdatePost = async (id: string, content: string) => {
    try {
      const response = await updatePost(id, content);
      if (response) {
        setSnackbarMessage(response.data.message);
        setSnackbarSeverity("success");
        setPosts((prevPosts) =>
          prevPosts.map((post) =>
            post.id === id ? { ...post, content: content } : post
          )
        );
      } else {
        setSnackbarMessage("Failed to update post");
        setSnackbarSeverity("error");
      }
    } catch (error) {
      setSnackbarMessage("An error occurred");
      setSnackbarSeverity("error");
    } finally {
      setOpenSnackbar(true);
    }
  }

  return (
    <>
      <Snackbar
        open={openSnackbar}
        autoHideDuration={3000}
        onClose={handleCloseSnackbar}
        anchorOrigin={{ vertical: "top", horizontal: "right" }}
      >
        <Alert
          onClose={handleCloseSnackbar}
          severity={snackbarSeverity}
          variant="filled"
          sx={{ width: "100%" }}
        >
          {snackbarMessage}
        </Alert>
      </Snackbar>
      <Header />
      <div className="d-flex justify-content-center mt-5">
        <Card
          sx={{
            minWidth: "80%",
            maxWidth: "80%",
            boxShadow: 3,
            borderRadius: 2,
            padding: "20px",
            overflow: "visible",
          }}
        >
          {/* Post Creation Area */}
          <Box
            sx={{
              display: "flex",
              flexDirection: "column",
              gap: "10px",
              marginBottom: "20px",
              width: "100%",
              padding: "10px",
              border: "1px solid #ddd",
              borderRadius: "8px",
              boxShadow: "0 2px 4px rgba(0, 0, 0, 0.1)",
            }}
          >
            <Typography sx={{ fontSize: 16, fontWeight: "bold" }}>
              Create a Post
            </Typography>
            <textarea
              placeholder="What's on your mind?"
              value={newPostContent}
              onChange={(e) => setNewPostContent(e.target.value)}
              rows={3}
              style={{
                width: "100%",
                padding: "8px",
                border: "1px solid #ddd",
                borderRadius: "4px",
                resize: "none",
              }}
            />
            {/* Custom File Upload Button */}
            <label
              htmlFor="upload-button"
              style={{
                display: "inline-block",
                backgroundColor: "#1b1e21",
                color: "#fff",
                padding: "8px 16px",
                borderRadius: "4px",
                cursor: "pointer",
                marginTop: "8px",
                textAlign: "center",
              }}
            >
              Add Image
            </label>
            <input
              id="upload-button"
              type="file"
              accept="image/*"
              multiple
              onChange={handleImageChange}
              style={{
                display: "none",
              }}
            />
            {/* Image Previews */}
            <Box sx={{ display: "flex", gap: "10px", flexWrap: "wrap" }}>
              {imagePreviews.map((preview, index) => (
                <Box
                  key={index}
                  sx={{
                    position: "relative",
                    width: "100px",
                    height: "100px",
                    borderRadius: "4px",
                    overflow: "hidden",
                    "&:hover .overlay": {
                      opacity: 1,
                    },
                  }}
                >
                  {/* Display the image */}
                  <img
                    src={preview}
                    alt={`preview-${index}`}
                    style={{
                      width: "100%",
                      height: "100%",
                      objectFit: "cover",
                      borderRadius: "4px",
                    }}
                  />

                  {/* Overlay with Delete Icon */}
                  <Box
                    className="overlay"
                    sx={{
                      position: "absolute",
                      top: 0,
                      left: 0,
                      right: 0,
                      bottom: 0,
                      backgroundColor: "rgba(0, 0, 0, 0.5)",
                      display: "flex",
                      justifyContent: "center",
                      alignItems: "center",
                      opacity: 0,
                      transition: "opacity 0.3s ease",
                      cursor: "pointer",
                    }}
                    onClick={() => removeImage(index)}
                  >
                    <DeleteIcon fontSize="medium" sx={{ color: "white" }} />
                  </Box>
                </Box>
              ))}
            </Box>
            <Box
              sx={{
                display: "flex",
                justifyContent: "center",
                gap: "10px",
                marginTop: "10px",
              }}
            >
              <button
                onClick={handleCreatePost}
                disabled={creatingPost}
                style={{
                  backgroundColor: "#1b1e21",
                  color: "#fff",
                  border: "none",
                  padding: "8px 16px",
                  borderRadius: "4px",
                  cursor: "pointer",
                  opacity: creatingPost ? 0.7 : 1,
                }}
              >
                {creatingPost ? "Posting..." : "Post"}
              </button>
            </Box>
          </Box>
          {/* Posts List */}
          <Typography sx={{ fontSize: 16, fontWeight: "bold", marginBottom: "10px" }}>
            Posts
          </Typography>

          {posts.map((post) => (
            <Post key={post.id} post={post} onDelete={() => handleDeletePost(post.id)} onUpdate={(id, updatedContent) => handleUpdatePost(id, updatedContent)} />
          ))}
          {loading && (
            <div className="d-flex justify-content-center mt-4">
              <CircularProgress />
            </div>
          )}
        </Card>
      </div>
    </>
  );
};
