import { HttpClient } from "../configurations/HttpClient";
import { API } from "../configurations/Configuration";
import { getAccessToken } from "./LocalStorageService";

export const getMyInfo = async () => {
  return await HttpClient.get(API.MY_INFO, {
    headers: {
      Authorization: `Bearer ${getAccessToken()}`,
    },
  });
};

export const updateMyInfo = async (email: string, phoneNumber: string, verifyCode: string) => {
  return await HttpClient.put(API.UPDATE_MY_INFO, {
    email,
    phoneNumber,
    verifyCode,
  }, {
    headers: {
      Authorization: `Bearer ${getAccessToken()}`,
    },
  });
}