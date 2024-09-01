import { HttpClient } from "../configurations/HttpClient";
import { API } from "../configurations/Configuration";
import { getAccessToken } from "./LocalStorageService";

export const getMyPosts = async (page: any) => {
  return await HttpClient
    .get(API.MY_POSTS, {
      headers: {
        Authorization: `Bearer ${getAccessToken()}`,
      },
      params: {
        page: page,
        size: 10,
      },
    });
};