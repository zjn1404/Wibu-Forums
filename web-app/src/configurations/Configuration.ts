export const CONFIG = {
  API_GATEWAY: "http://localhost:8888/api",
};

export const API = {
  LOGIN: "/identity/auth/token",
  REGISTER: "/identity/users/registration",
  LOGOUT: "/identity/auth/logout",
  MY_INFO: "/identity/users/my-info",
  UPDATE_MY_INFO: "/identity/users/my-info",
  MY_PROFILE: "/profile/users/my-profile",
  UPDATE_PROFILE: "/profile/users",
  CREATE_POST: "/post/create-post",
  MY_POSTS: "/post/my-posts",
  CHANGE_PASSWORD: "/identity/auth/change-password",
  SEND_VERIFICATION_CODE: "/identity/verify/send-verify-code",
  REFRESH_TOKEN: "/identity/auth/refresh"
};

export const OAUTHCONFIG = {
  clientId: "60486252610-3lu1b07uuf5h2sokpsdikejp53cf1qsb.apps.googleusercontent.com",
  redirectUri: "http://localhost:3000/authenticate",
  authUri: "https://accounts.google.com/o/oauth2/auth"
};

export const CODE = {
  SUCCESS: 1000,
  VERIFICATION_CODE_EXPIRED: 2801
};