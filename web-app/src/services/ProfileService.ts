import { HttpClient } from "../configurations/HttpClient";
import { API, PAGE_RESPONSE_PARAM } from "../configurations/Configuration";
import { getAccessToken } from "./LocalStorageService";

export const getMyProfile = async () => {
  return await HttpClient.get(API.MY_PROFILE, {
    headers: {
      Authorization: `Bearer ${getAccessToken()}`,
    },
  });
};

export const getProfileById = async (userId: string) => {
  return await HttpClient.get(API.PROFILE_BY_USER_ID, {
    headers: { Authorization: `Bearer ${getAccessToken()}` },
    params: { userId: userId },
  });
};

export const updateProfile = async (
  firstName: string,
  lastName: string,
  dob: Date,
  address: string
) => {
  return await HttpClient.put(
    API.UPDATE_PROFILE,
    {
      firstName: firstName,
      lastName: lastName,
      dob: dob,
      address: address,
    },
    {
      headers: {
        Authorization: `Bearer ${getAccessToken()}`,
      },
    }
  );
};

export const addFriend = async (friendId: string) => {
  return await HttpClient.post(
    `${API.SEND_ADD_FRIEND_REQUEST}?friendId=${(friendId)}`,
    null, // no request body
    {
      headers: {
        Authorization: `Bearer ${getAccessToken()}`,
      },
    }
  );
};

export const responseAddFriendRequest = async (
  friendId: string,
  accept: boolean
) => {
  return await HttpClient.post(
    API.RESPONSE_ADD_FRIEND_REQUEST,
    {
      friendId: friendId,
      isAccepted: accept,
    },
    {
      headers: {
        Authorization: `Bearer ${getAccessToken()}`,
      },
    }
  );
};

export const getAllAddFriendRequests = async (userId: string, page: number) => {
  return await HttpClient.get(API.ALL_ADD_FRIEND_REQUESTS, {
    headers: {
      Authorization: `Bearer ${getAccessToken()}`,
    },
    params: {
      userId: userId,
      page: page,
      size: PAGE_RESPONSE_PARAM.SIZE,
    },
  });
};

export const unfriend = async (friendId: string) => {
  return await HttpClient.delete(API.UNFRIEND, {
    headers: {
      Authorization: `Bearer ${getAccessToken()}`,
    },
    params: {
      friendId: friendId,
    },
  });
};

export const isFriend = async (friendId: string): Promise<any> => {
  return await HttpClient.get(API.IS_FRIEND, {
    headers: {
      Authorization: `Bearer ${getAccessToken()}`,
    },
    params: {
      friendId: friendId,
    },
  });
};

export const getFriends = async (page: number) => {
  return await HttpClient.get(API.GET_FRIENDS, {
    headers: {
      Authorization: `Bearer ${getAccessToken()}`,
    },
    params: {
      page: page,
    },
  });
};
