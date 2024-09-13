import { useEffect, useRef, useState } from "react";
import { UserProfile } from "../entity/UserProfile";
import {
  getFriends,
  getAllAddFriendRequests,
  responseAddFriendRequest,
} from "../services/ProfileService";
import { Link as RouterLink } from "react-router-dom";
import {
  Avatar,
  Box,
  Link,
  CircularProgress,
  Typography,
  Card,
  Button,
} from "@mui/material";
import { Header } from "../components/Header";
import { getProfileFromLocalStorage } from "../services/LocalStorageService";

export const Friends = () => {
  const [friends, setFriends] = useState<UserProfile[]>([]);
  const [friendRequests, setFriendRequests] = useState<any[]>([]);
  const [page, setPage] = useState(1);
  const [requestPage, setRequestPage] = useState(1);
  const [loading, setLoading] = useState(false);
  const [loadingRequests, setLoadingRequests] = useState(false);
  const [hasMoreRequests, setHasMoreRequests] = useState(true);
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

  const loadMoreFriendRequests = async () => {
    if (loadingRequests || !hasMoreRequests) return;
    setLoadingRequests(true);
    try {
      const userId = getProfileFromLocalStorage()?.userId; // Assume you have a function to get the logged-in user's ID
      const res = await getAllAddFriendRequests(userId, requestPage);
      const { data, totalPages } = res.data.result;
      setFriendRequests((prev) => [...prev, ...data]);
      setHasMoreRequests(requestPage < totalPages);
      setRequestPage((prev) => prev + 1);
    } catch (error) {
      console.error("Failed to load friend requests:", error);
    } finally {
      setLoadingRequests(false);
    }
  };

  const handleFriendRequestResponse = async (
    userId: string,
    accept: boolean
  ) => {
    try {
      await responseAddFriendRequest(userId, accept);
      // Remove the request from the state after responding
      setFriendRequests((prev) =>
        prev.filter((request) => request.sendingUserId !== userId)
      );
    } catch (error) {
      console.error("Failed to respond to friend request:", error);
    }
  };

  useEffect(() => {
    loadMoreFriends();
    loadMoreFriendRequests();
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
              Add Friend Requests
            </Typography>
            {friendRequests.length === 0 && !loadingRequests ? (
              <>
                <Typography
                  variant="body1"
                  color="textSecondary"
                  sx={{ marginBottom: 3 }}
                >
                  No friend requests found.
                </Typography>
                <hr />
              </>
            ) : (
              <Box sx={{ marginBottom: 3 }}>
                {friendRequests.map((request) => (
                  <Box
                    key={request.sendingUserId}
                    sx={{
                      padding: 2,
                      borderRadius: 1,
                      border: "1px solid #ccc",
                      marginBottom: 2,
                      display: "flex",
                      alignItems: "center",
                      justifyContent: "space-between",
                    }}
                  >
                    <Typography variant="body2" sx={{ flex: 1 }}>
                      {request.sendingUserName} wants to be your friend.
                    </Typography>
                    <Button
                      variant="contained"
                      onClick={() =>
                        handleFriendRequestResponse(request.sendingUserId, true)
                      }
                      sx={{
                        marginRight: 1,
                        backgroundColor: "#3194bb",
                        "&:hover": { backgroundColor: "#287b9e" },
                      }}
                    >
                      Accept
                    </Button>
                    <Button
                      variant="outlined"
                      onClick={() =>
                        handleFriendRequestResponse(
                          request.sendingUserId,
                          false
                        )
                      }
                      sx={{
                        borderColor: "#3194bb",
                        color: "#3194bb",
                        "&:hover": {
                          borderColor: "#287b9e",
                          color: "#287b9e",
                        },
                      }}
                    >
                      Decline
                    </Button>
                  </Box>
                ))}
                {hasMoreRequests && (
                  <Button
                    onClick={loadMoreFriendRequests}
                    disabled={loadingRequests}
                    sx={{ marginTop: 2 }}
                  >
                    {loadingRequests ? "Loading..." : "Show More Requests"}
                  </Button>
                )}
              </Box>
            )}
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
                    key={friend.userId}
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
