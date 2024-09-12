import React, { useEffect, useState } from "react";
import { Button, Box, Typography, Paper, Avatar } from "@mui/material";
import { isFriend, addFriend, unfriend } from "../services/ProfileService";
import { useNavigate } from "react-router-dom";
import { UserProfile } from "../entity/UserProfile";
import { Header } from "../components/Header";

export const UserFeatures = () => {
  const userId = window.location.pathname.split("/")[2];
  const [profile, setProfile] = useState<UserProfile>();
  const [friendStatus, setFriendStatus] = useState<boolean>(false);
  const navigate = useNavigate();

  useEffect(() => {
    const storedProfile = localStorage.getItem("profile");
    if (storedProfile) {
      setProfile(JSON.parse(storedProfile));
    }

    const checkFriendStatus = async () => {
      const response = await isFriend(userId);
      if (response.data.code === 1000) {
        setFriendStatus(response.data.result);
      }
    };

    checkFriendStatus();
  }, [userId]);

  const handleAddFriend = async () => {
    await addFriend(userId);
    setFriendStatus(true);
  };

  const handleUnfriend = async () => {
    await unfriend(userId);
    setFriendStatus(false);
  };

  return (
    <>
      <Header user={profile} />
      <Box
        sx={{
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          minHeight: "100vh",
          backgroundColor: "#f0f2f5",
          padding: 2,
        }}
      >
        {profile && (
          <Paper
            elevation={3}
            sx={{
              padding: 4,
              borderRadius: 2,
              textAlign: "center",
              maxWidth: 400,
              width: "100%",
              backgroundColor: "#fff",
            }}
          >
            <Avatar
              sx={{ width: 80, height: 80, margin: "0 auto", marginBottom: 2 }}
              alt={`${profile.firstName} ${profile.lastName}`}
            />
            <Typography variant="h4" gutterBottom>
              {profile.firstName} {profile.lastName}
            </Typography>
            <Button
              variant="contained"
              onClick={friendStatus ? handleUnfriend : handleAddFriend}
              sx={{
                marginBottom: 2,
                width: "100%",
                backgroundColor: "#1b1e21",
                color: "#fff",
                "&:hover": {
                  backgroundColor: "#334",
                },
              }}
            >
              {friendStatus ? "Unfriend" : "Add Friend"}
            </Button>
            <Button
              variant="outlined"
              onClick={() => navigate(`/profile/${userId}`)}
              sx={{
                width: "100%",
                marginBottom: 2,
                borderColor: "#1b1e21",
                color: "#1b1e21",
                "&:hover": {
                  backgroundColor: "#1b1e21",
                  color: "#fff",
                },
              }}
            >
              Profile
            </Button>
            <Button
              variant="outlined"
              onClick={() => navigate(`/post/user/${userId}`)}
              sx={{
                width: "100%",
                marginBottom: 2,
                borderColor: "#1b1e21",
                color: "#1b1e21",
                "&:hover": {
                  backgroundColor: "#1b1e21",
                  color: "#fff",
                },
              }}
            >
              Post
            </Button>
            <Button
              variant="outlined"
              disabled
              sx={{
                width: "100%",
                color: "#aaa",
                borderColor: "#ccc",
                "&:hover": {
                  borderColor: "#ccc",
                },
              }}
            >
              Message
            </Button>
          </Paper>
        )}
      </Box>
    </>
  );
};
