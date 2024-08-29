import { useNavigate } from "react-router-dom";
import { useState, useEffect } from "react";
import { setAccessToken, setRefreshToken } from "../services/LocalStorageService";
import { Box, CircularProgress, Typography } from "@mui/material";
import axios from "axios";

export default function Authenticate() {
  const navigate = useNavigate();
  const [isLoggedin, setIsLoggedin] = useState(false);

  useEffect(() => {
    console.log(window.location.href);

    const authCodeRegex = /code=([^&]+)/;
    const isMatch = window.location.href.match(authCodeRegex);

    if (isMatch) {
      const authCode = isMatch[1];
      
      axios.post(`http://localhost:8888/api/identity/auth/outbound/authentication?code=${authCode}`)
      .then(reponse => reponse.data)
      .then(data => {
        const accessToken = data.result.accessToken;
        const refreshToken = data.result.refreshToken;
        
        setAccessToken(accessToken);
        setRefreshToken(refreshToken);
        setIsLoggedin(true);
      })

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