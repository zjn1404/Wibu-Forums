import { getMyProfile } from "./ProfileService";

export const PROFILE = 'profile';
export const ACCESS_TOKEN = "accessToken";
export const REFRESH_TOKEN = "refreshToken";

export const setAccessToken = (token : string) => {
  localStorage.setItem(ACCESS_TOKEN, token);
};

export const setRefreshToken = (token : string) => {
  localStorage.setItem(REFRESH_TOKEN, token);
};

export const getAccessToken = () => {
  return localStorage.getItem(ACCESS_TOKEN);
};

export const getRefreshToken = () => {
  return localStorage.getItem(REFRESH_TOKEN);
};

export const removeAccessToken = () => {
  return localStorage.removeItem(ACCESS_TOKEN);
};

export const removeRefreshToken = () => {
  return localStorage.removeItem(REFRESH_TOKEN);
};

export const getProfileFromLocalStorage = () => {
  const userProfileString = localStorage.getItem(PROFILE);
    if (userProfileString) {
      return JSON.parse(userProfileString);
    }
}

export const removeProfileFromLocalStorage = () => {
  localStorage.removeItem(PROFILE);
}

export const saveUserProfileInLocalStorage = async () => {
  try {
    const response = await getMyProfile();
    const data = response.data;
    
    localStorage.setItem("profile", JSON.stringify(data.result));
  } catch (error) {
    console.error("Failed to load profile", error);
  }
};