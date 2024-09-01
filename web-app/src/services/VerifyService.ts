import { HttpClient } from "../configurations/HttpClient";
import { API } from "../configurations/Configuration";
import { getAccessToken } from "./LocalStorageService";

export const sendVerificationCode = async (email: string) => {
  return await HttpClient.post(
    API.SEND_VERIFICATION_CODE,
    {
      email,
    },
    {
      headers: {
        Authorization: `Bearer ${getAccessToken()}`,
      },
    }
  );
};
