import SockJS from "sockjs-client";
import { Client, IMessage, StompConfig } from "@stomp/stompjs";
import { API } from "../configurations/Configuration";
import { Notification } from "../entity/Notification";
import { HttpClient } from "../configurations/HttpClient";
import { getAccessToken } from "./LocalStorageService";
import { useNotification } from "../components/NotificationContext";
import { getProfileFromLocalStorage } from "./LocalStorageService";
import { UserProfile } from "../entity/UserProfile";

export const connectSocketServer = (
  onNotificationReceived: (notification: Notification) => void
) => {
  const userProfile: UserProfile = getProfileFromLocalStorage();

  const socketFactory = () =>
    new SockJS(`http://localhost:8082${API.NOTIFICATION_SOCKET}`);

  // Create Stomp client
  const stompClient = new Client({
    webSocketFactory: socketFactory,
    debug: function (str: string) {
      console.log(str); // Add debug logging
    },
    reconnectDelay: 5000, // Retry every 5 seconds if connection is lost
    heartbeatIncoming: 0,
    heartbeatOutgoing: 20000, // Send heartbeat every 20 seconds
  });

  // Handle connection success
  stompClient.onConnect = (frame) => {
    console.log("WebSocket connected:", frame);
    const uniqueId = `sub-${Date.now()}`;
    stompClient.subscribe(
      `/user/${userProfile.userId}/queue/notifications`,
      (message: IMessage) => {
        try {
          console.log("Received message:", message);
          const notification: Notification = JSON.parse(message.body);
          onNotificationReceived(notification);

          // Show notification using the context
          const { showNotification } = useNotification();
          showNotification(notification.body); // Adjust as needed
        } catch (e) {
          console.error("Failed to parse notification:", e);
        }
      },
      { id: uniqueId }
    );
  };

  // Handle connection error
  stompClient.onStompError = (frame: any) => {
    console.error("WebSocket connection error:", frame);
  };

  // Activate the client
  stompClient.activate();

  // Return disconnect function
  return {
    disconnect: (callback: () => void) => {
      stompClient.deactivate().then(callback);
    },
  };
};

// Fetch unread notifications from the server
export const fetchUnreadNotifications = async (): Promise<Notification[]> => {
  return await HttpClient.get(API.UNREAD_NOTIFICATIONS, {
    headers: {
      Authorization: `Bearer ${getAccessToken()}`,
    },
  }).then((response) => response.data.result.data);
};

// Mark notifications as read
export const markNotificationsAsRead = async (notificationIds: string[]) => {
  await HttpClient.put(
    API.MARK_AS_READ,
    {
      notificationIds,
    },
    {
      headers: {
        Authorization: `Bearer ${getAccessToken()}`,
      },
    }
  );
};
