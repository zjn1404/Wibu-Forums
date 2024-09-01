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