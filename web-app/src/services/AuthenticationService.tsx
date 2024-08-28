import {
  getAccessToken,
  getRefreshToken,
  removeAccessToken,
  removeRefreshToken,
  setAccessToken,
  setRefreshToken,
} from "./LocalStorageService";
import { HttpClient } from "../configurations/HttpClient";
import { API } from "../configurations/Configuration";

export const logIn = async (username: string, password: string) => {
  const response = await HttpClient.post(API.LOGIN, {
    username: username,
    password: password,
  });

  setAccessToken(response.data?.result?.accessToken);
  setRefreshToken(response.data?.result?.refreshToken);

  return response;
};

export const logOut = () => {
  removeAccessToken();
  removeRefreshToken();
};

export const isAuthenticated = () => {
  return getAccessToken();
};
