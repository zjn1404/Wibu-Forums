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

export const updatePost = async (postId: string, content: string) => {
  return await HttpClient.put(`${API.UPDATE_POST}/${postId}`, {
    content: content
  }, {
    headers: {
      Authorization: `Bearer ${getAccessToken()}`,
    }
  });
};

export const deletePost = async (postId: string) => {
  return await HttpClient.delete(API.DELETE_POST, {
    headers: {
      Authorization: `Bearer ${getAccessToken()}`,
    },
    params: {
      postId: postId,
    },
  });
};
