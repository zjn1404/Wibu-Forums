import {
  getAccessToken,
  getRefreshToken,
  removeAccessToken,
  removeRefreshToken,
  setAccessToken,
  setRefreshToken,
  removeProfileFromLocalStorage
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

export const register = async (
  username: string,
  password: string,
  email: string,
  phoneNumber: string,
  firstName: string,
  lastName: string,
  address: string,
  dob: Date
) => {
  const response = await HttpClient.post(API.REGISTER, {
    username: username,
    password: password,
    email: email,
    phoneNumber: phoneNumber,
    firstName: firstName,
    lastName: lastName,
    address: address,
    dob: dob,
    roles: ["USER"],
  });

  return response;
};

export const logOut = async () => {
  const response = await HttpClient.post(API.LOGOUT, {
    token: getAccessToken(),
  });
  removeProfileFromLocalStorage();
  removeAccessToken();
  removeRefreshToken();
};

export const isAuthenticated = () => {
  return getAccessToken();
};

export const refreshToken = async () => {
  console.log('Refreshing token');
  const response = await HttpClient.post(API.REFRESH_TOKEN, {
    token: getRefreshToken(),
  });
  
  setAccessToken(response.data?.result?.accessToken);
  setRefreshToken(response.data?.result?.refreshToken);
}

export const changePassword = async (
  currentPassword: string,
  newPassword: string
) => {
  return await HttpClient.post(
    API.CHANGE_PASSWORD,
    {
      oldPassword: currentPassword,
      newPassword: newPassword,
    },
    {
      headers: {
        Authorization: `Bearer ${getAccessToken()}`,
      },
    }
  );
};
