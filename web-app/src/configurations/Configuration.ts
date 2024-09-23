export const CONFIG = {
  API_GATEWAY: "http://localhost:8888/api",
};

export const API = {
  LOGIN: "/identity/auth/token",
  REGISTER: "/identity/users/registration",
  LOGOUT: "/identity/auth/logout",
  MY_INFO: "/identity/users/my-info",
  UPDATE_MY_INFO: "/identity/users/my-info",
  PROFILE_BY_USER_ID: "/profile/users/get-by-user-id",
  MY_PROFILE: "/profile/users/my-profile",
  IS_FRIEND: "/profile/users/is-friend",
  SEND_ADD_FRIEND_REQUEST: "/profile/requests/add-friend",
  RESPONSE_ADD_FRIEND_REQUEST: "/profile/requests/add-friend-response",
  ALL_ADD_FRIEND_REQUESTS: "/profile/requests/all-add-friend",
  UNFRIEND: "/profile/users/unfriend",
  UPDATE_PROFILE: "/profile/users",
  GET_FRIENDS: "/profile/users/get-friends",
  CREATE_POST: "/post/create-post",
  MY_POSTS: "/post/my-posts",
  FRIENDS_POSTS: "/post/friends-posts",
  POST_BY_USER_ID: "/post/get-by-user-id",
  GET_POST_BY_ID: "/post/get-by-id",
  DELETE_POST: "/post/delete-post",
  UPDATE_POST: "/post/update-post",
  CREATE_COMMENT: "/post/comment",
  UPDATE_COMMENT: "/post/comment",
  DELETE_COMMENT: "/post/comment",
  COMMENT_OF_POST: "/post/comment",
  CHANGE_PASSWORD: "/identity/auth/change-password",
  SEND_VERIFICATION_CODE: "/identity/verify/send-verify-code",
  REFRESH_TOKEN: "/identity/auth/refresh",
  NOTIFICATION_SOCKET: "/notification/ws",
  UNREAD_NOTIFICATIONS: "/notification/unread-notifications",
  MARK_NOTIFICATION_AS_READ: "/notification/mark-as-read",
  MESSAGE_SOCKET: "/message/ws",
  GET_MESSAGE: "/message/get-all-messages",
  MARK_MESSAGE_AS_READ: "/message/mark-as-read",
  SERVER_CHAT_DESTINATION: "/app/chat",
};

export const OAUTHCONFIG = {
  clientId:
    "60486252610-3lu1b07uuf5h2sokpsdikejp53cf1qsb.apps.googleusercontent.com",
  redirectUri: "http://localhost:3000/authenticate",
  authUri: "https://accounts.google.com/o/oauth2/auth",
};

export const CODE = {
  SUCCESS: 1000,
  VERIFICATION_CODE_EXPIRED: 2801,
};

export const ROLE = {
  ADMIN: "ROLE_ADMIN",
  USER: "ROLE_USER",
};

export const NOTIFICATION_CHANNEL = {
  POST: "POST",
  COMMENT: "COMMENT",
  FRIEND: "FRIEND",
}

export const PAGE_RESPONSE_PARAM = {
  SIZE: 10
}
