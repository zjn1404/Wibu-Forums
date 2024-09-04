import axios from "axios";
import { refresh, parseJwt, decodeJwt } from "./auth"
import { jwtDecode } from "jwt-decode";

axios.interceptors.request.use(
  async (config) => {
    let accessToken = localStorage.getItem("accessToken");
    if (accessToken) {
      const decodedToken = accessToken ? jwtDecode(accessToken) as string : null;
      const tokenExpiration = decodedToken ? parseJwt(decodedToken).exp * 1000 : 0;
      const now = new Date().getTime();
      if (now >= tokenExpiration - 10 * 60 * 1000) {
        console.log("tokenExpiration", now);
        accessToken = await refresh();
      }
      config.headers.Authorization = `Bearer ${accessToken}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);
