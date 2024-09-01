import { HttpClient } from "../configurations/HttpClient";
import { API } from "../configurations/Configuration";
import { getAccessToken } from "./LocalStorageService";

export const getMyProfile = async () => {
  return await HttpClient.get(API.MY_PROFILE, {
    headers: {
      Authorization: `Bearer ${getAccessToken()}`,
    },
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
