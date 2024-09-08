import React, { useEffect } from "react";
import "./App.css";
import { AppRoutes } from "./routes/AppRoutes";
import { parseJwt, refresh } from "./services/AuthenticationConfig/auth";

function App() {
  useEffect(() => {
    const checkToken = async () => {
      const accessToken = localStorage.getItem("accessToken");
      const refreshToken = localStorage.getItem("refreshToken");

      if (accessToken && refreshToken) {
        try {
          const parsedToken = parseJwt(accessToken);
          const tokenExpiration = parsedToken?.exp * 1000;
          const now = new Date().getTime();

          if (now >= tokenExpiration - 10 * 60 * 1000) {
            await refresh();
          }
        } catch (error) {
          console.error("Error during initial token check:", error);
          localStorage.removeItem("accessToken");
          localStorage.removeItem("refreshToken");
          window.location.href = "/login";
        }
      }
    };

    checkToken();

    const intervalId = setInterval(async () => {
      const accessToken = localStorage.getItem("accessToken");
      if (accessToken) {
        const parsedToken = parseJwt(accessToken);
        const tokenExpiration = parsedToken?.exp * 1000;
        const now = new Date().getTime();

        if (now >= tokenExpiration - 10 * 60 * 1000) {
          await refresh();
        }
      }
    }, 10 * 60 * 1000);

    return () => clearInterval(intervalId);
  }, []);

  return (
    <>
      <AppRoutes />
    </>
  );
}

export default App;
