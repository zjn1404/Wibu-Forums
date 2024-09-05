import { useEffect, useState, useRef, useCallback } from "react";
import { useNavigate } from "react-router-dom";
import { Box, Card, CircularProgress, Typography } from "@mui/material";
import { isAuthenticated, logOut } from "../services/AuthenticationService";
import { Post } from "../components/Post";
import { getMyPosts } from "../services/PostService";
import { Header } from "../components/Header";

interface Post {
  avatarUrl: string;
  username: string;
  formattedPostedDate: string;
  postedDate: string;
  content: string;
  image: string;
}

export const Home = () => {
  const [posts, setPosts] = useState<Post[]>([]);
  const [page, setPage] = useState(1);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(true);
  const [hasMore, setHasMore] = useState(false);

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
      console.log(`loading posts for page ${page}`);
      setLoading(true);
      getMyPosts(page)
        .then((response) => {
          setTotalPages(response.data.result.totalPages);

          // Transform response data to match the expected structure
          const transformedPosts = response.data.result.data.map(
            (post: any) => {
              if (post.user) {
                return {
                  avatarUrl: post.user.avatarUrl ?? "",
                  username: post.user.username ?? "Unknown",
                  formattedPostedDate: post.formattedPostedDate ?? "",
                  postedDate: post.postedDate ?? "",
                  content: post.content ?? "",
                  image: post.image ?? "",
                };
              }
              return {
                avatarUrl: "",
                username: "Unknown",
                formattedPostedDate: post.formattedPostedDate ?? "",
                postedDate: post.postedDate ?? "",
                content: post.content ?? "",
                image: post.image ?? "",
              };
            }
          );

          setPosts((prevPosts) => [...prevPosts, ...transformedPosts]);
          setHasMore(response.data.result.data.length > 0);
          console.log("loaded posts:", response.data.result);
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
      { threshold: 1.0 } // Adjust threshold if necessary
    );

    if (lastPostElementRef.current) {
      observer.current.observe(lastPostElementRef.current);
    }

    return () => {
      if (observer.current) observer.current.disconnect();
    };
  }, [hasMore, page, totalPages]);

  return (
    <>
      <Header />
      <div className="d-flex justify-content-center mt-5">
        <Card
          sx={{
            minWidth: "80%",
            maxWidth: "80%",
            boxShadow: 3,
            borderRadius: 2,
            padding: "20px",
            overflow: "visible", // Ensures content inside the Card is not clipped
          }}
        >
          <Box
            sx={{
              display: "flex",
              flexDirection: "column",
              alignItems: "flex-start",
              width: "100%",
              gap: "10px",
              overflow: "visible", // Prevents clipping of the content
            }}
          >
            <Typography
              sx={{
                fontSize: 18,
                mb: "10px",
              }}
            >
              Your posts,
            </Typography>
            {posts.map((post, index) => {
              return (
                <Box
                  ref={posts.length === index + 1 ? lastPostElementRef : null}
                  key={post.postedDate}
                  sx={{
                    width: "100%", // Ensures the Box takes full width of the parent
                    overflow: "visible", // Prevents clipping of the content
                  }}
                >
                  <Post post={post} />
                </Box>
              );
            })}
            {loading && (
              <Box
                sx={{
                  display: "flex",
                  justifyContent: "center",
                  width: "100%",
                }}
              >
                <CircularProgress size="24px" />
              </Box>
            )}
          </Box>
        </Card>
      </div>
    </>
  );
};
