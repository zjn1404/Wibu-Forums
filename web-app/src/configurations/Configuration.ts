export const CONFIG = {
  API_GATEWAY: "http://localhost:8888/api",
};

export const API = {
  LOGIN: "/identity/auth/token",
  REGISTER: "/identity/users/registration",
  LOGOUT: "/identity/auth/logout",
  MY_INFO: "/identity/users/my-info",
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