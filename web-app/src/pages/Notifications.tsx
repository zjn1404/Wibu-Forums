import React, { useEffect, useState, useCallback } from "react";
import {
  fetchUnreadNotifications,
  markNotificationsAsRead,
} from "../services/NotificationService";
import { Notification } from "../entity/Notification";
import {
  Box,
  Button,
  Typography,
  CircularProgress,
  IconButton,
} from "@mui/material";
import { CheckBox, CheckBoxOutlineBlank } from "@mui/icons-material";
import { Header } from "../components/Header";
import { getProfileFromLocalStorage } from "../services/LocalStorageService";
import { Link } from "react-router-dom";
import { NOTIFICATION_CHANNEL } from "../configurations/Configuration";

const PAGE_SIZE = 10;

export const Notifications = () => {
  const [notifications, setNotifications] = useState<Notification[]>([]);
  const [selectedNotifications, setSelectedNotifications] = useState<
    Set<string>
  >(new Set());
  const [isFetching, setIsFetching] = useState<boolean>(false);
  const [hasMore, setHasMore] = useState<boolean>(true);
  const [page, setPage] = useState<number>(1);

  useEffect(() => {
    const fetchNotifications = async () => {
      try {
        setIsFetching(true);
        const unreadNotifications = await fetchUnreadNotifications(
          page,
          PAGE_SIZE
        );
        setNotifications((prev) => [...prev, ...unreadNotifications]);
        setHasMore(unreadNotifications.length === PAGE_SIZE);
        setIsFetching(false);
      } catch (error) {
        console.error("Error fetching notifications:", error);
        setIsFetching(false);
      }
    };

    fetchNotifications();
  }, [page]);

  const loadMoreNotifications = useCallback(() => {
    if (isFetching || !hasMore) return;
    setPage((prevPage) => prevPage + 1);
  }, [isFetching, hasMore]);

  const handleMarkAsRead = async () => {
    const notificationIds = Array.from(selectedNotifications);
    try {
      await markNotificationsAsRead(notificationIds);
      setNotifications((prev) =>
        prev.filter((n) => !selectedNotifications.has(n.id))
      );
      setSelectedNotifications(new Set());
    } catch (error) {
      console.error("Error marking notifications as read:", error);
    }
  };

  const toggleSelectNotification = (id: string) => {
    setSelectedNotifications((prev) => {
      const newSelection = new Set(prev);
      if (newSelection.has(id)) {
        newSelection.delete(id);
      } else {
        newSelection.add(id);
      }
      return newSelection;
    });
  };

  return (
    <>
      <Header user={getProfileFromLocalStorage()} />

      <Box sx={{ padding: 2, maxWidth: 600, margin: "0 auto" }}>
        <Typography variant="h4" gutterBottom>
          <strong>Notifications</strong>
        </Typography>

        {/* Container for notifications with border */}
        <Box
          sx={{
            border: "1px solid #ddd",
            borderRadius: "8px",
            padding: 2,
            marginTop: 2,
            boxShadow: "0 4px 8px rgba(0, 0, 0, 0.1)",
          }}
        >
          {notifications.length > 0 ? (
            notifications.map((notification: Notification) => (
              <Box
                key={notification.id}
                sx={{
                  display: "flex",
                  alignItems: "center",
                  marginBottom: 2,
                  borderBottom: "1px solid #ddd",
                  paddingBottom: 1,
                }}
              >
                <IconButton
                  onClick={() => toggleSelectNotification(notification.id)}
                  size="small"
                  sx={{
                    color: selectedNotifications.has(notification.id)
                      ? "primary.main"
                      : "text.primary",
                  }}
                >
                  {selectedNotifications.has(notification.id) ? (
                    <CheckBox sx={{ color: "#3194bb" }} />
                  ) : (
                    <CheckBoxOutlineBlank />
                  )}
                </IconButton>
                <Link
                  to={
                    notification.channel === NOTIFICATION_CHANNEL.COMMENT ||
                    notification.channel === NOTIFICATION_CHANNEL.POST
                      ? `/post/${notification.componentId}`
                      : "/friends"
                  }
                  style={{
                    textDecoration: "none",
                    flexGrow: 1,
                    color: "#3194bb",
                  }}
                  onClick={() => markNotificationsAsRead([notification.id])}
                >
                  <Typography sx={{ marginLeft: 2 }}>
                    {notification.body}
                  </Typography>
                </Link>
                <Typography>{notification.formatedCreatedDate}</Typography>
              </Box>
            ))
          ) : (
            <Typography>No notifications available.</Typography>
          )}

          {isFetching && (
            <CircularProgress sx={{ display: "block", margin: "20px auto" }} />
          )}

          {true && (
            <Button
              onClick={loadMoreNotifications}
              variant="contained"
              sx={{
                display: "block",
                margin: "20px auto",
                backgroundColor: "#3194bb",
              }}
            >
              Load More
            </Button>
          )}

          {selectedNotifications.size > 0 && (
            <Button
              variant="contained"
              onClick={handleMarkAsRead}
              sx={{
                display: "block",
                margin: "20px auto",
                backgroundColor: "#3194bb",
              }}
            >
              Mark as Read
            </Button>
          )}
        </Box>
      </Box>
    </>
  );
};
