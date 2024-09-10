import { useEffect, useRef, useState } from "react";
import { UserProfile } from "../entity/UserProfile";
import { getFriends } from "../services/ProfileService";
import { Link as RouterLink } from "react-router-dom";
import {
  Avatar,
  Box,
  Link,
  CircularProgress,
  Typography,
  Card,
} from "@mui/material";
import { Header } from "../components/Header";
import { getProfileFromLocalStorage } from "../services/LocalStorageService";

export const Friends = () => {
  const [friends, setFriends] = useState<UserProfile[]>([]);
  const [page, setPage] = useState(1);
  const [loading, setLoading] = useState(false);
  const containerRef = useRef<HTMLDivElement>(null);

  const loadMoreFriends = async () => {
    if (loading) return;
    setLoading(true);
    try {
      const res = await getFriends(page);
      setFriends((prev) => [...prev, ...res.data.result.data]);
      setPage((prev) => prev + 1);
    } catch (error) {
      console.error("Failed to load more friends:", error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadMoreFriends();
  }, []);

  useEffect(() => {
    const handleScroll = () => {
      if (!containerRef.current) return;
      const bottom =
        containerRef.current.scrollHeight ===
        containerRef.current.scrollTop + containerRef.current.clientHeight;
      if (bottom && !loading) {
        loadMoreFriends();
      }
    };

    const refCurrent = containerRef.current;
    refCurrent?.addEventListener("scroll", handleScroll);
    return () => {
      refCurrent?.removeEventListener("scroll", handleScroll);
    };
  }, [loading]);

  return (
    <>
      <Header user={getProfileFromLocalStorage()} />
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
          <div
            ref={containerRef}
            style={{
              overflowY: "auto",
              height: "100vh",
              padding: "16px",
              backgroundColor: "#fff",
            }}
          >
            <Typography
              variant="h4"
              sx={{ marginBottom: 2, fontWeight: "bold" }}
            >
              Friends
            </Typography>
            {friends.length === 0 && !loading ? (
              <Typography variant="body1" color="textSecondary">
                No friends found.
              </Typography>
            ) : (
              <Box
                sx={{
                  display: "grid",
                  gridTemplateColumns: "repeat(auto-fill, minmax(220px, 1fr))",
                  gap: 2,
                }}
              >
                {friends.map((friend: UserProfile) => (
                  <Box
                    key={friend.userId} // Added key for better performance
                    sx={{
                      padding: 2,
                      borderRadius: 1,
                      backgroundColor: "#fff",
                      boxShadow: 1,
                      display: "flex",
                      alignItems: "center",
                      transition: "background-color 0.3s",
                      "&:hover": {
                        backgroundColor: "#f0f0f0",
                      },
                    }}
                  >
                    <Link
                      component={RouterLink}
                      to={`/user/${friend.userId}`}
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
                        flex: 1,
                      }}
                    >
                      <Avatar sx={{ marginRight: 2, width: 56, height: 56 }} />
                      {friend.firstName} {friend.lastName}
                    </Link>
                  </Box>
                ))}
              </Box>
            )}
            {loading && (
              <Box
                sx={{ display: "flex", justifyContent: "center", padding: 2 }}
              >
                <CircularProgress />
              </Box>
            )}
          </div>
        </Card>
      </div>
    </>
  );
};
