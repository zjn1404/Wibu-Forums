import { HttpClient } from "../configurations/HttpClient";
import { API } from "../configurations/Configuration";
import { getAccessToken } from "./LocalStorageService";

export const createPost = async (formData: FormData) => {

  return await HttpClient.post(API.CREATE_POST, formData, {
    headers: {
      Authorization: `Bearer ${getAccessToken()}`,
      "Content-Type": "multipart/form-data",
    },
  });
};

export const getMyPosts = async (page: any) => {
  return await HttpClient.get(API.MY_POSTS, {
    headers: {
      Authorization: `Bearer ${getAccessToken()}`,
    },
    params: {
      page: page,
      size: 10,
    },
  });
};
