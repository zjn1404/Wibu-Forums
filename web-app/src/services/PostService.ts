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

export const createComment = async (postId: string, content: string) => {
  return await HttpClient.post(API.CREATE_COMMENT, {
    content: content,
    postId: postId
  }, {
    headers: {
      Authorization: `Bearer ${getAccessToken()}`,
    }
  });
};

export const updateComment = async (commentId: string, content: string) => {
  return await HttpClient.put(API.UPDATE_COMMENT, {
    content: content
  }, {
    params: {
      commentId: commentId
    },
    headers: {
      Authorization: `Bearer ${getAccessToken()}`,
    }
  });
}

export const getCommentsOfPost = async (postId: string, page: number) => {
  return await HttpClient.get(API.COMMENT_OF_POST, {
    params: {
      postId: postId,
      page: page
    },
    headers: {
      Authorization: `Bearer ${getAccessToken()}`,
    }
  });
}

export const deleteCommnet = async (commentId: string) => {
  return await HttpClient.delete(API.DELETE_COMMENT, {
    params: {
      commentId: commentId,
    },
    headers: {
      Authorization: `Bearer ${getAccessToken()}`,
    }
  });
}