import { useNavigate } from "react-router-dom";
import { useState, useEffect } from "react";
import { setAccessToken, setRefreshToken } from "../services/LocalStorageService";
import { Box, CircularProgress, Typography } from "@mui/material";

export default function Authenticate() {
  const navigate = useNavigate();
  const [isLoggedin, setIsLoggedin] = useState(false);

  useEffect(() => {
    console.log(window.location.href);

    const accessTokenRegex = /access_token=([^&]+)/;
    // const setRefreshTokenRegex = /refresh_token=([^&]+)/;
    const isMatchAT = window.location.href.match(accessTokenRegex);
    // const isMathFT = window.location.href.match(setRefreshTokenRegex);

    if (isMatchAT /*&& isMathFT*/) {
      const accessToken = isMatchAT[1];
      // const refreshToken = isMathFT[1];
      console.log("Token: ", accessToken);

      setAccessToken(accessToken);
      // setRefreshToken(refreshToken);
      setIsLoggedin(true);
    }
  }, []);

  useEffect(() => {
    if (isLoggedin) {
      navigate("/");
    }
  }, [isLoggedin, navigate]);

  return (
    <>
      <Box
        sx={{
          display: "flex",
          flexDirection : "column",
          gap: "30px",
          justifyContent: "center",
          alignItems: "center",
          height: "100vh",
        }}
      >
        <CircularProgress></CircularProgress>
        <Typography>Authenticating...</Typography>
      </Box>
    </>
  );
}