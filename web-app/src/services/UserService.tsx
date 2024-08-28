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