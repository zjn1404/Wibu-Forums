import React, { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { Snackbar, Alert } from "@mui/material";
import { FaGoogle } from "react-icons/fa";
import { isAuthenticated, logIn } from "../services/AuthenticationService";
import { OAUTHCONFIG } from "../configurations/Configuration";

export const Login: React.FC = () => {
  const navigate = useNavigate();
  
  const handleSignInWithGoogle = () => {
    const callbackUrl = OAUTHCONFIG.redirectUri;
    const authUrl = OAUTHCONFIG.authUri;
    const googleClientId = OAUTHCONFIG.clientId;

    const targetUrl = `${authUrl}?redirect_uri=${encodeURIComponent(
      callbackUrl
    )}&response_type=code&client_id=${googleClientId}&scope=openid%20email%20profile&access_type=offline`;

    window.location.href = targetUrl;
  };

  useEffect(() => {
    if (isAuthenticated()) {
      navigate("/");
    }
  }, [navigate]);

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [snackBarOpen, setSnackBarOpen] = useState(false);
  const [snackBarMessage, setSnackBarMessage] = useState("");

  const handleCloseSnackBar = (event?: any, reason?: any) => {
    if (reason === "clickaway") return;
    setSnackBarOpen(false);
  };

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();

    try {
      if (username == null || password == null || username.length == 0 || password.length == 0) {
        setSnackBarMessage("Username and password are required");
        setSnackBarOpen(true);
        return;
      }
      const response = await logIn(username, password);
      console.log("Response body:", response.data);
      navigate("/");
    } catch (error: any) {
      const errorResponse = error.response.data;
      setSnackBarMessage(errorResponse.message || "Login failed");
      setSnackBarOpen(true);
    }
  };

  return (
    <>
      <Snackbar
        open={snackBarOpen}
        onClose={handleCloseSnackBar}
        autoHideDuration={6000}
        anchorOrigin={{ vertical: "top", horizontal: "right" }}
      >
        <Alert
          onClose={handleCloseSnackBar}
          severity="error"
          variant="filled"
          sx={{ width: "100%" }}
        >
          {snackBarMessage}
        </Alert>
      </Snackbar>

      <div className="d-flex align-items-center justify-content-center vh-100 bg-dark">
        <div className="card p-4 shadow" style={{ width: "350px" }}>
          <h3 className="card-title text-center mb-4">Log In</h3>

          {/* Google Login */}
          <button
            className="btn btn-outline-dark w-100 mb-3 d-flex align-items-center justify-content-center"
            onClick={handleSignInWithGoogle}
          >
            <FaGoogle className="me-2" /> Sign in with Google
          </button>

          <hr />

          {/* Username/Password Login */}
          <form onSubmit={handleSubmit}>
            <div className="mb-3">
              <label htmlFor="username" className="form-label">
                Username
              </label>
              <input
                type="text"
                className="form-control"
                id="username"
                placeholder="Enter your username"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                required
              />
            </div>
            <div className="mb-3">
              <label htmlFor="password" className="form-label">
                Password
              </label>
              <input
                type="password"
                className="form-control"
                id="password"
                placeholder="Enter your password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
              />
            </div>
            <button type="submit" className="mt-3 btn btn-dark w-100">
              Log In
            </button>
          </form>

          {/* Create Account Link */}
          <div className="mt-4 text-center">
            <Link to="/register" className="text-dark">
              Create new account
            </Link>
          </div>
        </div>
      </div>
    </>
  );
};
