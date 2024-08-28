import axios from "axios";
import { CONFIG } from "./Configuration";

export const HttpClient = axios.create({
  baseURL: CONFIG.API_GATEWAY,
  timeout: 30000,
  headers: {
    "Content-Type": "application/json",
  },
});